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
package ccc.api.ejb3;

import static javax.ejb.TransactionAttributeType.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import org.apache.log4j.Logger;

import ccc.api.AliasDelta;
import ccc.api.FileDelta;
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserSummary;
import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.CancelActionCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.CommandFailedException;
import ccc.commands.CreateAliasCommand;
import ccc.commands.CreateFileCommand;
import ccc.commands.CreateFolderCommand;
import ccc.commands.CreatePageCommand;
import ccc.commands.CreateRootCommand;
import ccc.commands.CreateSearchCommand;
import ccc.commands.CreateTemplateCommand;
import ccc.commands.CreateUserCommand;
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
import ccc.commands.UpdateCurrentUserAction;
import ccc.commands.UpdateFileCommand;
import ccc.commands.UpdateFolderCommand;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateTemplateCommand;
import ccc.commands.UpdateUserCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.Action;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceOrder;
import ccc.domain.User;
import ccc.entities.ResourceName;
import ccc.persistence.jpa.BaseDao;
import ccc.persistence.jpa.FsCoreData;
import ccc.services.AuditLog;
import ccc.services.Commands;
import ccc.services.LocalCommands;
import ccc.services.ModelTranslation;
import ccc.services.UserLookup;
import ccc.services.impl.AuditLogImpl;
import ccc.services.impl.DataManagerImpl;
import ccc.types.CommandType;
import ccc.types.Duration;
import ccc.types.ID;
import ccc.types.Paragraph;


