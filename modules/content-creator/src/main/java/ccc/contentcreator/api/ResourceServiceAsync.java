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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous service for managing resources.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceServiceAsync {

    void logout(AsyncCallback<Void> callback);

    /**
     * @see ResourceService#getRoot(Root)
     */
    void getRoot(Root root, AsyncCallback<FolderDTO> callback);

    /**
     * @see ResourceService#getRootAsResource(Root)
     */
    void getRootAsResource(Root root, AsyncCallback<ResourceDTO> callback);

    /**
     * @see ResourceService#getResource(String)
     */
    void getResource(String resourceId,
                     AsyncCallback<? extends ResourceDTO> callback);

    /**
     * @see ResourceService#saveContent(String, String, Map)
     */
    void saveContent(String id,
                     String title,
                     Map<String, ParagraphDTO> paragraphs,
                     AsyncCallback<Void> callback);

    /**
     * @see ResourceService#createTemplate(TemplateDTO)
     */
    void createTemplate(TemplateDTO dto,
                        AsyncCallback<Void> callback);

    /**
     * @see ResourceService#listTemplates()
     */
    void listTemplates(AsyncCallback<List<TemplateDTO>> callback);

    /**
     * @see ResourceService#listOptions()
     */
    void listOptions(AsyncCallback<List<OptionDTO<? extends DTO>>> callback);

    /**
     * @see ResourceService#updateOptions(List)
     */
    void updateOptions(List<OptionDTO<? extends DTO>> options,
                       AsyncCallback<Void> callback);

    /**
     * @see ResourceService#getFolderChildren(FolderDTO)
     */
    void getFolderChildren(FolderDTO folder,
                           AsyncCallback<List<FolderDTO>> callback);

    /**
     * @see ResourceService#getChildren(ResourceDTO)
     */
    void getChildren(ResourceDTO resource,
                     AsyncCallback<List<ResourceDTO>> callback);

    /**
     * @see ResourceService#createFolder(FolderDTO, String)
     */
    void createFolder(FolderDTO parent,
                      String name,
                      AsyncCallback<FolderDTO> callback);

    /**
     * @see ResourceService#getAbsolutePath(ResourceDTO)
     */
    void getAbsolutePath(ResourceDTO item, AsyncCallback<String> asyncCallback);

    /**
     * @see ResourceService#updateTemplate(TemplateDTO);
     */
    void updateTemplate(TemplateDTO dto, AsyncCallback<Void> callback);

    /**
     * @see ResourceService#listTemplateOptionsForResource(ResourceDTO)
     */
    void listTemplateOptionsForResource(ResourceDTO item,
                                        AsyncCallback<List<OptionDTO<? extends DTO>>> callback);

    /**
     * @see ResourceService#updateResourceTemplate(List, ResourceDTO)
     */
    void updateResourceTemplate(List<OptionDTO<? extends DTO>> options,
                                ResourceDTO resource,
                                AsyncCallback<Void> callback);

    /**
     * @see ResourceService#createAlias(FolderDTO, AliasDTO)
     */
    void createAlias(FolderDTO folderDTO,
                     AliasDTO aliasDTO,
                     AsyncCallback<Void> callback);

    /**
     * @see ResourceService#nameExistsInFolder(FolderDTO, String)
     */
    void nameExistsInFolder(FolderDTO folderDTO,
                            String name,
                            AsyncCallback<Boolean> callback);

    /**
     * @see ResourceService#listUsers()
     */
    void listUsers(AsyncCallback<List<UserDTO>> callback);

    /**
     * @see ResourceService#listUsersWithRole(String)
     */
    void listUsersWithRole(String role, AsyncCallback<List<UserDTO>> callback);

    /**
     * @see ResourceService#createUser(UserDTO, String)
     */
    void createUser(final UserDTO userDto,
                    final String password,
                    AsyncCallback<Void> callback);

    /**
     * @see ResourceService#listUsersWithUsername(String)
     */
    void listUsersWithUsername(String username,
                               AsyncCallback<List<UserDTO>> callback);

    /**
     * @see ResourceService#listUsersWithEmail(String)
     */
    void listUsersWithEmail(String email,
                            AsyncCallback<List<UserDTO>> callback);

    /**
     * @see ResourceService#usernameExists(String)
     */
    void usernameExists(String username, AsyncCallback<Boolean> callback);

    /**
     * @see ResourceService#updateUser(UserDTO)
     */
    void updateUser(final UserDTO userDto,
                    final String password,
                    AsyncCallback<Void> callback);

    /**
     * @see ResourceService#createPage(FolderDTO, PageDTO, TemplateDTO)
     */
    void createPage(FolderDTO folderDTO,
                    PageDTO pageDTO,
                    TemplateDTO templateDTO,
                    AsyncCallback<Void> callback);

    /**
     * @see ResourceService#templateNameExists(String)
     */
    void templateNameExists(final String templateName,
                            AsyncCallback<Boolean> callback);

    /**
     * @see ResourceService#getTemplateForResource()
     */
    void getTemplateForResource(ResourceDTO resourceDTO,
                                AsyncCallback<TemplateDTO> callback);

    void loggedInUser(AsyncCallback<UserDTO> callback);

    /**
     * @see ResourceService#move()
     */
    void move(FolderDTO folderDTO, String id, AsyncCallback<Void> callback);

    /**
     * @see ResourceService#updateAlias()
     */
    void updateAlias(ResourceDTO target,
                     String aliasId,
                     AsyncCallback<Void> callback);

    /**
     * @see ResourceService#nameExistsInParentFolder()
     */
    void nameExistsInParentFolder(String id,
                                  String value,
                                  AsyncCallback<Boolean> errorReportingCallback);

    /**
     * @see ResourceService#rename()
     */
    void rename(String id, String name, AsyncCallback<Void> callback);

}
