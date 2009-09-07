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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.CreateAliasCommand;
import ccc.commands.CreateSearchCommand;
import ccc.commands.CreateTemplateCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.LockResourceCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.PublishCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateAliasCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateTemplateCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.migration.ResourcesExt;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;


/**
 * EJB implementation of the {@link ResourcesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=ResourcesExt.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(ResourcesExt.class)
@RolesAllowed({})
public class CommandsEJB
    extends
        BaseCommands
    implements
        ResourcesExt {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private LogEntryRepository _audit;

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createAlias(final UUID parentId,
                                       final String name,
                                       final UUID targetId)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateAliasCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    parentId,
                    targetId,
                    name));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createTemplate(final UUID parentId,
                                          final TemplateDelta delta,
                                          final String title,
                                          final String description,
                                          final String name)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateTemplateCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    parentId,
                    delta,
                    title,
                    description,
                    new ResourceName(name)));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void lock(final UUID resourceId) throws CommandFailedException {
        lock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void lock(final UUID resourceId,
                     final UUID actorId,
                     final Date happenedOn) throws CommandFailedException {
        try {
            new LockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void move(final UUID resourceId,
                     final UUID newParentId) throws CommandFailedException {
        try {
            new MoveResourceCommand(_bdao, _audit).execute(
                loggedInUser(),
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
    public void publish(final UUID resourceId) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                new Date(),
                loggedInUser(),
                _bdao.find(Resource.class, resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final UUID resourceId,
                        final UUID userId,
                        final Date date) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                date,
                userForId(userId),
                _bdao.find(Resource.class, resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void rename(final UUID resourceId,
                       final String name) throws CommandFailedException {
            try {
                new RenameResourceCommand(_bdao, _audit).rename(
                    loggedInUser(), new Date(), resourceId, name);

            } catch (final CccCheckedException e) {
                throw fail(e);
            }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unlock(final UUID resourceId) throws CommandFailedException {
        unlock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void unlock(final UUID resourceId,
                       final UUID actorId,
                       final Date happenedOn) throws CommandFailedException {
        try {
            new UnlockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final UUID resourceId) throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), resourceId);

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
                                                 throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                _bdao.find(User.class, userId),
                publishDate,
                resourceId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateAlias(final UUID aliasId,
                            final AliasDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                delta.getTargetId(),
                aliasId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }



    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createWorkingCopy(final UUID resourceId, final long index)
                                                 throws CommandFailedException {
        try {

            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(),
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
    throws CommandFailedException {
        createWorkingCopy(resourceId, pu.getRevision());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId)
                                                 throws CommandFailedException {
        updateResourceTemplate(
            resourceId, templateId, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu)
    throws CommandFailedException {
        updateResourceTemplate(resourceId, pu.getTemplateId());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId,
                                       final UUID actorId,
                                       final Date happenedOn)
                                                 throws CommandFailedException {

        try {
            new ChangeTemplateForResourceCommand(_bdao, _audit).execute(
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
    public void updateTemplate(final UUID templateId,
                               final TemplateDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), templateId, delta);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include)
                                                 throws CommandFailedException {
        includeInMainMenu(resourceId, include, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void includeInMainMenu(final UUID resourceId)
    throws CommandFailedException {
        includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void excludeFromMainMenu(final UUID resourceId)
    throws CommandFailedException {
        includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include,
                                  final UUID actorId,
                                  final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new IncludeInMainMenuCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, resourceId, include);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateMetadata(final UUID resourceId,
                               final Json json) throws CommandFailedException {

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
    throws CommandFailedException {

        try {
            final Date happenedOn = new Date();
            final UUID actorId = loggedInUserId();

            new UpdateResourceMetadataCommand(_bdao, _audit).execute(
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
                                                 throws CommandFailedException {
        try {
            new UpdateResourceMetadataCommand(_bdao, _audit).execute(
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
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateSearchCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), parentId, title)
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
                                                 throws CommandFailedException {
        changeRoles(resourceId, roles, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void changeRoles(final UUID resourceId,
                            final Collection<String> roles,
                            final UUID actorId,
                            final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdateResourceRolesCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, resourceId, roles);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void applyWorkingCopy(final UUID resourceId)
                                                 throws CommandFailedException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), resourceId, null, false);

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
                                                 throws CommandFailedException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                _bdao.find(User.class, userId),
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
                                                 throws CommandFailedException {
        try {
            new UpdateCachingCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), resourceId, duration);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu)
    throws CommandFailedException {
        updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void clearWorkingCopy(final UUID resourceId)
                                                 throws CommandFailedException {
        try {
            new ClearWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(_context), new Date(), resourceId);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void deleteCacheDuration(final UUID id)
    throws CommandFailedException {
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
    public String getAbsolutePath(final UUID resourceId) {
        return
            _resources.find(Resource.class, resourceId)
                      .absolutePath()
                      .toString();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<RevisionDto> history(final UUID resourceId) {
        return mapLogEntries(_resources.history(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> locked() {
        return mapResources(_resources.locked());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return mapResources(_resources.lockedByUser(currentUser()));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public ResourceSummary resource(final UUID resourceId) {
        return
            mapResource(_resources.find(Resource.class, resourceId));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Map<String, String> metadata(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.metadata();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<String> roles(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.roles();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Duration cacheDuration(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.cache();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public TemplateSummary computeTemplate(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return mapTemplate(r.computeTemplate(null));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public ResourceSummary resourceForPath(final String rootPath) {
        final ResourcePath rp = new ResourcePath(rootPath);
        return mapResource(
            _resources.lookup(rp.top().toString(), rp.removeTop()));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return mapResource(_resources.lookupWithLegacyId(legacyId));
    }


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
        _resources = new ResourceRepositoryImpl(_bdao);
    }

    private User currentUser() {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }

    private CommandFailedException fail(final CccCheckedException e) {
        return fail(_context, e);
    }

    private User loggedInUser() {
        return loggedInUser(_context);
    }

    private UUID loggedInUserId() {
        return loggedInUserId(_context);
    }
}
