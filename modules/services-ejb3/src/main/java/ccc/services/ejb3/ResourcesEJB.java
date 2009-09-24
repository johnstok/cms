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

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.CreateSearchCommand;
import ccc.commands.DeleteResourceCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.LockResourceCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.PublishCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.extensions.ResourcesExt;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.ResourcePath;


/**
 * EJB implementation of the {@link ResourcesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Resources.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(ResourcesExt.class)
@RolesAllowed({})
public class ResourcesEJB
    extends
        AbstractEJB
    implements
        ResourcesExt {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void lock(final UUID resourceId) throws RestException {
        try {
            lock(resourceId, currentUserId(), new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void lock(final UUID resourceId,
                     final UUID actorId,
                     final Date happenedOn) throws RestException {
        try {
            new LockResourceCommand(getResources(), getAuditLog()).execute(
                userForId(actorId), happenedOn, resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
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
        try {
            new PublishCommand(getAuditLog()).execute(
                new Date(),
                currentUser(),
                getResources().find(Resource.class, resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final UUID resourceId,
                        final UUID userId,
                        final Date date) throws RestException {
        try {
            new PublishCommand(getAuditLog()).execute(
                date,
                userForId(userId),
                getResources().find(Resource.class, resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void rename(final UUID resourceId,
                       final String name) throws RestException {
            try {
                new RenameResourceCommand(
                    getResources(), getAuditLog()).rename(
                        currentUser(), new Date(), resourceId, name);

            } catch (final CccCheckedException e) {
                throw fail(e);
            }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
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
        try {
            new UnlockResourceCommand(getResources(), getAuditLog()).execute(
                userForId(actorId), happenedOn, resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final UUID resourceId) throws RestException {
        try {
            new UnpublishResourceCommand(
                getResources(), getAuditLog()).execute(
                    currentUser(), new Date(), resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final UUID resourceId,
                          final UUID userId,
                          final Date publishDate)
                                                 throws RestException {
        try {
            new UnpublishResourceCommand(
                getResources(), getAuditLog()).execute(
                    getUsers().find(userId),
                    publishDate,
                    resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createWorkingCopy(final UUID resourceId, final long index)
                                                 throws RestException {
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
                                  final ResourceDto pu)
    throws RestException {
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
    public void includeInMainMenu(final UUID resourceId)
    throws RestException {
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
                new CreateSearchCommand(getResources(), getAuditLog()).execute(
                    currentUser(), new Date(), parentId, title)
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
                getResources(), getAuditLog()).execute(
                    userForId(actorId), happenedOn, resourceId, roles);

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
            new ApplyWorkingCopyCommand(getResources(), getAuditLog()).execute(
                currentUser(), new Date(), resourceId, null, false);

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
            new ApplyWorkingCopyCommand(getResources(), getAuditLog()).execute(
                getUsers().find(userId),
                happenedOn,
                resourceId,
                comment,
                isMajorEdit);

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
            new UpdateCachingCommand(getResources(), getAuditLog()).execute(
                currentUser(), new Date(), resourceId, duration);

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
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
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
            return mapTemplate(r.computeTemplate(null));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
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
    public void delete(final UUID resourceId) throws RestException {
        try {
            new DeleteResourceCommand(getResources(), getAuditLog()).execute(
                currentUser(), new Date(), resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public void delete(final UUID resourceId,
                       final UUID actorId,
                       final Date happenedOn) throws RestException {
        try {
            new DeleteResourceCommand(getResources(), getAuditLog()).execute(
                getUsers().find(actorId), happenedOn, resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
