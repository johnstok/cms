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

package ccc.contentcreator.api;

import java.util.List;
import java.util.Map;

import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.contentcreator.dto.UserDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Synchronous service for managing resources.
 *
 * @author Civic Computing Ltd
 */
@RemoteServiceRelativePath("resource")
public interface ResourceService extends RemoteService {

    /**
     * TODO: Add a description of this method.
     */
    void logout();

    /**
     * Retrieve a root folder.
     *
     * @param root The identifier for the root folder to retrieve.
     * @return The root folder as a dto.
     */
    FolderDTO getRoot(Root root);

    /**
     * Retrieve a root folder.
     *
     * @param root The identifier for the root folder to retrieve.
     * @return The root folder as a ResourceDTO.
     */
    ResourceDTO getRootAsResource(Root root);

    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The UUID of the existing resource.
     * @return The resource as a dto.
     */
    ResourceDTO getResource(String resourceId);

    /**
     * Update the specified page on the server.
     * TODO: should pass a PageDTO.
     *
     * @param id The uuid of the page to update.
     * @param title The title of the page.
     * @param paragraphs The paragraphs for the page.
     */
    void saveContent(String id,
                     String title,
                     Map<String, ParagraphDTO> paragraphs);

    /**
     * Create a new template in CCC.
     *
     * @param dto Details of the new template to create.
     */
    void createTemplate(final TemplateDTO dto);

    /**
     * List all the templates currently available in CCC.
     *
     * @return A list of templates.
     */
    List<TemplateDTO> listTemplates();

    /**
     * List the available server options.
     *
     * @return A list of available options.
     */
    List<OptionDTO<? extends DTO>> listOptions();

    /**
     * Update the specified list of options on the server.
     *
     * @param options The options to update.
     */
    void updateOptions(List<OptionDTO<? extends DTO>> options);

    /**
     * List all of the folders that are children of the specified parent folder.
     *
     * @param folder The parent folder.
     * @return The list of child folders.
     */
    List<FolderDTO> getFolderChildren(FolderDTO folder);

    /**
     * List all of the children of the specified parent resource.
     *
     * @param resource The parent resource.
     * @return The list of children.
     */
    List<ResourceDTO> getChildren(ResourceDTO resource);

    /**
     * Create a folder with the specified name.
     *
     * @param parent The parent folder within which the new folder should be
     *  created.
     * @param name The name of the new folder.
     * @return A dto representing the new folder.
     */
    FolderDTO createFolder(FolderDTO parent, String name);

    /**
     * Determine the absolute path to a resource.
     *
     * @param resource The resource for which we want the absolute path.
     * @return The absolute path as a string.
     */
    String getAbsolutePath(ResourceDTO resource);

    /**
     * Update the specified template on the server.
     *
     * @param dto The dto representing the updated template.
     */
    void updateTemplate(TemplateDTO dto);

    /**
     * List the available template options for given resource.
     *
     * @param resource The resource to list templates for.
     * @return A list of available options.
     */
    List<OptionDTO<? extends DTO>> listTemplateOptionsForResource(
        ResourceDTO resource);

    /**
     * Update the specified resource's template on the server.
     *
     * @param options The options.
     * @param resource The resource representing the updated resource.
     */
    void updateResourceTemplate(List<OptionDTO<? extends DTO>> options,
                                ResourceDTO resource);
    /**
     * Create a new alias in CCC.
     *
     * @param folderDTO The parent folder for the new alias.
     * @param aliasDTO Details of the new alias to create.
     */
    void createAlias(final FolderDTO folderDTO, final AliasDTO aliasDTO);

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folder The folder to check.
     * @param name The name of the resource.
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    boolean nameExistsInFolder(final FolderDTO folder, final String name);

    /**
     * Query all users.
     *
     * @return Returns list of users.
     */
    List<UserDTO> listUsers();

    /**
     * Query users with specified role.
     *
     * @param role The role as a string.
     * @return Returns list of users.
     */
    List<UserDTO> listUsersWithRole(String role);

    /**
     * Create a new user in the system.
     *
     * @param userDto A dto representing the new user.
     * @param password The password for the new user.
     */
    void createUser(final UserDTO userDto, String password);


    /**
     * Query users with specified username.
     *
     * @param username The username as a string.
     * @return Returns list of users.
     */
    List<UserDTO> listUsersWithUsername(String username);

    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    boolean usernameExists(String username);

    /**
     * Query users with specified email.
     *
     * @param email The email as a string.
     * @return Returns list of users.
     */
    List<UserDTO> listUsersWithEmail(String email);


    /**
     * Updates the user in the system.
     *
     * @param userDto A dto representing the  user.
     * @param password The new password for user. Null value will
     * retain old password.
     */
    void updateUser(final UserDTO userDto, String password);

    /**
     * Creates a new page.
     *
     * @param folderDto The parent folder.
     * @param pageDto   The page to be created.
     * @param templateDto The template for the page.
     */
    void createPage(final FolderDTO folderDto,
                    final PageDTO pageDto,
                    final TemplateDTO templateDto);

    /**
     * Returns true if template name exists in the template folder.
     *
     * @param templateName The name to look up.
     * @return True if name exists.
     */
     boolean templateNameExists(final String templateName);

     /**
     * Returns TemplateDTO of the template assigned for a resource.
     *
     * @param resourceDTO Resource used for lookup.
     * @return TemplateDTO
     */
    TemplateDTO getTemplateForResource(final ResourceDTO resourceDTO);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    UserDTO loggedInUser();

    /**
     * Changes resource's parent.
     *
     * @param folderDTO The new parent folder.
     * @param id The id of the resource.
     */
    void move(final FolderDTO folderDTO, final String id);

    /**
     * Update alias' target.
     *
     * @param target The new target ResourceDTO
     * @param aliasId The alias id
     */
    void updateAlias(ResourceDTO target, String aliasId);

    /**
     * Query whether given resource's parent has a resource with given name.
     *
     * @param id The resource id which parent folder to check.
     * @param name The name of the resource.
     * @return Returns true in case parent folder has a resource with given
     * name, false otherwise.
     */
    boolean nameExistsInParentFolder(final String id,
                                            final String name);

    /**
     * Rename resource.
     *
     * @param id The id of the resource.
     * @param name The new name for the resource.
     */
    void rename(final String id, final String name);

    /**
     * Update the tags for a resource.
     *
     * @param id
     * @param tags
     */
    void updateTags(String id, String tags);
}
