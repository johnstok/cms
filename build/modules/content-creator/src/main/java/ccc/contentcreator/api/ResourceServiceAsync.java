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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous service for managing resources.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceServiceAsync {

    /**
     * @see ResourceService#getRoot(Root)
     */
    void getRoot(Root root, AsyncCallback<FolderDTO> callback);

    /**
     * @see ResourceService#getResource(String)
     */
    void getResource(String absolutePath,
                     AsyncCallback<? extends ResourceDTO> callback);

    /**
     * @see ResourceService#saveContent(String, String, Map)
     */
    void saveContent(String id,
                     String title,
                     Map<String, String> paragraphs,
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
     * @see ResourceService#getChildren(FolderDTO)
     */
    void getChildren(FolderDTO folder,
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
     * TODO: Add a description of this method.
     *
     * @param aliasDTO
     * @param callback
     */
    void createAlias(AliasDTO aliasDTO, AsyncCallback<Void> callback);
}
