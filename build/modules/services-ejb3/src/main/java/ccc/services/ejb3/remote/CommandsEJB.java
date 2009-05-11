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
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
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
        return mapResource(
            new CreateAliasCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(parentId),
                toUUID(targetId),
                name));
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
        return mapResource(
            new CreateFolderCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(parentId), name, title));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId) {
        final Page p = new CreatePageCommand(_bdao, _audit).execute(
            loggedInUser(),
            new Date(),
            toUUID(parentId),
            delta,
            publish,
            ResourceName.escape(name),
            toUUID(templateId));

        return mapResource(p);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name) {
        return mapResource(
            new CreateTemplateCommand(_bdao, _audit).execute(
                loggedInUser(),
                new Date(),
                toUUID(parentId),
                delta,
                new ResourceName(name)));

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
        _resources.lock(loggedInUser(), new Date(), toUUID(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) {
        _resources.move(
            loggedInUser(),
            new Date(),
            toUUID(resourceId),
            toUUID(newParentId));
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId) {
        _resources.publish(
            loggedInUser(), new Date(), toUUID(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId, final ID userId, final Date date) {
        _resources.publish(toUUID(resourceId), toUUID(userId), date);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId,
                       final String name) {

        _resources.rename(
            loggedInUser(), new Date(), toUUID(resourceId), name);
    }

    /** {@inheritDoc} */
    @Override
    public void unlock(final ID resourceId) {
        _resources.unlock(
            loggedInUser(), new Date(), toUUID(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId) {
        _resources.unpublish(
            loggedInUser(), new Date(), toUUID(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta) {
        new UpdateAliasCommand(_bdao, _audit).execute(
            loggedInUser(),
            new Date(),
            toUUID(delta.getTargetId()),
            toUUID(aliasId));
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) {
        new UpdatePageCommand(_bdao, _audit).execute(
            loggedInUser(),
            new Date(),
            toUUID(pageId),
            delta,
            comment,
            isMajorEdit);
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta) {
        // FIXME: A delta and a working copy are the thing!

        final Page page = new Page(delta.getTitle());

        new PageHelper().assignParagraphs(page, delta);

        _wcMgr.updateWorkingCopy(
            loggedInUser(), toUUID(pageId), page.createSnapshot());
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index) {
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
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId) {
        _resources.updateTemplateForResource(
            loggedInUser(), new Date(), toUUID(resourceId), toUUID(templateId));
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) {
        _resources.updateTags(
            loggedInUser(), new Date(), toUUID(resourceId), tags);

    }

    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final ID templateId,
                                          final TemplateDelta delta) {
        new UpdateTemplateCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(templateId), delta);
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
        _resources.includeInMainMenu(
            loggedInUser(), new Date(), toUUID(resourceId), include);
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
        _resources.updateMetadata(
            loggedInUser(), new Date(), toUUID(resourceId), metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder) {
        new UpdateFolderCommand(_bdao, _audit).execute(
            loggedInUser(),
             new Date(),
             toUUID(folderId),
             ResourceOrder.valueOf(sortOrder));
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) {
        _wcMgr.clearWorkingCopy(loggedInUser(), toUUID(pageId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title) {
        return mapResource(
            new CreateSearchCommand(_bdao, _audit).execute(
                loggedInUser(), new Date(), toUUID(parentId), title)
        );
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
        final List<UUID> newOrder = new ArrayList<UUID>();
        for (final String entry : order) {
            newOrder.add(UUID.fromString(entry));
        }

        new ReorderFolderContentsCommand(_resources, _audit).execute(
            loggedInUser(), new Date(), toUUID(folderId), newOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles) {
        _resources.changeRoles(
            loggedInUser(), new Date(), toUUID(resourceId), roles);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopyToFile(final ID fileId) {
        new ApplyWorkingCopyCommand(_bdao, _audit).execute(
            loggedInUser(), new Date(), toUUID(fileId), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration) {
        _resources.updateCache(
            loggedInUser(), new Date(), toUUID(resourceId), duration);
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
