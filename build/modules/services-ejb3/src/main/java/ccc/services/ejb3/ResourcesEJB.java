/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
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
import ccc.domain.Action;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.persistence.streams.ReadToStringAction;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.extensions.ResourcesExt;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.ResourcePath;


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
    @RolesAllowed({CONTENT_CREATOR})
    public void executeAction(final UUID actionId)
    throws RestException {
        try {
            final Action a = getActions().find(actionId);

            if (new Date().before(a.executeAfter())) {
                return; // Too early.
            }

            executeAction(a);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    //--
    private void executeAction(final Action action) throws RestException {
        switch (action.type()) {

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
                    "Unsupported action type: "+action.type());

        }

        action.complete();
    }


    private void executeDelete(final Action action) throws RestException {
        deleteResource(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }


    private void executeUpdate(final Action action) throws RestException {
        applyWorkingCopy(
            action.subject().id(),
            action.actor().id(),
            new Date(),
            Boolean.valueOf(action.getParams().get("MAJOR")).booleanValue(),
            action.getParams().get("COMMENT"));
    }


    private void executePublish(final Action action) throws RestException {
        publish(action.subject().id(),
            action.actor().id(), new Date());
    }


    private void executeUnpublish(final Action action) throws RestException {
        unpublish(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }
    //--


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR, API_USER})
    public void lock(final UUID resourceId) throws RestException {
        execute(
            commands().lockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void lock(final UUID resourceId,
                     final UUID actorId,
                     final Date happenedOn) throws RestException {
        sudoExecute(
            commands().lockResourceCommand(resourceId), actorId, happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void move(final UUID resourceId,
                     final UUID newParentId) throws RestException {
        try {
            new MoveResourceCommand(getResources(), getAuditLog()).execute(
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
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final UUID resourceId) throws RestException {
        execute(
            commands().publishResource(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final UUID resourceId,
                        final UUID userId,
                        final Date date) throws RestException {
        sudoExecute(
            commands().publishResource(resourceId), userId, date);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void rename(final UUID resourceId,
                       final String name) throws RestException {
            try {
                new RenameResourceCommand(
                    getResources(),
                    getAuditLog(),
                    resourceId,
                    name)
                .execute(
                    currentUser(),
                    new Date());

            } catch (final CccCheckedException e) {
                throw fail(e);
            }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR, API_USER})
    public void unlock(final UUID resourceId) throws RestException {
        try {
            unlock(resourceId, currentUserId(), new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void unlock(final UUID resourceId,
                       final UUID actorId,
                       final Date happenedOn) throws RestException {
        sudoExecute(
            commands().unlockResourceCommand(resourceId), actorId, happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final UUID resourceId) throws RestException {
        execute(
            commands().unpublishResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final UUID resourceId,
                          final UUID userId,
                          final Date publishDate) throws RestException {
        sudoExecute(
            commands().unpublishResourceCommand(resourceId),
            userId,
            publishDate);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createWorkingCopy(final UUID resourceId,
                                  final long index) throws RestException {
        try {
            new UpdateWorkingCopyCommand(
                getResources(), getAuditLog()).execute(
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
    @RolesAllowed({CONTENT_CREATOR})
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu) throws RestException {
        createWorkingCopy(resourceId, pu.getRevision().longValue());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId)
                                                 throws RestException {
        try {
            updateResourceTemplate(
                resourceId, templateId, currentUserId(), new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu)
    throws RestException {
        updateResourceTemplate(resourceId, pu.getTemplateId());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId,
                                       final UUID actorId,
                                       final Date happenedOn)
                                                 throws RestException {

        try {
            new ChangeTemplateForResourceCommand(
                getResources(), getAuditLog()).execute(
                    userForId(actorId),
                    happenedOn,
                    resourceId,
                    templateId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include)
                                                 throws RestException {
        try {
            includeInMainMenu(resourceId, include, currentUserId(), new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void includeInMainMenu(final UUID resourceId) throws RestException {
        includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void excludeFromMainMenu(final UUID resourceId)
    throws RestException {
        includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include,
                                  final UUID actorId,
                                  final Date happenedOn)
                                                 throws RestException {
        try {
            new IncludeInMainMenuCommand(
                getResources(), getAuditLog()).execute(
                    userForId(actorId), happenedOn, resourceId, include);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
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
    @RolesAllowed({CONTENT_CREATOR})
    public void updateMetadata(final UUID resourceId,
                                   final String title,
                                   final String description,
                                   final String tags,
                                   final Map<String, String> metadata)
    throws RestException {

        try {
            final Date happenedOn = new Date();
            final UUID actorId = currentUserId();

            new UpdateResourceMetadataCommand(
                getResources(), getAuditLog()).execute(
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
    @RolesAllowed({ADMINISTRATOR})
    public void updateMetadata(final UUID resourceId,
                               final String title,
                               final String description,
                               final String tags,
                               final Map<String, String> metadata,
                               final UUID actorId,
                               final Date happenedOn)
                                                 throws RestException {
        try {
            new UpdateResourceMetadataCommand(
                getResources(), getAuditLog()).execute(
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
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title)
                                                 throws RestException {
        try {
            return mapResource(
                commands().createSearchCommand(
                    parentId,
                    title)
                .execute(currentUser(), new Date())
            );

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void changeRoles(final UUID resourceId,
                            final Collection<String> roles)
                                                 throws RestException {
        try {
            changeRoles(resourceId, roles, currentUserId(), new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void changeRoles(final UUID resourceId,
                            final Collection<String> roles,
                            final UUID actorId,
                            final Date happenedOn)
                                                 throws RestException {
        try {
            new UpdateResourceRolesCommand(
                getResources(),
                getAuditLog(),
                resourceId,
                roles)
            .execute(
                userForId(actorId),
                happenedOn);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void applyWorkingCopy(final UUID resourceId)
                                                 throws RestException {
        try {
            new ApplyWorkingCopyCommand(
                getResources(),
                getAuditLog(),
                resourceId,
                null,
                false)
            .execute(
                currentUser(),
                new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void applyWorkingCopy(final UUID resourceId,
                                 final UUID userId,
                                 final Date happenedOn,
                                 final boolean isMajorEdit,
                                 final String comment)
                                                 throws RestException {
        try {
            new ApplyWorkingCopyCommand(
                getResources(),
                getAuditLog(),
                resourceId,
                comment,
                isMajorEdit)
            .execute(
                getUsers().find(userId),
                happenedOn);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void updateCacheDuration(final UUID resourceId,
                                    final Duration duration)
                                                 throws RestException {
        try {
            new UpdateCachingCommand(
                getResources(),
                getAuditLog(),
                resourceId,
                duration)
            .execute(
                currentUser(),
                new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu)
    throws RestException {
        updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void clearWorkingCopy(final UUID resourceId)
                                                 throws RestException {
        try {
            new ClearWorkingCopyCommand(getResources(), getAuditLog()).execute(
                currentUser(), new Date(), resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void deleteCacheDuration(final UUID id)
    throws RestException {
        updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() {
        throw new UnsupportedOperationException("Method not implemented.");
    }



    /* ====================================================================
     * Queries API.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<RevisionDto> history(final UUID resourceId)
    throws RestException {
        try {
            return mapLogEntries(getResources().history(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> locked() {
        return mapResources(getResources().locked());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public ResourceSummary resource(final UUID resourceId)
    throws RestException {
        try {
            return
                mapResource(getResources().find(Resource.class, resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Map<String, String> metadata(final UUID resourceId)
    throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            return r.metadata();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<String> roles(final UUID resourceId)
    throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            return r.roles();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Duration cacheDuration(final UUID resourceId) throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            return r.cache();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public TemplateSummary computeTemplate(final UUID resourceId)
    throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, resourceId);
            final Template t = r.computeTemplate(null);
            return (null==t) ? null : t.mapTemplate();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public ResourceSummary resourceForPath(final String rootPath)
    throws RestException {
        try {
            final ResourcePath rp = new ResourcePath(rootPath);
            return mapResource(
                getResources().lookup(rp.top().toString(), rp.removeTop()));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> resourceForMetadataKey(
        final String key) {
        return mapResources(getResources().lookupWithMetadataKey(key));

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public ResourceSummary resourceForLegacyId(final String legacyId)
    throws RestException {
        try {
            return mapResource(getResources().lookupWithLegacyId(legacyId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public void deleteResource(final UUID resourceId) throws RestException {
        execute(commands().createDeleteResourceCmd(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public void deleteResource(final UUID resourceId,
                               final UUID actorId,
                               final Date happenedOn) throws RestException {
        sudoExecute(
            commands().createDeleteResourceCmd(resourceId),
            actorId,
            happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail)
        throws RestException {
        try {
            final LogEntry le = new LogEntry(currentUser(),
                                            action,
                                            new Date(),
                                            resourceId,
                                            detail);
            getAuditLog().record(le);
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
        return resourceForLegacyId(legacyId);
    }

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public String getAbsolutePath(final UUID resourceId) throws RestException {
        try {
            return
                getResources().find(Resource.class, resourceId)
                          .absolutePath()
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
        try {
            final ResourcePath rp = new ResourcePath(rootPath);
            final Resource r =
                getResources().lookup(rp.top().toString(), rp.removeTop());
            checkSecurity(r);
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
        try {
            final ResourcePath rp = new ResourcePath(rootPath);
            final Resource r =
                getResources().lookup(rp.top().toString(), rp.removeTop());
            checkSecurity(r);
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
        try {
            final ResourcePath rp = new ResourcePath(path);
            final Resource r =
                getResources().lookup(rp.top().toString(), rp.removeTop());
            checkSecurity(r);
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
        final StringBuilder sb = new StringBuilder();
        final ResourcePath rp = new ResourcePath(absolutePath);
        Resource r;
        try {
            r = getResources().lookup(rp.top().toString(), rp.removeTop());
        } catch (final EntityNotFoundException e) {
            return null;
        }
        if (r instanceof File) {
            final File f = (File) r;
            if (f.isText()) {
                getFiles().retrieve(
                    f.data(),
                    new ReadToStringAction(sb, charset)
                );
            }
        }
        return sb.toString();
    }


/* StatefulReaderImpl */
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public IResource resourceFromPath(final String absolutePath) {
//        return continuityForPath(absolutePath).forCurrentRevision();
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public IResource resourceFromId(final String id) {
//        try {
//            return _resources.find(
//                Resource.class, UUID.fromString(id)).forCurrentRevision();
//        } catch (final EntityNotFoundException e) {
//            throw new NotFoundException();
//        }
//    }
//
//
//    private Resource continuityForPath(final String absolutePath) {
//        final ResourcePath rp = new ResourcePath(absolutePath);
//        try {
//            return getResources().lookup(
//                rp.top().toString(), rp.removeTop(), currentUser());
//        } catch (final EntityNotFoundException e) {
//            return null;
//        }
//    }
//
//
//    /** {@inheritDoc} */
//    @Override
//    public UUID uuidFromString(final String id) {
//        return UUID.fromString(id);
//    }
}
