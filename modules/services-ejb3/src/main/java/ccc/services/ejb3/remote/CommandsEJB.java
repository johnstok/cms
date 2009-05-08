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
import ccc.commons.EmailAddress;
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
import ccc.domain.User;
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
    public ResourceSummary createAlias(final String parentId,
                            final String name,
                            final String targetId) {

        final Resource target =
            _resources.find(Resource.class, UUID.fromString(targetId));
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }

        final Resource parent =
            _resources.find(Resource.class, UUID.fromString(parentId));
        if (parent == null) {
            throw new CCCException("Parent does not exists.");
        }

        final Alias a = new Alias(name, target);
        _resources.create(parent.id(), a);

        return map(a);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {

        return createFolder(parentId, name, null);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name,
                                        final String title) {
        final Folder f = new Folder(name);
        f.title((null==title)?name:title);
        _resources.create(UUID.fromString(parentId), f);
        return map(f);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);

        if (delta._published) {
            page.publish(_users.loggedInUser());
        }

        if (templateId != null) {
            final Template template =
                _resources.find(Template.class, UUID.fromString(templateId));
            page.template(template);
        }

        assignParagraphs(delta._paragraphs, page);

        _page.create(UUID.fromString(parentId), page);

        return map(page);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final String parentId,
                               final TemplateDelta delta) {

        final Template t = new Template(
            new ResourceName(delta._name),
            delta._title,
            delta._description,
            delta._body,
            delta._definition);

        _resources.create(UUID.fromString(parentId), t);

        return map(t);

    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta) {
        final User user = new User(delta.getUsername());
        user.email(new EmailAddress(delta.getEmail()));
        for (final String role : delta.getRoles()) {
            user.addRole(role);
        }
        _users.createUser(user, delta.getPassword());
        return map(user);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary lock(final String resourceId) {
        return map(_resources.lock(UUID.fromString(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public void move(final String resourceId,
                     final String newParentId) {

        _resources.move(UUID.fromString(resourceId),
                        UUID.fromString(newParentId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId) {
        return map(_resources.publish(UUID.fromString(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId,
                                   final String userId,
                                   final Date date) {
        return map(_resources.publish(
            UUID.fromString(resourceId),
            UUID.fromString(userId),
            date));
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String resourceId,
                       final String name) {

        _resources.rename(UUID.fromString(resourceId), name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final String resourceId) {
        return map(_resources.unlock(UUID.fromString(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final String resourceId) {
        return map(_resources.unpublish(UUID.fromString(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final AliasDelta delta) {
        _alias.updateAlias(
            toUUID(delta.getTargetId()),
            toUUID(delta.getId()));
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);
        page.id(UUID.fromString(delta._id));

        assignParagraphs(delta._paragraphs, page);

        _page.update(UUID.fromString(delta._id),
                     delta._title,
                     page.paragraphs(), comment, isMajorEdit);

    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final PageDelta delta) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);
        page.id(UUID.fromString(delta._id));

        assignParagraphs(delta._paragraphs, page);

        _wcMgr.updateWorkingCopy(
            UUID.fromString(delta._id), page.createSnapshot());
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final String resourceId, final long index) {
        final UUID resourceUuid = UUID.fromString(resourceId);
        final LogEntry le = _audit.findEntryForIndex(index);

        if (resourceUuid.equals(le.subjectId())) {
            _wcMgr.updateWorkingCopy(
                UUID.fromString(resourceId),
                new Snapshot(le.detail()));
        } else {
            throw new CCCException("Log entry describes another resource.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final String templateId) {

        final Template t = (null==templateId)
            ? null
            : _resources.find(Template.class, UUID.fromString(templateId));

        _resources.updateTemplateForResource(
            UUID.fromString(resourceId), t);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId,
                           final String tags) {
        _resources.updateTags(UUID.fromString(resourceId), tags);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final TemplateDelta delta) {

        final Template t = _templates.update(UUID.fromString(delta._id),
                                             delta._title,
                                             delta._description,
                                             delta._definition,
                                             delta._body);

        return map(t);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary updateUser(final UserDelta delta) {
        // FIXME: Refactor - horrible.
        final User user = new User(delta.getUsername());
        user.email(new EmailAddress(delta.getEmail()));
        for (final String role : delta.getRoles()) {
            user.addRole(role);
        }
        user.id(toUUID(delta.getId()));

        _users.updateUser(user, delta.getPassword());

        return map(user);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        final Folder f = new Folder(name);
        _resources.createRoot(f);
        return map(f);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final String resourceId,
                                  final boolean include) {
        _resources.includeInMainMenu(UUID.fromString(resourceId),
                                     include);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final List<ParagraphDelta> delta,
                                       final String definition) {
        final Set<Paragraph> pList = new HashSet<Paragraph>();

        for (final ParagraphDelta para : delta) {
            switch (para._type) {
                case TEXT:
                    pList.add(Paragraph.fromText(para._name, para._textValue));
                    break;

                case DATE:
                    pList.add(Paragraph.fromDate(para._name, para._dateValue));
                    break;

                default:
                    throw new CCCException("Unexpected type");
            }
        }

        return _page.validateFields(pList, definition);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final String resourceId,
                               final Map<String, String> metadata) {
        _resources.updateMetadata(UUID.fromString(resourceId), metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final String folderId,
                                      final String sortOrder) {
        _folders.updateSortOrder(UUID.fromString(folderId),
                                 ResourceOrder.valueOf(sortOrder));
    }


    private void assignParagraphs(final List<ParagraphDelta> paragraphs,
                                  final Page page) {

        for (final ParagraphDelta para : paragraphs) {
            switch (para._type) {
                case TEXT:
                    page.addParagraph(
                        Paragraph.fromText(para._name, para._textValue));
                    break;

                case DATE:
                    page.addParagraph(
                        Paragraph.fromDate(para._name, para._dateValue));
                    break;

                default:
                    throw new CCCException("Unexpected type");
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final String pageId) {
        _wcMgr.clearWorkingCopy(UUID.fromString(pageId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final String parentId,
                                        final String title) {
        final Search s = new Search(title);
        _resources.create(UUID.fromString(parentId), s);
        return map(s);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final String actionId) {
        _scheduler.cancel(UUID.fromString(actionId));
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final String resourceId,
                             final String action,
                             final Date executeAfter,
                             final String parameters) {
      final Action a =
      new Action(
          ccc.services.api.Action.valueOf(action),
          executeAfter,
          _users.loggedInUser(),
          _resources.find(Resource.class, UUID.fromString(resourceId)),
          new Snapshot(parameters));
      _scheduler.schedule(a);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final String folderId, final List<String> order) {
        final List<UUID> newOrder = new ArrayList<UUID>();
        for (final String entry : order) {
            newOrder.add(UUID.fromString(entry));
        }
        _folders.reorder(UUID.fromString(folderId), newOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final String resourceId,
                            final Collection<String> roles) {
        _resources.changeRoles(UUID.fromString(resourceId), roles);
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
    public void applyWorkingCopyToFile(final String fileId) {
        _wcMgr.applyWorkingCopy(UUID.fromString(fileId), _users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final String resourceId,
                                    final Duration duration) {
        _resources.updateCache(UUID.fromString(resourceId), duration);
    }

    private UUID toUUID(final ID id) {
        return UUID.fromString(id.toString());
    }
}


