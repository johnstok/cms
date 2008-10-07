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
     * Retrieve a root folder.
     *
     * @param root The identifier for the root folder to retrieve.
     * @return The root folder as a dto.
     */
    FolderDTO getRoot(Root root);

    /**
     * Get the resource located at the specified path.
     *
     * @param absolutePath An absolute path to an existing resource.
     * @return The resource as a dto.
     */
    ResourceDTO getResource(String absolutePath);

    /**
     * Update the specified page on the server.
     * TODO: should pass a PageDTO.
     *
     * @param id The uuid of the page to update.
     * @param title The title of the page.
     * @param paragraphs The paragraphs for the page.
     */
    void saveContent(String id, String title, Map<String, String> paragraphs);

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
     * List all of the children of the specified parent folder.
     *
     * @param folder The parent folder.
     * @return The list of children.
     */
    List<ResourceDTO> getChildren(FolderDTO folder);

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
     */
    void createUser(final UserDTO userDto);

    /**
     * Query users with specified username.
     *
     * @param username The username as a string.
     * @return Returns list of users.
     */
    List<UserDTO> listUsersWithUsername(String username);

    /**
     * Query users with specified email.
     *
     * @param username The email as a string.
     * @return Returns list of users.
     */
    List<UserDTO> listUsersWithEmail(String email);
}
