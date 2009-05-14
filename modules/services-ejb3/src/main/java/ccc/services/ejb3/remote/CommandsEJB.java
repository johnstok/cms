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
package ccc.services.ejb3.remote;

import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.CancelActionCommand;
import ccc.commands.ChangeResourceTagsCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.CreateAliasCommand;
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
import ccc.commands.ReorderFolderContentsCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateAliasCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateFolderCommand;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateResourceMetadataRolesCommand;
import ccc.commands.UpdateResourceRolesCommand;
import ccc.commands.UpdateTemplateCommand;
import ccc.commands.UpdateUserCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.Action;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.Paragraph;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.ResourceOrder;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.persistence.jpa.BaseDao;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.ModelTranslation;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.UserLookup;
import ccc.services.api.ActionType;
import ccc.services.api.AliasDelta;
import ccc.services.api.CCCRemoteException;
import ccc.services.api.Commands;
import ccc.services.api.Duration;
import ccc.services.api.ID;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * EJB implementation of the {@link Commands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Commands.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Commands.class)
@RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
public class CommandsEJB
    extends
        ModelTranslation
    implements
        Commands {
    private static final Logger LOG = Logger.getLogger(CommandsEJB.class);

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private ResourceDao        _resources;
    private AuditLog           _audit;
    private UserLookup         _userLookup;
    private BaseDao            _bdao;

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId,
                                       final String name,
                                       final ID targetId)
                                                     throws CCCRemoteException {
        try {
            return mapResource(
                new CreateAliasCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(parentId),
                    toUUID(targetId),
                    name));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.ALIAS_CREATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name)
                                                     throws CCCRemoteException {
        return createFolder(parentId, name, null);

    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title)
                                                     throws CCCRemoteException {
        try {
            return mapResource(
                new CreateFolderCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), name, title));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.FOLDER_CREATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId)
                                                     throws CCCRemoteException {
        try {
            final Page p = new CreatePageCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(parentId),
                delta,
                publish,
                ResourceName.escape(name),
                toUUID(templateId));
            return mapResource(p);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.PAGE_CREATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name)
                                                     throws CCCRemoteException {
        try {
            return mapResource(
                new CreateTemplateCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(parentId),
                    delta,
                    new ResourceName(name)));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.TEMPLATE_CREATE);
        }

    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta,
                                  final String password) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), delta, password));
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void lock(final ID resourceId) throws CCCRemoteException {
        try {
            new LockResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_LOCK);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) throws CCCRemoteException {
        try {
            new MoveResourceCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(newParentId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_MOVE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void publish(final ID resourceId) throws CCCRemoteException {
        try {
            new PublishCommand(_audit).execute(
                new Date(),
                loggedInUser(),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_PUBLISH);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void publish(final ID resourceId,
                        final ID userId,
                        final Date date) throws CCCRemoteException {
        try {
            new PublishCommand(_audit).execute(
                date,
                _bdao.find(User.class, toUUID(userId)),
                _bdao.find(Resource.class, toUUID(resourceId)));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_PUBLISH);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void rename(final ID resourceId,
                       final String name) throws CCCRemoteException {
            try {
                new RenameResourceCommand(_bdao, _audit).rename(
                    loggedInUser(), new Date(), toUUID(resourceId), name);

            } catch (final RemoteExceptionSupport e) {
                throw newCCCRemoteException(e, ActionType.RESOURCE_RENAME);
            }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void unlock(final ID resourceId) throws CCCRemoteException {
        try {
            new UnlockResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_UNLOCK);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void unpublish(final ID resourceId) throws CCCRemoteException {
        try {
            new UnpublishResourceCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_UNPUBLISH);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateAlias(final ID aliasId,
                            final AliasDelta delta) throws CCCRemoteException {
        try {
            new UpdateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(delta.getTargetId()),
                toUUID(aliasId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.ALIAS_UPDATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit)
                                                     throws CCCRemoteException {
        try {
            new UpdatePageCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(pageId),
                delta,
                comment,
                isMajorEdit);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.PAGE_UPDATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateWorkingCopy(final ID pageId,
                                  final PageDelta delta)
                                                     throws CCCRemoteException {
        try {
            // FIXME: A delta and a working copy are the thing!

            final Page page = new Page(delta.getTitle());

            new PageHelper().assignParagraphs(page, delta);

            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(pageId),
                page.createSnapshot());

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_SET_WC);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void createWorkingCopy(final ID resourceId,
                                  final long index) throws CCCRemoteException {
        try {
            final UUID resourceUuid = toUUID(resourceId);
            final LogEntry le = _audit.findEntryForIndex(index);

            if (resourceUuid.equals(le.subjectId())) {
                new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(resourceId),
                    new Snapshot(le.detail()));
            } else {
                throw new CCCException("Log entry describes another resource.");
            }

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_SET_WC);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId)
                                                     throws CCCRemoteException {
        try {
            new ChangeTemplateForResourceCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(templateId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_CHANGE_TEMPLATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) throws CCCRemoteException {
        try {
            new ChangeResourceTagsCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), tags);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_UPDATE_TAGS);
        }

    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateTemplate(final ID templateId,
                               final TemplateDelta delta)
                                                     throws CCCRemoteException {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(templateId), delta);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.TEMPLATE_UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserDelta delta) {
        new UpdateUserCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(userId), delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name)
                                                     throws CCCRemoteException {
        try {
            final Folder f = new Folder(name);
            new CreateRootCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), f);
            return mapResource(f);
        } catch (final ResourceExistsException e) {
            throw newCCCRemoteException(e, ActionType.FOLDER_CREATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include)
                                                     throws CCCRemoteException {
        try {
            new IncludeInMainMenuCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), include);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_INCLUDE_IN_MM);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final List<ParagraphDelta> delta,
                                       final String definition) {
        final Set<Paragraph> pList = new HashSet<Paragraph>();

        for (final ParagraphDelta para : delta) {
            switch (para.getType()) {
                case TEXT:
                    pList.add(Paragraph.fromText(para.getName(),
                                                 para.getTextValue()));
                    break;

                case DATE:
                    pList.add(Paragraph.fromDate(para.getName(),
                                                 para.getDateValue()));
                    break;

                default:
                    throw new CCCException("Unexpected type");
            }
        }

        return new PageHelper().validateFields(pList, definition);
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateMetadata(final ID resourceId,
                               final Map<String, String> metadata)
                                                     throws CCCRemoteException {
        try {
            new UpdateResourceMetadataRolesCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), metadata);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_UPDATE_METADATA);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder)
                                                     throws CCCRemoteException {
        try {
            new UpdateFolderCommand(_bdao, _audit).execute(
                loggedInUser(),
                 new Date(),
                 toUUID(folderId),
                 ResourceOrder.valueOf(sortOrder));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.FOLDER_UPDATE_SORT_ORDER);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void clearWorkingCopy(final ID resourceId)
                                                     throws CCCRemoteException {
        try {
            new ClearWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_CLEAR_WC);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title)
                                                     throws CCCRemoteException {
        try {
            return mapResource(
                new CreateSearchCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), title)
            );

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.SEARCH_CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) {
        new CancelActionCommand(_bdao).execute(toUUID(actionId));
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final ID resourceId,
                             final ActionType action,
                             final Date executeAfter,
                             final String parameters,
                             final String comment,
                             final boolean isMajorEdit) {// TODO: Use ActionDelta.
      final Action a =
      new Action(
          action,
          executeAfter,
          loggedInUser(),
          _resources.find(Resource.class, toUUID(resourceId)),
          new Snapshot(parameters),
          comment,
          isMajorEdit);

      new ScheduleActionCommand(_bdao).execute(a);
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void reorder(final ID folderId,
                        final List<String> order) // FIXME: Should be List<ID>
                                                     throws CCCRemoteException {
        try {
            final List<UUID> newOrder = new ArrayList<UUID>();
            for (final String entry : order) {
                newOrder.add(UUID.fromString(entry));
            }
            new ReorderFolderContentsCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(folderId), newOrder);


        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.FOLDER_REORDER);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles)
                                                     throws CCCRemoteException {
        try {
            new UpdateResourceRolesCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), roles);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_CHANGE_ROLES);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void applyWorkingCopyToFile(final ID fileId)
                                                     throws CCCRemoteException {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(fileId), null, false);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.FILE_UPDATE);
        }
    }

    /** {@inheritDoc}
     * @throws CCCRemoteException */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration)
                                                     throws CCCRemoteException {
        try {
            new UpdateCachingCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(resourceId), duration);

        } catch (final RemoteExceptionSupport e) {
            throw newCCCRemoteException(e, ActionType.RESOURCE_UPDATE_CACHE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId, final String password) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(userId), password);
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(_bdao);
        _resources = new ResourceDaoImpl(_bdao);
        _userLookup = new UserLookup(_bdao);
    }

    private User loggedInUser() {
        return _userLookup.loggedInUser(_context.getCallerPrincipal());
    }

    private CCCRemoteException newCCCRemoteException(
                                        final RemoteExceptionSupport e,
                                        final ActionType action) {
        _context.setRollbackOnly();  // CRITICAL
        LOG.info("Handled local exception: "+e.getUUID(), e);
        return e.toRemoteException(action);
    }
}
