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

package ccc.view.contentcreator.client;

import java.util.List;
import java.util.Map;

import ccc.view.contentcreator.dto.DTO;
import ccc.view.contentcreator.dto.FolderDTO;
import ccc.view.contentcreator.dto.OptionDTO;
import ccc.view.contentcreator.dto.ResourceDTO;
import ccc.view.contentcreator.dto.TemplateDTO;

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
     *
     * @param root
     * @return
     */
    FolderDTO getRoot(Root root);

    /**
     * TODO: Add a description of this method.
     *
     * @param absolutePath
     * @return
     */
    ResourceDTO getResource(String absolutePath);

    /**
     * TODO: Add a description of this method.
     *
     * @param id
     * @param title
     * @param paragraphs
     */
    void saveContent(String id, String title, Map<String, String> paragraphs);

    /**
     * TODO: Add a description of this method.
     *
     * @param dto
     */
    void createTemplate(final TemplateDTO dto);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    List<TemplateDTO> listTemplates();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    List<OptionDTO<? extends DTO>> listOptions();

    /**
     * TODO: Add a description of this method.
     *
     * @param options
     */
    void updateOptions(List<OptionDTO<? extends DTO>> options);

    /**
     * TODO: Add a description of this method.
     *
     * @param folder
     * @return
     */
    List<FolderDTO> getFolderChildren(FolderDTO folder);

    /**
     * TODO: Add a description of this method.
     *
     * @param folder
     * @return
     */
    List<ResourceDTO> getChildren(FolderDTO folder);

    /**
     * TODO: Add a description of this method.
     *
     * @param parent
     * @param name
     */
    void createFolder(FolderDTO parent, String name);

    /**
     * TODO: Add a description of this method.
     *
     * @param item
     */
    String getAbsolutePath(ResourceDTO item);
}