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
import ccc.services.AssetManagerLocal;
import ccc.services.ContentManagerLocal;
import ccc.services.QueryManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.UserManagerLocal;
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;


/**
 * TODO: Add Description for this type.
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

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;
    @EJB(name="UserManager", beanInterface=UserManagerLocal.class)
    private UserManagerLocal _users;
    @EJB(name="AssetManager", beanInterface=AssetManagerLocal.class)
    private AssetManagerLocal _assets;
    @EJB(name="ContentManager", beanInterface=ContentManagerLocal.class)
    private ContentManagerLocal _content;
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
                new Folder(new ResourceName(name)));
        return map(f);

    }

    /** {@inheritDoc} */
    @Override
    public void createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);

        if (templateId != null) {
            final Template template =  _qm.find(Template.class, templateId);
            page.displayTemplateName(template);
        }

        for (final String[] para : delta._paragraphs) { // FIXME: Wrong!
            final Paragraph paragraph = Paragraph.fromText(para[1]);
            page.addParagraph(para[0], paragraph);
        }

        _content.create(UUID.fromString(parentId), page);

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

        _assets.createDisplayTemplate(t);

        return map(t);

    }

    /** {@inheritDoc} */
    @Override
    public void createUser(final UserDelta delta) {
        final User user = new User(delta._username);
        user.email(delta._email);
        for (final String role : delta._roles) {
            user.addRole(CreatorRoles.valueOf(role));
        }
        _users.createUser(user, delta._password);
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
    public void updateAlias(final String aliasId,
                            final long version,
                            final String targetId) {

        _content.updateAlias(
            UUID.fromString(targetId),
            UUID.fromString(aliasId));
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final String pageId,
                           final long version,
                           final PageDelta delta) { // FIXME: WRONG!!!

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);
        page.id(UUID.fromString(pageId));
        page.version(version);

        for (final String[] para : delta._paragraphs) { // FIXME: Wrong!
            final Paragraph paragraph = Paragraph.fromText(para[1]);
            page.addParagraph(para[0], paragraph);
        }

        _content.update(UUID.fromString(pageId), delta._title, null);

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

        _assets.update(t);

        return map(t); // FIXME: Should be returned by _assets.update
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final String userId,
                           final long version,
                           final UserDelta delta) {
        final User user = new User(delta._username);
        user.email(delta._email);
        for (final String role : delta._roles) {
            user.addRole(CreatorRoles.valueOf(role));
        }
        user.version(version);
        user.id(UUID.fromString(userId));

        _users.updateUser(user, delta._password);
    }
}
