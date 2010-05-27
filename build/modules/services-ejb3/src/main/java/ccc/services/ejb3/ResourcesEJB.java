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

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.ACL;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Resources;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.Duration;
import ccc.api.types.ResourcePath;
import ccc.api.types.SortOrder;
import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceAclCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.commons.Exceptions;
import ccc.commons.streams.ReadToStringAction;
import ccc.domain.ActionEntity;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionEntity;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;
import ccc.persistence.ResourceCriteria;
import ccc.persistence.ResourceRepository;
import ccc.rest.extensions.ResourcesExt;


/**
 * EJB implementation of the {@link ResourcesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Resources.NAME)
@TransactionAttribute(REQUIRED)
@Remote(ResourcesExt.class)
@Local(Resources.class)
@RolesAllowed({})
public class ResourcesEJB
    extends
        AbstractEJB
    implements
        Resources,
        ResourcesExt {


    /** {@inheritDoc} */
    @Override
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed(ACTION_EXECUTE)
    public void executeAction(final UUID actionId) {
            final ActionEntity a =
                getRepoFactory().createActionRepository().find(actionId);

            if (new Date().before(a.getExecuteAfter())) {
                return; // Too early.
            }

            executeAction(a);
    }


    private void executeAction(final ActionEntity action) {
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


    private void executeDelete(final ActionEntity action) {
        sudoExecute(
            commands().createDeleteResourceCmd(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUpdate(final ActionEntity action) {
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


    private void executePublish(final ActionEntity action) {
        sudoExecute(
            commands().publishResource(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUnpublish(final ActionEntity action) {
        sudoExecute(
            commands().unpublishResourceCommand(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void lock(final UUID resourceId) {
        checkPermission(RESOURCE_LOCK);

        execute(
            commands().lockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MOVE)
    public void move(final UUID resourceId, final UUID newParentId) {
        new MoveResourceCommand(
            getRepoFactory().createResourceRepository(),
            getRepoFactory().createLogEntryRepo())
        .execute(
            currentUser(),
            new Date(),
            resourceId,
            newParentId);
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void publish(final UUID resourceId) {
        checkPermission(RESOURCE_PUBLISH);

        execute(
            commands().publishResource(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_RENAME)
    public void rename(final UUID resourceId, final String name) {
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
    public void unlock(final UUID resourceId) {
        checkPermission(RESOURCE_UNLOCK);
        execute(
            commands().unlockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UNPUBLISH)
    public void unpublish(final UUID resourceId) {
        execute(
            commands().unpublishResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void createWorkingCopy(final UUID resourceId, final long index) {
        new UpdateWorkingCopyCommand(getRepoFactory())
            .execute(
                currentUser(),
                new Date(),
                resourceId,
                index);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void createWorkingCopy(final UUID resourceId,
                                  final Resource pu) {
        createWorkingCopy(resourceId, pu.getRevision());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId) {
        new ChangeTemplateForResourceCommand(getRepoFactory()).execute(
                currentUser(),
                new Date(),
                resourceId,
                templateId);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateResourceTemplate(final UUID resourceId,
                                       final Resource pu) {
        updateResourceTemplate(resourceId, pu.getTemplate());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include) {
        new IncludeInMainMenuCommand(getRepoFactory()).execute(
                currentUser(), new Date(), resourceId, include);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void includeInMainMenu(final UUID resourceId) {
        includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_MM)
    public void excludeFromMainMenu(final UUID resourceId) {
        includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateMetadata(final UUID resourceId,
                               final Resource resource) {

        updateMetadata(
            resourceId,
            resource.getTitle(),
            resource.getDescription(),
            resource.getTags(),
            resource.getMetadata());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void updateMetadata(final UUID resourceId,
                               final String title,
                               final String description,
                               final Set<String> tags,
                               final Map<String, String> metadata) {

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
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(SEARCH_CREATE)
    // FIXME: Move to SearchEngineEJB
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title) {
        return
            commands().createSearchCommand(
                parentId,
                title)
            .execute(currentUser(), new Date())
            .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_ACL_UPDATE)
    public void changeAcl(final UUID resourceId, final ACL acl) {
        execute(
            new UpdateResourceAclCommand(getRepoFactory(), resourceId, acl));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void applyWorkingCopy(final UUID resourceId) {
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
                                    final Duration duration) {
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
                                    final Resource pu) {
        updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_UPDATE)
    public void clearWorkingCopy(final UUID resourceId) {
        new ClearWorkingCopyCommand(
            getRepoFactory().createResourceRepository(),
            getRepoFactory().createLogEntryRepo())
        .execute(
            currentUser(), new Date(), resourceId);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_CACHE_UPDATE)
    public void deleteCacheDuration(final UUID id) {
        updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_DELETE)
    public void delete(final UUID resourceId) {
        execute(commands().createDeleteResourceCmd(resourceId));
    }



    /* ====================================================================
     * Queries API.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public PagedCollection<Revision> history(final UUID resourceId) {
        final List<Revision> revisions = RevisionEntity.mapRevisions(
            getRepoFactory()
            .createResourceRepository()
            .history(resourceId));
        return
            new PagedCollection<Revision>(
                revisions.size(), Revision.class, revisions);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public ACL acl(final UUID resourceId) {
        final ACL acl =
            getResources().find(ResourceEntity.class, resourceId).getAcl();
        return acl;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public Duration cacheDuration(final UUID resourceId) {
        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        return r.getCacheDuration();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public Template computeTemplate(final UUID resourceId) {
        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        final TemplateEntity t = r.computeTemplate(null);
        return (null==t) ? null : t.summarize();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(RESOURCE_READ)
    public ResourceSummary resourceForPath(final String rootPath) {
        return
            getResources().lookup(new ResourcePath(rootPath)).mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public PagedCollection<ResourceSummary> resourceForMetadataKey(
        final String key) {
        checkPermission(RESOURCE_READ);
        final List<ResourceSummary> list =
         ResourceEntity.mapResources(getResources().lookupWithMetadataKey(key));
        return
            new PagedCollection<ResourceSummary>(
                list.size(), ResourceSummary.class, list);
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        checkPermission(RESOURCE_READ);
        return getResources().lookupWithLegacyId(legacyId).mapResource();
    }



    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public String getAbsolutePath(final UUID resourceId) {
        checkPermission(RESOURCE_READ);
        return
            getResources().find(ResourceEntity.class, resourceId)
                      .getAbsolutePath()
                      .removeTop()
                      .toString();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Resource resourceForPathSecure(final String rootPath) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(rootPath);
        final ResourceEntity r = getResources().lookup(rp);
        checkRead(r);
        return r.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Resource workingCopyForPath(final String rootPath) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(rootPath);
        final ResourceEntity r =
            getResources().lookup(rp);
        checkRead(r);
        return r.forWorkingCopy();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Resource revisionForPath(final String path,
                                            final int version) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(path);
        final ResourceEntity r =
            getResources().lookup(rp);
        checkRead(r);
        return r.forSpecificRevision(version);
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        checkPermission(RESOURCE_READ);

        final StringBuilder sb = new StringBuilder();
        final ResourcePath rp = new ResourcePath(absolutePath);
        ResourceEntity r;
        try {
            r = getResources().lookup(rp);
        } catch (final EntityNotFoundException e) {
            return null;
        }
        if (r instanceof FileEntity) {
            final FileEntity f = (FileEntity) r;
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
    public Map<String, String> metadata(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        return r.getMetadata();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSummary retrieve(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        checkRead(r);

        return r.mapResource();
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail) {
        checkPermission(LOG_ENTRY_CREATE);

        final LogEntry le = new LogEntry(currentUser(),
                                        action,
                                        new Date(),
                                        resourceId,
                                        detail);
        getRepoFactory().createLogEntryRepo().record(le);
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public PagedCollection<ResourceSummary> list(final UUID parent,
        final String tag,
        final Long before,
        final Long after,
        final String mainMenu,
        final String type,
        final String locked,
        final String published,
        final String sort,
        final SortOrder order,
        final int pageNo,
        final int pageSize) {
        checkPermission(RESOURCE_READ);

        UserEntity u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        final ResourceCriteria criteria = new ResourceCriteria();
        FolderEntity f = null;
        if (parent != null) {
            f =
                getRepoFactory()
                .createResourceRepository()
                .find(FolderEntity.class, parent);
            checkRead(f);
            criteria.setParent(parent);
        }

        criteria.setTag(tag);
        criteria.setChangedBefore(
            (null==before)?null:new Date(before.longValue()));
        criteria.setChangedAfter(
            (null==after)?null:new Date(after.longValue()));
        criteria.setMainmenu(mainMenu);
        criteria.setType(type);
        criteria.setPublished(published);
        criteria.setLocked(locked);

        final List<ResourceSummary> list = ResourceEntity.mapResources(
            filterAccessibleTo(u,
                getResources().list(criteria,
                    f,
                    sort,
                    order,
                    pageNo,
                    pageSize)));

        final long count = getResources().totalCount(criteria, f);

        return
            new PagedCollection<ResourceSummary>(
                count, ResourceSummary.class, list);
    }


    private Collection<? extends ResourceEntity> filterAccessibleTo(
                                        final UserEntity u,
                                        final List<ResourceEntity> resources) {
        final List<ResourceEntity> accessible = new ArrayList<ResourceEntity>();
        for (final ResourceEntity r : resources) {
            if (r.isReadableBy(u)) { accessible.add(r); }
        }
        return accessible;
    }


    @Deprecated
    private ResourceRepository getResources() {
        return getRepoFactory().createResourceRepository();
    }

}
