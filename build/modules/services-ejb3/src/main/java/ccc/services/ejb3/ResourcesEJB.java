/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.commons.Exceptions;
import ccc.domain.Action;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.ResourceRepository;
import ccc.persistence.streams.ReadToStringAction;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.extensions.ResourcesExt;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.ResourcePath;
import ccc.types.SortOrder;


/**
 * EJB implementation of the {@link ResourcesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Resources.NAME)
@TransactionAttribute(REQUIRED)
@Remote(ResourcesExt.class)
@RolesAllowed({})
public class ResourcesEJB
    extends
        AbstractEJB
    implements
        ResourcesExt {


    /** {@inheritDoc} */
    @Override
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed(ACTION_EXECUTE)
    public void executeAction(final UUID actionId)
    throws RestException {
        try {
            final Action a =
                getRepoFactory().createActionRepository().find(actionId);

            if (new Date().before(a.getExecuteAfter())) {
                return; // Too early.
            }

            executeAction(a);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    //--
    private void executeAction(final Action action) throws RestException {
        switch (action.getType()) {

            case RESOURCE_UNPUBLISH:
                executeUnpublish(action);
                break;

            case RESOURCE_PUBLISH:
                executePublish(action);
                break;

            case PAGE_UPDATE:
                executeUpdate(action);
                break;

            case RESOURCE_DELETE:
                executeDelete(action);
                break;

            default:
                throw new UnsupportedOperationException(
                    "Unsupported action type: "+action.getType());

        }

        action.complete();
    }


    private void executeDelete(final Action action) throws RestException {
        sudoExecute(
            commands().createDeleteResourceCmd(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUpdate(final Action action) throws RestException {
        sudoExecute(
            new ApplyWorkingCopyCommand(
                getRepoFactory(),
                action.getSubject().getId(),
                action.getParams().get("COMMENT"),
                Boolean
                    .valueOf(action.getParams().get("MAJOR"))
                    .booleanValue()),
            action.getActor().getId(),
            new Date());
    }


    private void executePublish(final Action action) throws RestException {
        sudoExecute(
            commands().publishResource(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUnpublish(final Action action) throws RestException {
        sudoExecute(
            commands().unpublishResourceCommand(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }
    //--


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void lock(final UUID resourceId) throws RestException {
        checkPermission(RESOURCE_LOCK);

        execute(
            commands().lockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MOVE)
    public void move(final UUID resourceId,
                     final UUID newParentId) throws RestException {
        try {
            new MoveResourceCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo())
            .execute(
                currentUser(),
                new Date(),
                resourceId,
                newParentId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void publish(final UUID resourceId) throws RestException {
        checkPermission(RESOURCE_PUBLISH);

        execute(
            commands().publishResource(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_RENAME)
    public void rename(final UUID resourceId,
                       final String name) throws RestException {
        execute(
            new RenameResourceCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo(),
                resourceId,
                name));
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void unlock(final UUID resourceId) throws RestException {
        checkPermission(RESOURCE_UNLOCK);
        execute(
            commands().unlockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UNPUBLISH)
    public void unpublish(final UUID resourceId) throws RestException {
        execute(
            commands().unpublishResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void createWorkingCopy(final UUID resourceId,
                                  final long index) throws RestException {
        try {
            new UpdateWorkingCopyCommand(getRepoFactory())
                .execute(
                    currentUser(),
                    new Date(),
                    resourceId,
                    index);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu) throws RestException {
        createWorkingCopy(resourceId, pu.getRevision().longValue());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId)
                                                 throws RestException {
        try {
            new ChangeTemplateForResourceCommand(getRepoFactory()).execute(
                    currentUser(),
                    new Date(),
                    resourceId,
                    templateId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu)
    throws RestException {
        updateResourceTemplate(resourceId, pu.getTemplateId());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include) throws RestException {
        try {
            new IncludeInMainMenuCommand(getRepoFactory()).execute(
                    currentUser(), new Date(), resourceId, include);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void includeInMainMenu(final UUID resourceId) throws RestException {
        includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void excludeFromMainMenu(final UUID resourceId)
    throws RestException {
        includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateMetadata(final UUID resourceId,
                               final Json json) throws RestException {

        final String title = json.getString("title");
        final String description = json.getString("description");
        final String tags = json.getString("tags");
        final Map<String, String> metadata = json.getStringMap("metadata");

        updateMetadata(resourceId, title, description, tags, metadata);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateMetadata(final UUID resourceId,
                                   final String title,
                                   final String description,
                                   final String tags,
                                   final Map<String, String> metadata)
    throws RestException {

        try {
            final Date happenedOn = new Date();
            final UUID actorId = currentUserId();

            new UpdateResourceMetadataCommand(getRepoFactory()).execute(
                    userForId(actorId),
                    happenedOn,
                    resourceId,
                    title,
                    description,
                    tags,
                    metadata);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(SEARCH_CREATE)
    // FIXME: Move to SearchEngineEJB
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title)
                                                 throws RestException {
        try {
            return
                commands().createSearchCommand(
                    parentId,
                    title)
                .execute(currentUser(), new Date())
                .mapResource();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_ACL_UPDATE)
    public void changeRoles(final UUID resourceId,
                            final AclDto acl) throws RestException {

        execute(
            new UpdateResourceRolesCommand(getRepoFactory(), resourceId, acl));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void applyWorkingCopy(final UUID resourceId) throws RestException {
        execute(
            new ApplyWorkingCopyCommand(
                getRepoFactory(),
                resourceId,
                null,
                false));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_CACHE_UPDATE)
    public void updateCacheDuration(final UUID resourceId,
                                    final Duration duration)
                                                 throws RestException {
        execute(
            new UpdateCachingCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo(),
                resourceId,
                duration));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_CACHE_UPDATE)
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu)
    throws RestException {
        updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void clearWorkingCopy(final UUID resourceId)
                                                 throws RestException {
        try {
            new ClearWorkingCopyCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo())
            .execute(
                currentUser(), new Date(), resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_CACHE_UPDATE)
    public void deleteCacheDuration(final UUID id)
    throws RestException {
        updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_DELETE)
    public void deleteResource(final UUID resourceId) throws RestException {
        execute(commands().createDeleteResourceCmd(resourceId));
    }



    /* ====================================================================
     * Queries API.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public Collection<RevisionDto> history(final UUID resourceId)
    throws RestException {
        try {
            return Revision.mapRevisions(
                getRepoFactory()
                .createResourceRepository()
                .history(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public Collection<ResourceSummary> locked() {
        return Resource.mapResources(getResources().locked());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public AclDto roles(final UUID resourceId)
    throws RestException {
        try {
            final AclDto acl =
                getResources().find(Resource.class, resourceId).getAcl();
            return acl;

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public Duration cacheDuration(final UUID resourceId) throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            return r.getCacheDuration();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public TemplateSummary computeTemplate(final UUID resourceId)
    throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            final Template t = r.computeTemplate(null);
            return (null==t) ? null : t.summarize();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public ResourceSummary resourceForPath(final String rootPath)
    throws RestException {
        try {
            return
                getResources().lookup(new ResourcePath(rootPath)).mapResource();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> resourceForMetadataKey(
        final String key) {
        checkPermission(RESOURCE_READ);

        return Resource.mapResources(getResources().lookupWithMetadataKey(key));

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public ResourceSummary resourceForLegacyId(final String legacyId)
    throws RestException {
        try {
            return getResources().lookupWithLegacyId(legacyId).mapResource();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }



    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    @PermitAll
    @Deprecated
    public ResourceSummary lookupWithLegacyId(final String legacyId)
    throws RestException {
        checkPermission(RESOURCE_READ);

        return resourceForLegacyId(legacyId);
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public String getAbsolutePath(final UUID resourceId) throws RestException {
        checkPermission(RESOURCE_READ);

        try {
            return
                getResources().find(Resource.class, resourceId)
                          .getAbsolutePath()
                          .removeTop()
                          .toString();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSnapshot resourceForPathSecure(final String rootPath)
    throws RestException, UnauthorizedException {
        checkPermission(RESOURCE_READ);

        try {
            final ResourcePath rp = new ResourcePath(rootPath);
            final Resource r =
                getResources().lookup(rp);
            checkRead(r);
            return r.forCurrentRevision();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSnapshot workingCopyForPath(final String rootPath)
    throws RestException, UnauthorizedException {
        checkPermission(RESOURCE_READ);

        try {
            final ResourcePath rp = new ResourcePath(rootPath);
            final Resource r =
                getResources().lookup(rp);
            checkRead(r);
            return r.forWorkingCopy();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSnapshot revisionForPath(final String path,
                                            final int version)
    throws RestException, UnauthorizedException {
        checkPermission(RESOURCE_READ);

        try {
            final ResourcePath rp = new ResourcePath(path);
            final Resource r =
                getResources().lookup(rp);
            checkRead(r);
            return r.forSpecificRevision(version);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        checkPermission(RESOURCE_READ);

        final StringBuilder sb = new StringBuilder();
        final ResourcePath rp = new ResourcePath(absolutePath);
        Resource r;
        try {
            r = getResources().lookup(rp);
        } catch (final EntityNotFoundException e) {
            return null;
        }
        if (r instanceof File) {
            final File f = (File) r;
            if (f.isText()) {
                getRepoFactory().createDataRepository().retrieve(
                    f.getData(),
                    new ReadToStringAction(sb, charset)
                );
            }
        }
        return sb.toString();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> getSiblings(final UUID resourceId)
    throws UnauthorizedException {
        checkPermission(RESOURCE_READ);

        final List<ResourceSummary> siblings = new ArrayList<ResourceSummary>();
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            checkRead(r);

            final Folder f = r.getParent().as(Folder.class);
            for (final Resource item : f.getEntries()) {
                siblings.add(item.mapResource());
            }

        } catch (final CccCheckedException e) {
            fail(e);
        }
        return siblings;
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Map<String, String> metadata(final UUID resourceId)
    throws RestException {
        checkPermission(RESOURCE_READ);

        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            return r.getMetadata();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc}
     * @throws UnauthorizedException */
    @Override
    @PermitAll
    public ResourceSummary resource(final UUID resourceId)
    throws RestException, UnauthorizedException {
        checkPermission(RESOURCE_READ);

        try {
            final Resource r = getResources().find(Resource.class, resourceId);
            checkRead(r);
            return
                r.mapResource();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail)
        throws RestException {
        checkPermission(LOG_ENTRY_CREATE);

        try {
            final LogEntry le = new LogEntry(currentUser(),
                                            action,
                                            new Date(),
                                            resourceId,
                                            detail);
            getRepoFactory().createLogEntryRepo().record(le);
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> list(final String tag,
                                            final Long before,
                                            final Long after,
                                            final String sort,
                                            final SortOrder order,
                                            final int pageNo,
                                            final int pageSize) {
        checkPermission(RESOURCE_READ);

        User u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        return
            Resource.mapResources(
                filterAccessibleTo(u,
                    getResources().list(
                        null,
                        tag,
                        (null==before)?null:new Date(before.longValue()),
                        (null==after)?null:new Date(after.longValue()),
                        sort,
                        order,
                        pageNo,
                        pageSize)));
    }


    private Collection<? extends Resource> filterAccessibleTo(
                                            final User u,
                                            final List<Resource> resources) {
        final List<Resource> accessible = new ArrayList<Resource>();
        for (final Resource r : resources) {
            if (r.isReadableBy(u)) { accessible.add(r); }
        }
        return accessible;
    }


    @Deprecated
    private ResourceRepository getResources() {
        return getRepoFactory().createResourceRepository();
    }
}