/**
 * EJB implementation of the {@link updateMetadata} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Commands.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(Commands.class)
@Local(LocalCommands.class)
@RolesAllowed({}) // "ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"
public class CommandsEJB
    extends
        ModelTranslation
    implements
        Commands, LocalCommands {
    private static final Logger LOG = Logger.getLogger(CommandsEJB.class);

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private AuditLog           _audit;
    private UserLookup         _userLookup;
    private BaseDao            _bdao;

    private DataManagerImpl _dm;

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public ResourceSummary createFolder(final ID parentId,
                                        final String name)
                                                 throws CommandFailedException {
        return createFolder(parentId, name, null, false);

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish)
                                                 throws CommandFailedException {
        return createFolder(
            parentId, name, title, publish, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish,
                                        final ID actorId,
                                        final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            final User u = userForId(actorId);

            final Folder f =
                new CreateFolderCommand(_bdao, _audit).execute(
                    u, happenedOn, toUUID(parentId), name, title);

            if (publish) {
                f.lock(u);
                new PublishCommand(_audit).execute(happenedOn, u, f);
                f.unlock(u);
            }

            return mapResource(f);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId,
                                      final String title,
                                      final String comment,
                                      final boolean majorChange)
                                                 throws CommandFailedException {
        return createPage(
            parentId,
            delta,
            name,
            publish,
            templateId,
            title,
            loggedInUserId(),
            new Date(),
            comment,
            majorChange);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId,
                                      final String title,
                                      final ID actorId,
                                      final Date happenedOn,
                                      final String comment,
                                      final boolean majorChange)
                                                 throws CommandFailedException {
        try {
            final User u = userForId(actorId);

            final Page p = new CreatePageCommand(_bdao, _audit).execute(
                u,
                happenedOn,
                toUUID(parentId),
                delta,
                ResourceName.escape(name),
                title,
                toUUID(templateId),
                comment,
                majorChange);

            if (publish) {
                p.lock(u);
                new PublishCommand(_audit).execute(happenedOn, u, p);
                p.unlock(u);
            }

            return mapResource(p);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public UserSummary createUser(final UserSummary delta) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), delta));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void lock(final ID resourceId) throws CommandFailedException {
        lock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void lock(final ID resourceId,
                     final ID actorId,
                     final Date happenedOn) throws CommandFailedException {
        try {
            new LockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void move(final ID resourceId,
                     final ID newParentId) throws CommandFailedException {
        try {
            new MoveResourceCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(newParentId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void publish(final ID resourceId) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                new Date(),
                loggedInUser(),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void publish(final ID resourceId,
                        final ID userId,
                        final Date date) throws CommandFailedException {
        try {
            new PublishCommand(_audit).execute(
                date,
                userForId(userId),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void rename(final ID resourceId,
                       final String name) throws CommandFailedException {
            try {
                new RenameResourceCommand(_bdao, _audit).rename(
                    loggedInUser(), new Date(), toUUID(resourceId), name);

            } catch (final RemoteExceptionSupport e) {
                throw fail(e);
            }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void unlock(final ID resourceId) throws CommandFailedException {
        unlock(resourceId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void unlock(final ID resourceId,
                       final ID actorId,
                       final Date happenedOn) throws CommandFailedException {
        try {
            new UnlockResourceCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void unpublish(final ID resourceId) throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void unpublish(final ID resourceId,
                          final ID userId,
                          final Date publishDate)
                                                 throws CommandFailedException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                _bdao.find(User.class, toUUID(userId)),
                publishDate,
                toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateAlias(final ID aliasId,
                            final AliasDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(delta.getTargetId()),
                toUUID(aliasId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit)
                                                 throws CommandFailedException {
        updatePage(
            pageId, delta, comment, isMajorEdit, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit,
                           final ID actorId,
                           final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdatePageCommand(_bdao, _audit).execute(
                userForId(actorId),
                happenedOn,
                toUUID(pageId),
                delta,
                comment,
                isMajorEdit);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateWorkingCopy(final ID pageId,
                                  final PageDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(pageId),
                delta);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void createWorkingCopy(final ID resourceId, final long index)
                                                 throws CommandFailedException {
        try {
            final UUID resourceUuid = toUUID(resourceId);

            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                resourceUuid,
                index);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId)
                                                 throws CommandFailedException {
        updateResourceTemplate(
            resourceId, templateId, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateTemplate(final ID templateId,
                               final TemplateDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(templateId), delta);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updateUser(final ID userId, final UserSummary delta) {
        new UpdateUserCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(userId), delta);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public ResourceSummary createRoot(final String name)
                                                 throws CommandFailedException {
        try {
            final Folder f = new Folder(name);
            new CreateRootCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), f);
            return mapResource(f);
        } catch (final ResourceExistsException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include)
                                                 throws CommandFailedException {
        includeInMainMenu(resourceId, include, loggedInUserId(), new Date());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include,
                                  final ID actorId,
                                  final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new IncludeInMainMenuCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId), include);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public List<String> validateFields(final Set<Paragraph> delta,
                                       final String definition) {
        return new PageHelper().validateFields(delta, definition);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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
        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }



    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateFolder(final ID folderId,
                             final String sortOrder,
                             final ID indexPageId,
                             final Collection<String> sortList)
                                                 throws CommandFailedException {
        try {
            final List<UUID> list = new ArrayList<UUID>();

            for (final String item : sortList) {
                list.add(UUID.fromString(item));
            }

            new UpdateFolderCommand(_bdao, _audit).execute(
                loggedInUser(),
                 new Date(),
                 toUUID(folderId),
                 ResourceOrder.valueOf(sortOrder),
                 toUUID(indexPageId),
                 list);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void clearWorkingCopy(final ID resourceId)
                                                 throws CommandFailedException {
        try {
            new ClearWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public ResourceSummary createSearch(final ID parentId,
                                        final String title)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateSearchCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), title)
            );

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void cancelAction(final ID actionId) {
        new CancelActionCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(actionId));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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
    @RolesAllowed({"CONTENT_CREATOR"})
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles)
                                                 throws CommandFailedException {
        changeRoles(resourceId, roles, loggedInUserId(), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles,
                            final ID actorId,
                            final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdateResourceRolesCommand(_bdao, _audit).execute(
                userForId(actorId), happenedOn, toUUID(resourceId), roles);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void applyWorkingCopy(final ID resourceId)
                                                 throws CommandFailedException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), null, false);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"SITE_BUILDER"})
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration)
                                                 throws CommandFailedException {
        try {
            new UpdateCachingCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), duration);

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updateUserPassword(final ID userId, final String password) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(userId), password);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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
        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
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

        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc}
     * @throws CommandFailedException */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateYourUser(final ID userId, final String email, final String password) throws CommandFailedException {
        try {
        new UpdateCurrentUserAction(_bdao, _audit).execute(
        loggedInUser(), new Date(), toUUID(userId), email, password);
        } catch (final RemoteExceptionSupport e) {
            throw fail(e);
        }
    }

    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new BaseDao(_em);
        _audit = new AuditLogImpl(_bdao);
        _userLookup = new UserLookup(_bdao);
        _dm = new DataManagerImpl(new FsCoreData(), _bdao);
    }


    private User userForId(final ID userId) {
        final User u = _bdao.find(User.class, toUUID(userId));
        return u;
    }

    private User loggedInUser() {
        return _userLookup.loggedInUser(_context.getCallerPrincipal());
    }

    private ID loggedInUserId() {
        return new ID(loggedInUser().id().toString());
    }

    private CommandFailedException fail(final RemoteExceptionSupport e) {
        _context.setRollbackOnly();  // CRITICAL
        final CommandFailedException cfe = e.toRemoteException();
        LOG.info(
            "Handled local exception: "+cfe.getFailure().getExceptionId(), e);
        return cfe;
    }
}
