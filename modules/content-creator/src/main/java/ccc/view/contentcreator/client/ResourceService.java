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

    FolderDTO getRoot(Root root);

    FolderDTO getResource(String absolutePath);

    void saveContent(String id, String title, Map<String, String> paragraphs);

    void createTemplate(final TemplateDTO dto);

    List<TemplateDTO> listTemplates();

    List<OptionDTO<? extends DTO>> listOptions();

    void updateOptions(List<OptionDTO<? extends DTO>> options);

    List<FolderDTO> getFolderChildren(FolderDTO folder);

    List<ResourceDTO> getChildren(FolderDTO folder);

    void createFolder(FolderDTO parent, String name);
}