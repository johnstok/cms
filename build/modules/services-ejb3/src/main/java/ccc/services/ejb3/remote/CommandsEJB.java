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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.jboss.annotation.security.SecurityDomain;

import ccc.actions.Action;
import ccc.commons.EmailAddress;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
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
import ccc.services.AliasDao;
import ccc.services.AuditLog;
import ccc.services.FolderDao;
import ccc.services.PageDao;
import ccc.services.ResourceDao;
import ccc.services.TemplateDao;
import ccc.services.UserManager;
import ccc.services.api.AliasDelta;
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;
import ccc.services.ejb3.local.ActionDao;
import ccc.services.support.ModelTranslation;


/**
 * EJB implementation of the {@link Commands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Commands.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Commands.class)
@RolesAllowed({"ADMINISTRATOR"})
@SecurityDomain("java:/jaas/ccc")
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
    @EJB(name=ResourceDao.NAME)    private ResourceDao     _resources;
    @EJB(name=ActionDao.NAME)      private ActionDao       _scheduler;
    @EJB(name=AuditLog.NAME)       private AuditLog        _audit;

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
        final Folder f = new Folder(name);
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
        final User user = new User(delta._username);
        user.email(new EmailAddress(delta._email));
        for (final String role : delta._roles) {
            user.addRole(CreatorRoles.valueOf(role));
        }
        _users.createUser(user, delta._password);
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
            UUID.fromString(delta._targetId),
            UUID.fromString(delta._id));
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

        _page.updateWorkingCopy(UUID.fromString(delta._id),
                     delta._title,
                     page.paragraphs());

    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final String resourceId, final long index) {
        final UUID pageUuid = UUID.fromString(resourceId);
        final LogEntry le = _audit.findEntryForIndex(index);

        if (pageUuid.equals(le.subjectId())) {
            _page.updateWorkingCopy(
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
        final User user = new User(delta._username);
        user.email(new EmailAddress(delta._email));
        for (final String role : delta._roles) {
            user.addRole(CreatorRoles.valueOf(role));
        }
        user.id(UUID.fromString(delta._id));

        _users.updateUser(user, delta._password);

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
            if (("TEXT".equals(para._type) || "HTML".equals(para._type))
                    && para._textValue != null) {
                final Paragraph paragraph =
                    Paragraph.fromText(para._name, para._textValue);
                pList.add(paragraph);
            } else if ("DATE".equals(para._type) && para._dateValue != null) {
                final Paragraph paragraph =
                    Paragraph.fromDate(para._name, para._dateValue);
                pList.add(paragraph);
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
            if (("TEXT".equals(para._type) || "HTML".equals(para._type))
                    && para._textValue != null) {
                final Paragraph paragraph =
                    Paragraph.fromText(para._name, para._textValue);
                page.addParagraph(paragraph);
            } else if ("DATE".equals(para._type) && para._dateValue != null) {
                final Paragraph paragraph =
                    Paragraph.fromDate(para._name, para._dateValue);
                page.addParagraph(paragraph);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final String pageId) {
        _page.clearWorkingCopy(UUID.fromString(pageId));
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
    public void publish(final String resourceId, final Date publishDate) {
        final Action a =
            new Action(
                Action.Type.PUBLISH,
                publishDate,
                _users.loggedInUser(),
                _resources.find(Resource.class, UUID.fromString(resourceId)),
                new Snapshot());
        _scheduler.schedule(a);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final String actionId) {
        _scheduler.cancel(UUID.fromString(actionId));
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

}


