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

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import ccc.services.ContentManager;
import ccc.services.QueryManager;
import ccc.services.ResourceDAOLocal;
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
public class CommandsEJB
    extends
        ModelTranslation
    implements
        Commands {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _entityManager;

    @EJB(name="QueryManager")
    private QueryManager _qm;
    @EJB(name="UserManager")
    private UserManager _users;
    @EJB(name="ContentManager")
    private ContentManager _content;
    @EJB(name="ResourceDAO", beanInterface=ResourceDAOLocal.class)
    private ResourceDAOLocal _resources;

    /** {@inheritDoc} */
    @Override
    public void createAlias(final String parentId,
                            final String name,
                            final String targetId) {

        final Resource target = _qm.find(Resource.class, targetId);
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }

        final Resource parent = _qm.find(Resource.class, parentId);
        if (parent == null) {
            throw new CCCException("Parent does not exists.");
        }

        _content.create(parent.id(),
            new Alias(new ResourceName(name), target));

    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {

        final Folder f =
            _content.create(
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
            final Template template =  _qm.find(Template.class, templateId);
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

        _content.create(UUID.fromString(parentId), page);

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

        _content.createDisplayTemplate(UUID.fromString(parentId), t);

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

        _content.move(UUID.fromString(resourceId),
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

        _content.rename(UUID.fromString(resourceId), name);
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
        _content.updateAlias(
            UUID.fromString(delta._targetId),
            UUID.fromString(delta._id));
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

        // TODO: Add version checking.
        _content.update(page.id(), page.title(), page.paragraphs());

    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final long version,
                                       final String templateId) {

        final Template t = (null==templateId)
            ? null
            : _qm.find(Template.class, templateId);

        _content.updateTemplateForResource(UUID.fromString(resourceId), t);
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
        final Template t = new Template(
            new ResourceName(delta._name),
            delta._title,
            delta._description,
            delta._body,
            delta._definition);
        t.version(delta._version);
        t.id(UUID.fromString(delta._id));

        _content.update(t);

        return map(t); // FIXME: Should be returned by _assets.update
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
        _entityManager.persist(f);
        return map(f);
    }
}
