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
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.actions.Action;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourceOrder;
import ccc.domain.Search;
import ccc.domain.Snapshot;
import ccc.domain.Template;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionDao;
import ccc.services.AliasDao;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;
import ccc.services.FolderDao;
import ccc.services.ModelTranslation;
import ccc.services.PageDao;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.TemplateDao;
import ccc.services.UserManager;
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

    @EJB(name=TemplateDao.NAME)    private TemplateDao     _templates;
    @EJB(name=FolderDao.NAME)      private FolderDao       _folders;
    @EJB(name=AliasDao.NAME)       private AliasDao        _alias;
    @EJB(name=PageDao.NAME)        private PageDao         _page;
    @EJB(name=UserManager.NAME)    private UserManager     _users;
    @EJB(name=ActionDao.NAME)      private ActionDao       _scheduler;

    @PersistenceContext private EntityManager _em;
    private ResourceDao        _resources;
    private AuditLog           _audit;
    private WorkingCopyManager _wcMgr;

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId,
                            final String name,
                            final ID targetId) {

        final Resource target =
            _resources.find(Resource.class, toUUID(targetId));
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }

        final Resource parent =
            _resources.find(Resource.class, toUUID(parentId));
        if (parent == null) {
            throw new CCCException("Parent does not exists.");
        }

        final Alias a = new Alias(name, target);
        _resources.create(parent.id(), a);

        return mapResource(a);
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
        final Folder f = new Folder(name);
        f.title((null==title)?name:title);
        _resources.create(toUUID(parentId), f);
        return mapResource(f);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId) {

        final Page page = new Page(
            ResourceName.escape(name),
            delta.getTitle());

        if (publish) {
            page.publish(_users.loggedInUser());
        }

        if (templateId != null) {
            final Template template =
                _resources.find(Template.class, toUUID(templateId));
            page.template(template);
        }

        assignParagraphs(delta.getParagraphs(), page);

        _page.create(toUUID(parentId), page);

        return mapResource(page);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name) {

        final Template t = new Template(
            new ResourceName(name),
            delta.getTitle(),
            delta.getDescription(),
            delta.getBody(),
            delta.getDefinition());

        _resources.create(toUUID(parentId), t);

        return mapResource(t);

    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta, final String password) {
        return mapUser(_users.createUser(delta, password));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary lock(final ID resourceId) {
        return mapResource(_resources.lock(toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) {

        _resources.move(toUUID(resourceId), toUUID(newParentId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final ID resourceId) {
        return mapResource(_resources.publish(toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final ID resourceId,
                                   final ID userId,
                                   final Date date) {
        return mapResource(_resources.publish(
            toUUID(resourceId),
            toUUID(userId),
            date));
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId,
                       final String name) {

        _resources.rename(toUUID(resourceId), name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final ID resourceId) {
        return mapResource(_resources.unlock(toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final ID resourceId) {
        return mapResource(_resources.unpublish(toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta) {
        _alias.updateAlias(
            toUUID(delta.getTargetId()),
            toUUID(aliasId));
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) {

        final Page page = new Page(delta.getTitle());

        assignParagraphs(delta.getParagraphs(), page);

        _page.update(toUUID(pageId),
                     delta.getTitle(),
                     page.paragraphs(), comment, isMajorEdit);

    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta) {

        final Page page = new Page(delta.getTitle());

        assignParagraphs(delta.getParagraphs(), page);

        _wcMgr.updateWorkingCopy(
            toUUID(pageId), page.createSnapshot());
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index) {
        final UUID resourceUuid = toUUID(resourceId);
        final LogEntry le = _audit.findEntryForIndex(index);

        if (resourceUuid.equals(le.subjectId())) {
            _wcMgr.updateWorkingCopy(
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

        final Template t = (null==templateId)
            ? null
            : _resources.find(Template.class, toUUID(templateId));

        _resources.updateTemplateForResource(
            toUUID(resourceId), t);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) {
        _resources.updateTags(toUUID(resourceId), tags);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final ID templateId,
                                          final TemplateDelta delta) {

        final Template t = _templates.update(toUUID(templateId),
                                             delta.getTitle(),
                                             delta.getDescription(),
                                             delta.getDefinition(),
                                             delta.getBody());

        return mapResource(t);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary updateUser(final ID userId, final UserDelta delta) {
        return mapUser(_users.updateUser(toUUID(userId), delta));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        final Folder f = new Folder(name);
        _resources.createRoot(f);
        return mapResource(f);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include) {
        _resources.includeInMainMenu(toUUID(resourceId),
                                     include);
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

        return _page.validateFields(pList, definition);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final ID resourceId,
                               final Map<String, String> metadata) {
        _resources.updateMetadata(toUUID(resourceId), metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder) {
        _folders.updateSortOrder(toUUID(folderId),
                                 ResourceOrder.valueOf(sortOrder));
    }


    private void assignParagraphs(final List<ParagraphDelta> paragraphs,
                                  final Page page) {

        for (final ParagraphDelta para : paragraphs) {
            switch (para.getType()) {
                case TEXT:
                    page.addParagraph(
                        Paragraph.fromText(para.getName(),
                                           para.getTextValue()));
                    break;

                case DATE:
                    page.addParagraph(
                        Paragraph.fromDate(para.getName(),
                                           para.getDateValue()));
                    break;

                default:
                    throw new CCCException("Unexpected type");
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) {
        _wcMgr.clearWorkingCopy(toUUID(pageId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title) {
        final Search s = new Search(title);
        _resources.create(toUUID(parentId), s);
        return mapResource(s);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) {
        _scheduler.cancel(toUUID(actionId));
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final ID resourceId,
                             final ActionType action,
                             final Date executeAfter,
                             final String parameters) {
      final Action a =
      new Action(
          action,
          executeAfter,
          _users.loggedInUser(),
          _resources.find(Resource.class, toUUID(resourceId)),
          new Snapshot(parameters));
      _scheduler.schedule(a);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final ID folderId, final List<String> order) { // FIXME: Should be List<ID>
        final List<UUID> newOrder = new ArrayList<UUID>();
        for (final String entry : order) {
            newOrder.add(UUID.fromString(entry));
        }
        _folders.reorder(toUUID(folderId), newOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles) {
        _resources.changeRoles(toUUID(resourceId), roles);
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(bdao);
        _resources = new ResourceDaoImpl(_users, _audit, bdao);
        _wcMgr = new WorkingCopyManager(_resources);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopyToFile(final ID fileId) {
        _wcMgr.applyWorkingCopy(toUUID(fileId), _users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration) {
        _resources.updateCache(toUUID(resourceId), duration);
    }

    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId, final String password) {
        _users.updatePassword(toUUID(userId),password);
    }
}
