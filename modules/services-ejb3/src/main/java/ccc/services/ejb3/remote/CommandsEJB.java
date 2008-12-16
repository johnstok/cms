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

import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commons.EmailAddress;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AliasDao;
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


/**
 * TODO: Add Description for this type.
 * TODO: Version checking for all methods.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PublicCommands")
@TransactionAttribute(REQUIRED)
@Remote(Commands.class)
@RolesAllowed({"ADMINISTRATOR"})
public class CommandsEJB
    extends
        ModelTranslation
    implements
        Commands {

    @EJB(name="TemplateDao")    private TemplateDao     _templates;
    @EJB(name="FolderDao")      private FolderDao       _folders;
    @EJB(name="AliasDao")       private AliasDao        _alias;
    @EJB(name="PageDao")        private PageDao         _page;
    @EJB(name="UserManager")    private UserManager     _users;
    @EJB(name="ResourceDao")    private ResourceDao     _resources;

    /** {@inheritDoc} */
    @Override
    public void createAlias(final String parentId,
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

        _alias.create(parent.id(),
            new Alias(name, target));

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {

        final Folder f =
            _folders.create(
                UUID.fromString(parentId),
                new Folder(name));
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

        for (final ParagraphDelta para : delta._paragraphs) {
            if ("TEXT".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromText(para._name, para._textValue);
                page.addParagraph(paragraph);
            } else if ("DATE".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromDate(para._name, para._dateValue);
                page.addParagraph(paragraph);

            }
        }

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

        _templates.create(UUID.fromString(parentId), t);

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
        return map(_resources.lock(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void move(final String resourceId,
                     final long version,
                     final String newParentId) {

        _resources.move(UUID.fromString(resourceId),
            UUID.fromString(newParentId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId) {
        return map(_resources.publish(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String resourceId,
                       final long version,
                       final String name) {

        _resources.rename(UUID.fromString(resourceId), name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final String resourceId) {
        return map(_resources.unlock(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final String resourceId) {
        return map(_resources.unpublish(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final AliasDelta delta) {
        _alias.updateAlias(
            UUID.fromString(delta._targetId),
            UUID.fromString(delta._id),
            delta._version);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final PageDelta delta) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);
        page.id(UUID.fromString(delta._id));
        page.version(delta._version);

        // TODO: Remove duplication
        for (final ParagraphDelta para : delta._paragraphs) {
            if ("TEXT".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromText(para._name, para._textValue);
                page.addParagraph(paragraph);
            } else if ("DATE".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromDate(para._name, para._dateValue);
                page.addParagraph(paragraph);

            }
        }

        _page.update(UUID.fromString(delta._id),
                     delta._version,
                     delta._title,
                     page.paragraphs());

    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final long version,
                                       final String templateId) {

        final Template t = (null==templateId)
            ? null
            : _resources.find(Template.class, UUID.fromString(templateId));

        _resources.updateTemplateForResource(UUID.fromString(resourceId), t);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId,
                           final long version,
                           final String tags) {
        _resources.updateTags(resourceId, tags);

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final TemplateDelta delta) {

        final Template t = _templates.update(UUID.fromString(delta._id),
                                             delta._version,
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
        user.version(delta._version);
        user.id(UUID.fromString(delta._id));

        _users.updateUser(user, delta._password);

        return map(user);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        final Folder f = new Folder(name);
        _folders.createRoot(f);
        return map(f);
    }
}
