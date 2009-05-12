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

import ccc.actions.Action;
import ccc.actions.ApplyWorkingCopyCommand;
import ccc.actions.CancelActionCommand;
import ccc.actions.CreateAliasCommand;
import ccc.actions.CreateFolderCommand;
import ccc.actions.CreatePageCommand;
import ccc.actions.CreateSearchCommand;
import ccc.actions.CreateTemplateCommand;
import ccc.actions.CreateUserCommand;
import ccc.actions.RenameResourceCommand;
import ccc.actions.ReorderFolderContentsCommand;
import ccc.actions.ScheduleActionCommand;
import ccc.actions.UpdateAliasCommand;
import ccc.actions.UpdateFolderCommand;
import ccc.actions.UpdatePageCommand;
import ccc.actions.UpdatePasswordAction;
import ccc.actions.UpdateTemplateCommand;
import ccc.actions.UpdateUserCommand;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.ResourceOrder;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.persistence.jpa.BaseDao;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.ModelTranslation;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.UserLookup;
import ccc.services.WorkingCopyManager;
import ccc.services.api.ActionType;
import ccc.services.api.AliasDelta;
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

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private ResourceDao        _resources;
    private AuditLog           _audit;
    private WorkingCopyManager _wcMgr;
    private UserLookup         _userLookup;
    private BaseDao            _bdao;

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId,
                            final String name,
                            final ID targetId) {
        try {
            return mapResource(
                new CreateAliasCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(parentId),
                    toUUID(targetId),
                    name));

        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId, final String name) {
        return createFolder(parentId, name, null);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title) {
        try {
            return mapResource(
                new CreateFolderCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), name, title));

        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId) {
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

        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name) {
        try {
            return mapResource(
                new CreateTemplateCommand(_bdao, _audit).execute(
                    loggedInUser(),
                    new Date(),
                    toUUID(parentId),
                    delta,
                    new ResourceName(name)));

        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }

    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta,
                                  final String password) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(delta, password));
    }

    /** {@inheritDoc} */
    @Override
    public void lock(final ID resourceId) {
        try {
            _resources.lock(loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) {
        try {
            _resources.move(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(newParentId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.MOVE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.MOVE);
        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.MOVE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId) {
        try {
            _resources.publish(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.PUBLISH);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.PUBLISH);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId, final ID userId, final Date date) {
        try {
            _resources.publish(toUUID(resourceId), toUUID(userId), date);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.PUBLISH);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.PUBLISH);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId,
                       final String name) {
            try {
                new RenameResourceCommand(_bdao, _audit).rename(
                    loggedInUser(), new Date(), toUUID(resourceId), name);

            } catch (final UnlockedException e) {
                throw e.toRemoteException(ActionType.RENAME);
            } catch (final LockMismatchException e) {
                throw e.toRemoteException(ActionType.RENAME);
            } catch (final ResourceExistsException e) {
                throw e.toRemoteException(ActionType.RENAME);
            }
    }

    /** {@inheritDoc} */
    @Override
    public void unlock(final ID resourceId) {
        try {
            _resources.unlock(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final InsufficientPrivilegesException e) {
            throw e.toRemoteException(ActionType.UNLOCK);
        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UNLOCK);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId) {
        try {
            _resources.unpublish(
                loggedInUser(), new Date(), toUUID(resourceId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UNPUBLISH);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UNPUBLISH);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta) {
        try {
            new UpdateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(delta.getTargetId()),
                toUUID(aliasId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) {
        try {
            new UpdatePageCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(pageId),
                delta,
                comment,
                isMajorEdit);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta) {
        try {
            // FIXME: A delta and a working copy are the thing!

            final Page page = new Page(delta.getTitle());

            new PageHelper().assignParagraphs(page, delta);

            _wcMgr.updateWorkingCopy(
                loggedInUser(), toUUID(pageId), page.createSnapshot());

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index) {
        try {
            final UUID resourceUuid = toUUID(resourceId);
            final LogEntry le = _audit.findEntryForIndex(index);

            if (resourceUuid.equals(le.subjectId())) {
                _wcMgr.updateWorkingCopy(
                    loggedInUser(),
                    toUUID(resourceId),
                    new Snapshot(le.detail()));
            } else {
                throw new CCCException("Log entry describes another resource.");
            }

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.CREATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.CREATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId) {
        try {
            _resources.updateTemplateForResource(
                loggedInUser(),
                new Date(),
                toUUID(resourceId),
                toUUID(templateId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.CHANGE_TEMPLATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.CHANGE_TEMPLATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) {
        try {
            _resources.updateTags(
                loggedInUser(), new Date(), toUUID(resourceId), tags);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE_TAGS);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE_TAGS);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final ID templateId,
                                          final TemplateDelta delta) {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(templateId), delta);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserDelta delta) {
        new UpdateUserCommand(_bdao, _audit).execute(toUUID(userId), delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        final Folder f = new Folder(name);
        _resources.createRoot(loggedInUser(), f);
        return mapResource(f);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include) {
        try {
            _resources.includeInMainMenu(
                loggedInUser(), new Date(), toUUID(resourceId), include);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.INCLUDE_IN_MM);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.INCLUDE_IN_MM);
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

    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final ID resourceId,
                               final Map<String, String> metadata) {
        try {
            _resources.updateMetadata(
                loggedInUser(), new Date(), toUUID(resourceId), metadata);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE_METADATA);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE_METADATA);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder) {
        try {
            new UpdateFolderCommand(_bdao, _audit).execute(
                loggedInUser(),
                 new Date(),
                 toUUID(folderId),
                 ResourceOrder.valueOf(sortOrder));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE_SORT_ORDER);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE_SORT_ORDER);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) {
        try {
            _wcMgr.clearWorkingCopy(loggedInUser(), toUUID(pageId));

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.CLEAR_WC);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.CLEAR_WC);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title) {
        try {
            return mapResource(
                new CreateSearchCommand(_bdao, _audit).execute(
                    loggedInUser(), new Date(), toUUID(parentId), title)
            );

        } catch (final ResourceExistsException e) {
            throw e.toRemoteException(ActionType.CREATE);
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
                             final String parameters) { // TODO: Use ActionDelta
      final Action a =
      new Action(
          action,
          executeAfter,
          loggedInUser(),
          _resources.find(Resource.class, toUUID(resourceId)),
          new Snapshot(parameters));

      new ScheduleActionCommand(_bdao).execute(a);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final ID folderId, final List<String> order) { // FIXME: Should be List<ID>
        try {
            final List<UUID> newOrder = new ArrayList<UUID>();
            for (final String entry : order) {
                newOrder.add(UUID.fromString(entry));
            }
            new ReorderFolderContentsCommand(_resources, _audit).execute(
                loggedInUser(), new Date(), toUUID(folderId), newOrder);


        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.REORDER);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.REORDER);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles) {
        try {
            _resources.changeRoles(
                loggedInUser(), new Date(), toUUID(resourceId), roles);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.CHANGE_ROLES);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.CHANGE_ROLES);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopyToFile(final ID fileId) {
        try {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(fileId), null, false);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration) {
        try {
            _resources.updateCache(
                loggedInUser(), new Date(), toUUID(resourceId), duration);

        } catch (final UnlockedException e) {
            throw e.toRemoteException(ActionType.UPDATE_CACHE);
        } catch (final LockMismatchException e) {
            throw e.toRemoteException(ActionType.UPDATE_CACHE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId, final String password) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            toUUID(userId), password);
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(_bdao);
        _resources = new ResourceDaoImpl(_audit, _bdao);
        _wcMgr = new WorkingCopyManager(_resources);
        _userLookup = new UserLookup(_bdao);
    }

    private User loggedInUser() {
        return _userLookup.loggedInUser(_context.getCallerPrincipal());
    }
}
