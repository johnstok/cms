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

import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

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
     */
    void createFolder(FolderDTO parent, String name);

    /**
     * Determine the absolute path to a resource.
     *
     * @param resource The resource for which we want the absolute path.
     * @return The absolute path as a string.
     */
    String getAbsolutePath(ResourceDTO resource);
}
