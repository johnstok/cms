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

import java.io.StringReader;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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

        _resources.create(parent.id(),
            new Alias(name, target));

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
            validateFields(delta, template);
        }

        for (final ParagraphDelta para : delta._paragraphs) {
            if ("TEXT".equals(para._type) || "HTML".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromText(para._name, para._textValue);
                page.addParagraph(paragraph);
            } else if ("DATE".equals(para._type)) {
                final Paragraph paragraph =
                    Paragraph.fromDate(para._name, para._dateValue);
                page.addParagraph(paragraph);
            }
        }

        _resources.create(UUID.fromString(parentId), page);

        return map(page);
    }

    private void validateFields(final PageDelta delta, final Template t) {

        Document document;
        String errors = "";

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource s =
                new InputSource(new StringReader(t.definition()));
            document = builder.parse(s);
            final NodeList nl = document.getElementsByTagName("field");
            for (int n = 0;  n < nl.getLength(); n++) {
                final NamedNodeMap nnm = nl.item(n).getAttributes();
                final Node regexp = nnm.getNamedItem("regexp");
                final Node name = nnm.getNamedItem("name");
                if (regexp != null && name != null) {
                    for (final ParagraphDelta para : delta._paragraphs) {
                        if (name.getNodeValue().equals(para._name)
                            && !para._textValue.matches(regexp.getNodeValue())
                            && ("TEXT".equals(para._type)
                            || "HTML".equals(para._type))) {
                            errors = errors + "problem with field "+para._name+"\n";
                        }
                    }
                }
            }

        } catch (final Exception e) {
            throw new CCCException("Error with XML parsing ", e);
        }
        if (!errors.isEmpty()) {
            throw new CCCException(errors);
        }
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
    public void updatePage(final PageDelta delta) {

        final Page page = new Page(
            ResourceName.escape(delta._name),
            delta._title);
        page.id(UUID.fromString(delta._id));

        final String templateId = delta._computedTemplate._id;
        if (templateId != null) {
            final Template template =
                _resources.find(Template.class, UUID.fromString(templateId));
            page.template(template);
            validateFields(delta, template);
        }

        // TODO: Remove duplication
        for (final ParagraphDelta para : delta._paragraphs) {
            if ("TEXT".equals(para._type) || "HTML".equals(para._type)) {
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
                     delta._title,
                     page.paragraphs());

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
}
