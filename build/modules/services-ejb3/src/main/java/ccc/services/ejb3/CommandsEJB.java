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

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.CancelActionCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.CreateAliasCommand;
import ccc.commands.CreateFileCommand;
import ccc.commands.CreateSearchCommand;
import ccc.commands.CreateTemplateCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.LockResourceCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.PublishCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateAliasCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateFileCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateTemplateCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.Action;
import ccc.domain.CccCheckedException;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.FileRepositoryImpl;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.FsCoreData;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.Commands;
import ccc.rest.LocalCommands;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.types.CommandType;
import ccc.types.Duration;
import ccc.types.ID;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Commands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Commands.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(Commands.class)
@Local(LocalCommands.class)
@RolesAllowed({})
public class CommandsEJB
    extends
        BaseCommands
    implements
        Commands, LocalCommands {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private LogEntryRepository           _audit;
    private FileRepositoryImpl _dm;

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createAlias(final ID parentId,
                                       final String name,
                                       final ID targetId)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateAliasCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(parentId),
                    toUUID(targetId),
                    name));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createTemplate(final ID parentId,
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
                    toUUID(parentId),
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
    public void lock(final ID resourceId) throws CommandFailedException {
        lock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void lock(final ID resourceId,
                     final ID actorId,
                     final Date happenedOn) throws CommandFailedException {
        try {
            new LockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void move(final ID resourceId,
                     final ID newParentId) throws CommandFailedException {
        try {
            new MoveResourceCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(newParentId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final ID resourceId) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                new Date(),
                loggedInUser(),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void publish(final ID resourceId,
                        final ID userId,
                        final Date date) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                date,
                userForId(userId),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void rename(final ID resourceId,
                       final String name) throws CommandFailedException {
            try {
                new RenameResourceCommand(_bdao, _audit).rename(
                    loggedInUser(), new Date(), toUUID(resourceId), name);

            } catch (final CccCheckedException e) {
                throw fail(e);
            }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unlock(final ID resourceId) throws CommandFailedException {
        unlock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void unlock(final ID resourceId,
                       final ID actorId,
                       final Date happenedOn) throws CommandFailedException {
        try {
            new UnlockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final ID resourceId) throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void unpublish(final ID resourceId,
                          final ID userId,
                          final Date publishDate)
                                                 throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                _bdao.find(User.class, toUUID(userId)),
                publishDate,
                toUUID(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateAlias(final ID aliasId,
                            final AliasDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(delta.getTargetId()),
                toUUID(aliasId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }



    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createWorkingCopy(final ID resourceId, final long index)
                                                 throws CommandFailedException {
        try {
            final UUID resourceUuid = toUUID(resourceId);

            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                resourceUuid,
                index);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId)
                                                 throws CommandFailedException {
        updateResourceTemplate(
            resourceId, templateId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId,
                                       final ID actorId,
                                       final Date happenedOn)
                                                 throws CommandFailedException {

        try {
            new ChangeTemplateForResourceCommand(_bdao, _audit).execute(
                userForId(actorId),
                happenedOn,
                toUUID(resourceId),
                toUUID(templateId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateTemplate(final ID templateId,
                               final TemplateDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(templateId), delta);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include)
                                                 throws CommandFailedException {
        includeInMainMenu(resourceId, include, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include,
                                  final ID actorId,
                                  final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new IncludeInMainMenuCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId), include);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateMetadata(final ID resourceId,
                                   final String title,
                                   final String description,
                                   final String tags,
                                   final Map<String, String> metadata)
    throws CommandFailedException {

        try {
            final Date happenedOn = new Date();
            final ID actorId = loggedInUserId();

            new UpdateResourceMetadataCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId), title, description, tags, metadata);
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateMetadata(final ID resourceId,
                               final String title,
                               final String description,
                               final String tags,
                               final Map<String, String> metadata,
                               final ID actorId,
                               final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdateResourceMetadataCommand(_bdao, _audit).execute(
                userForId(actorId),
                happenedOn,
                toUUID(resourceId),
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
    public ResourceSummary createSearch(final ID parentId,
                                        final String title)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateSearchCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), title)
            );

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void cancelAction(final ID actionId) {
        new CancelActionCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(actionId));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createAction(final ID resourceId,
                             final CommandType action,
                             final Date executeAfter,
                             final Map<String, String> parameters) {
      // TODO: Use ActionDelta.

      final Action a =
          new Action(
              action,
              executeAfter,
              loggedInUser(),
              _bdao.find(Resource.class, toUUID(resourceId)),
              parameters);

      new ScheduleActionCommand(_bdao, _audit).execute(
          loggedInUser(), new Date(), a);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles)
                                                 throws CommandFailedException {
        changeRoles(resourceId, roles, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles,
                            final ID actorId,
                            final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdateResourceRolesCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId), roles);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void applyWorkingCopy(final ID resourceId)
                                                 throws CommandFailedException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), null, false);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void applyWorkingCopy(final ID resourceId,
                                 final ID userId,
                                 final Date happenedOn,
                                 final boolean isMajorEdit,
                                 final String comment)
                                                 throws CommandFailedException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                _bdao.find(User.class, toUUID(userId)),
                happenedOn,
                toUUID(resourceId),
                comment,
                isMajorEdit);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SITE_BUILDER})
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration)
                                                 throws CommandFailedException {
        try {
            new UpdateCachingCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), duration);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFile(final ID parentFolder,
                                      final FileDelta file,
                                      final String resourceName,
                                      final InputStream dataStream,
                                      final String title,
                                      final String description,
                                      final Date lastUpdated,
                                      final boolean publish,
                                      final String comment,
                                      final boolean isMajorEdit)
                                                 throws CommandFailedException {
        try {
            final User u = loggedInUser();

            final File f =
                new CreateFileCommand(_bdao, _audit, _dm).execute(
                    u,
                    lastUpdated,
                    toUUID(parentFolder),
                    file,
                    title,
                    description,
                    new ResourceName(resourceName),
                    comment,
                    isMajorEdit,
                    dataStream);

            if (publish) {
                f.lock(u);
                new PublishCommand(_audit).execute(lastUpdated, u, f);
                f.unlock(u);
            }

            return mapResource(f);
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateFile(final ID fileId,
                           final FileDelta fileDelta,
                           final String comment,
                           final boolean isMajorEdit,
                           final InputStream dataStream)
                                                 throws CommandFailedException {

        try {
            new UpdateFileCommand(_bdao, _audit, _dm).execute(
                loggedInUser(),
                new Date(),
                UUID.fromString(fileId.toString()),
                fileDelta,
                comment,
                isMajorEdit,
                dataStream);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
        _dm = new FileRepositoryImpl(new FsCoreData(), _bdao);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param e
     * @return
     */
    private CommandFailedException fail(final CccCheckedException e) {
        return fail(_context, e);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    private User loggedInUser() {
        return loggedInUser(_context);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    private ID loggedInUserId() {
        return loggedInUserId(_context);
    }
}
