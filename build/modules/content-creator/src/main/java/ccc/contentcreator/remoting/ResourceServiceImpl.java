/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.contentcreator.remoting;

import static ccc.contentcreator.remoting.DTOs.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.contentcreator.dto.UserDTO;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AssetManagerLocal;
import ccc.services.ContentManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.ServiceNames;
import ccc.services.UserManagerLocal;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Implementation of {@link ResourceService}.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceServiceImpl extends RemoteServiceServlet
                                    implements ResourceService {

    private Registry _registry = new JNDI();

    /**
     * Constructor.
     *
     */
    public ResourceServiceImpl() { super(); }

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public ResourceServiceImpl(final Registry registry) {
        DBC.require().notNull(registry);
        _registry = registry;
    }

    /**
     * {@inheritDoc}
     * TODO: Dodgy.
     */
    public FolderDTO getRoot(final Root root) {
        switch (root) {
            case CONTENT:
                final Resource contentResource =
                    contentManager().lookup(new ResourcePath("")).get();
                return DTOs.dtoFrom(contentResource);

            case ASSETS:
                final Resource assetResource =
                    assetManager().lookup(new ResourcePath(""));
                return DTOs.dtoFrom(assetResource);

            default:
                throw new CCCException("Unable to look up root: "+root);
        }
    }

    /**
     * {@inheritDoc}
     * TODO: Dodgy.
     */
    public ResourceDTO getRootAsResource(final Root root) {
        switch (root) {
            case CONTENT:
                final Resource contentResource =
                    contentManager().lookup(new ResourcePath("")).get();
                return DTOs.dtoFrom(contentResource);

            case ASSETS:
                final Resource assetResource =
                    assetManager().lookup(new ResourcePath(""));
                return DTOs.dtoFrom(assetResource);

            default:
                throw new CCCException("Unable to look up root: "+root);
        }
    }

    /**
     * Accessor for the content manager.
     *
     * @return A ContentManager.
     */
    ContentManagerLocal contentManager() {
        return _registry.get(ServiceNames.CONTENT_MANAGER_LOCAL);
    }

    /**
     * Accessor for the asset manager.
     *
     * @return An AssetManager.
     */
    AssetManagerLocal assetManager() {
        return _registry.get(ServiceNames.ASSET_MANAGER_LOCAL);
    }

    /**
     * Accessor for the user manager.
     *
     * @return A UserManager.
     */
    UserManagerLocal userManager() {
        return _registry.get(ServiceNames.USER_MANAGER_LOCAL);
    }

    /**
     * {@inheritDoc}
     */
    public ResourceDTO getResource(final String resourceId) {
        final Resource resource = lookupContent(resourceId);
        return DTOs.dtoFrom(resource);
    }

    private Resource lookupContent(final String resourceId) {
        return contentManager().lookup(UUID.fromString(resourceId));
    }

    /**
     * {@inheritDoc}
     */
    public void saveContent(final String id,
                            final String title,
                            final Map<String, ParagraphDTO> paragraphs) {
        final Map <String, Paragraph> newParagraphs =
            new HashMap<String, Paragraph>();

        for (final Entry<String, ParagraphDTO> para : paragraphs.entrySet()) {
            newParagraphs.put(para.getKey(),
                DTOs.paragraphFrom(para.getValue()));
        }

        contentManager().update(UUID.fromString(id), title, newParagraphs);
    }

    /**
     * Create a new template.
     *
     * @param dto A DTO representing the template.
     */
    public void createTemplate(final TemplateDTO dto) {
        assetManager().createDisplayTemplate(DTOs.templateFrom(dto));
    }

    private void setDefaultTemplate(final TemplateDTO templateDTO) {
        if (null==templateDTO) {
            contentManager().setDefaultTemplate(null);
        } else {
            final Template newDefault =
                assetManager()
                    .lookup(UUID.fromString(templateDTO.getId()))
                    .as(Template.class);
            contentManager().setDefaultTemplate(newDefault);
        }

    }

    /**
     * {@inheritDoc}
     */
    public List<TemplateDTO> listTemplates() {
        return dtoFrom(assetManager().lookupTemplates(), TemplateDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    public List<OptionDTO<? extends DTO>> listOptions() {

        final List<OptionDTO<? extends DTO>> options =
            new ArrayList<OptionDTO<? extends DTO>>();

        final Folder contentRoot = contentManager().lookupRoot();
        final Template rootTemplate = contentRoot.displayTemplateName();

        final OptionDTO<TemplateDTO> defaultTemplate =
            new OptionDTO<TemplateDTO>(dtoFrom(rootTemplate),
                                       listTemplates(),
                                       OptionDTO.Type.CHOICES);
        options.add(defaultTemplate);

        return options;
    }

    /**
     * {@inheritDoc}
     */
    public void updateOptions(final List<OptionDTO<? extends DTO>> options) {
        final OptionDTO<TemplateDTO> defaultTemplate =
            options.get(0).makeTypeSafe();
        if (defaultTemplate.hasChanged()) {
            setDefaultTemplate(defaultTemplate.getCurrentValue());
        }
    }

    /** {@inheritDoc} */
    public List<FolderDTO> getFolderChildren(final FolderDTO folder) {
        final Folder parent =
            contentManager()
                .lookup(UUID.fromString(folder.getId()))
                .as(Folder.class);
        final List<FolderDTO> children = new ArrayList<FolderDTO>();
        for (final Resource r : parent.entries()) {
            if (r.type() == ResourceType.FOLDER) {
                children.add(DTOs.<FolderDTO>dtoFrom(r));
            }
        }
        return children;
    }

    /** {@inheritDoc} */
    public List<ResourceDTO> getChildren(final ResourceDTO resource) {

        final Resource res =
            contentManager().lookup(UUID.fromString(resource.getId()));
        final List<ResourceDTO> children = new ArrayList<ResourceDTO>();
        if (res.type() == ResourceType.FOLDER) {
            final Folder parent = res.as(Folder.class);
            for (final Resource r : parent.entries()) {
                children.add(DTOs.<ResourceDTO>dtoFrom(r));
            }
        }
        return children;
    }

    /** {@inheritDoc} */
    public FolderDTO createFolder(final FolderDTO parent, final String name) {
        final Folder f =
            contentManager().create(
                UUID.fromString(parent.getId()),
                new Folder(new ResourceName(name)));
        return DTOs.dtoFrom(f);
    }

    /** {@inheritDoc} */
    public String getAbsolutePath(final ResourceDTO item) {
        final Resource r =
            contentManager().lookup(UUID.fromString(item.getId()));
        return r.absolutePath().toString();
    }

    /** {@inheritDoc} */
    public void updateTemplate(final TemplateDTO dto) {
        final Template t = DTOs.templateFrom(dto);
        assetManager().update(t);
    }

    /** {@inheritDoc} */
    public List<OptionDTO<? extends DTO>>
        listTemplateOptionsForResource(final ResourceDTO resourceDTO) {
        final List<OptionDTO<? extends DTO>> options =
            new ArrayList<OptionDTO<? extends DTO>>();

        final Resource r =
            contentManager().lookup(UUID.fromString(resourceDTO.getId()));

        final Template selectedTemplate = r.displayTemplateName();

        final OptionDTO<TemplateDTO> templates =
            new OptionDTO<TemplateDTO>(dtoFrom(selectedTemplate),
                                       listTemplates(),
                                       OptionDTO.Type.CHOICES);
        options.add(templates);

        return options;
    }

    /** {@inheritDoc} */
    public void updateResourceTemplate(
                                   final List<OptionDTO<? extends DTO>> options,
                                   final ResourceDTO resourceDTO) {
        final OptionDTO<TemplateDTO> option =
            options.get(0).makeTypeSafe();

        if (option.hasChanged()) {
            final TemplateDTO templateDTO = option.getCurrentValue();
            if (null==templateDTO) {
                contentManager().updateTemplateForResource(
                    UUID.fromString(resourceDTO.getId()), null);
            } else {
                final Template selectedTemplate =
                    assetManager()
                        .lookup(UUID.fromString(templateDTO.getId()))
                        .as(Template.class);
                contentManager().updateTemplateForResource(
                    UUID.fromString(resourceDTO.getId()), selectedTemplate);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createAlias(final FolderDTO folderDTO,
                            final AliasDTO aliasDTO) {

        final Resource target = lookupContent(aliasDTO.getTargetId());
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }

        final Resource parent = lookupContent(folderDTO.getId());
        if (parent == null) {
            throw new CCCException("Parent does not exists.");
        }

        contentManager().create(parent.id(),
            new Alias(new ResourceName(aliasDTO.getName()), target));
    }

    /**
     * {@inheritDoc}
     */
    public boolean nameExistsInFolder(final FolderDTO folderDTO,
                                      final String name) {
        final Folder folder =
            lookupContent(folderDTO.getId()).as(Folder.class);
        return folder.hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    public List<UserDTO> listUsers() {
        final UserManagerLocal um = userManager();
        final Collection<User> users = um.listUsers();
        return DTOs.dtoFrom(users);
    }

    /** {@inheritDoc} */
    public List<UserDTO> listUsersWithRole(final String role) {
        final UserManagerLocal um = userManager();
        final Collection<User> users =
            um.listUsersWithRole(CreatorRoles.valueOf(role));
        return DTOs.dtoFrom(users);
    }

    /** {@inheritDoc} */
    public void createUser(final UserDTO userDto, final String password) {
        final UserManagerLocal um = userManager();
        final User user = DTOs.userFrom(userDto);
        um.createUser(user, password);
    }

    /** {@inheritDoc} */
    public List<UserDTO> listUsersWithUsername(final String username) {
        final UserManagerLocal um = userManager();
        final Collection<User> users =
            um.listUsersWithUsername(username);
        return DTOs.dtoFrom(users);
    }

    /** {@inheritDoc} */
    public List<UserDTO> listUsersWithEmail(final String email) {
        final UserManagerLocal um = userManager();
        final Collection<User> users =
            um.listUsersWithEmail(email);
        return DTOs.dtoFrom(users);
    }

    /** {@inheritDoc} */
    public boolean usernameExists(final String username) {
        final UserManagerLocal um = userManager();
        return um.usernameExists(username);
    }

    /** {@inheritDoc} */
    public void updateUser(final UserDTO userDto, final String password) {
        final UserManagerLocal um = userManager();
        final User user = DTOs.userFrom(userDto);
        um.updateUser(user, password);
    }

    /** {@inheritDoc} */
    public void createPage(final FolderDTO folderDto,
                           final PageDTO pageDto,
                           final TemplateDTO templateDto) {
        final Page page = new Page(
            ResourceName.escape(pageDto.getName()),
            pageDto.getTitle());

        final String parentFolderId = folderDto.getId();

        if (templateDto != null) {
            final Template template =  assetManager().lookup(
                UUID.fromString(templateDto.getId())).as(Template.class);
            page.displayTemplateName(template);
        }

       final Set<Entry<String, ParagraphDTO>> entries =
           pageDto.getParagraphs().entrySet();
        for (final Map.Entry<String, ParagraphDTO> item : entries) {
            final Paragraph paragraph = DTOs.paragraphFrom(item.getValue());
            page.addParagraph(item.getKey(), paragraph);
        }

        contentManager().create(UUID.fromString(parentFolderId), page);
    }

    /** {@inheritDoc} */
    public boolean templateNameExists(final String templateName) {
        boolean result = false;
        for (final TemplateDTO t : listTemplates()) {
            if (t.getName().equals(templateName)) {
                result = true;
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    public TemplateDTO getTemplateForResource(final ResourceDTO resourceDTO) {
        final Resource r =
            contentManager().lookup(UUID.fromString(resourceDTO.getId()));

        final Template selectedTemplate = r.computeTemplate(null);
        return DTOs.dtoFrom(selectedTemplate);
    }

    /** {@inheritDoc} */
    public void logout() {
        getThreadLocalRequest().getSession().invalidate();
    }

    /** {@inheritDoc} */
    public UserDTO loggedInUser() {
        UserDTO userDTO = null;

        final User user = userManager().loggedInUser();
        if (user != null) {
            userDTO = DTOs.dtoFrom(user);
        }

        return userDTO;
    }

    /** {@inheritDoc} */
    public void move(final FolderDTO folderDTO, final String id) {
        contentManager().move(UUID.fromString(id),
            UUID.fromString(folderDTO.getId()));
    }

    /** {@inheritDoc} */
    public void updateAlias(final ResourceDTO target, final String aliasId) {
        contentManager().updateAlias(UUID.fromString(target.getId()),
            UUID.fromString(aliasId));
    }

    /**
     * {@inheritDoc}
     */
    public boolean nameExistsInParentFolder(final String id,
                                      final String name) {
        final Resource r =
            lookupContent(id);
        return r.parent().hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    public void rename(final String id, final String name) {
        contentManager().rename(UUID.fromString(id), name);
    }

    /** {@inheritDoc} */
    public void updateTags(final String id, final String tags) {
        final ResourceDAOLocal rdao =
            _registry.get(ServiceNames.RESOURCE_DAO_LOCAL);
        rdao.updateTags(id, tags);
    }
}
