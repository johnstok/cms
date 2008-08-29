/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.gwt;

import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.view.contentcreator.dto.DTO;
import ccc.view.contentcreator.dto.FolderDTO;
import ccc.view.contentcreator.dto.PageDTO;
import ccc.view.contentcreator.dto.ResourceDTO;
import ccc.view.contentcreator.dto.TemplateDTO;


/**
 * Helper methods for DTOs.
 *
 * @author Civic Computing Ltd
 */
public final class DTOs {

    private DTOs() { super(); }

    /**
     * Create a {@link Template} from a {@link TemplateDTO}.
     *
     * @param templateDTO The dto from which to create the template.
     * @return A valid template.
     */
    public static Template templateFrom(final TemplateDTO templateDTO) {
        final Template t = new Template(
            templateDTO.getTitle(),
            templateDTO.getDescription(),
            templateDTO.getBody());


        if (null!=templateDTO.getId()) {
            t.id(UUID.fromString(templateDTO.getId()));
        }

        return t;
    }

    /**
     * Create a  {@link TemplateDTO} from a {@link Template}.
     *
     * @param template The template from which to create a dto.
     * @return A dto representing the template.
     */
    public static TemplateDTO dtoFrom(final Template template) {
        if (null==template) {
            return null;
        }

        final TemplateDTO dto = new TemplateDTO(template.title(),
            template.description(),
            template.body());
        dto.setId(template.id().toString());
        return dto;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param resource
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends DTO> T dtoFrom(final Resource resource) {

        switch (resource.type()) {
            case FOLDER:
                final Folder f = resource.as(Folder.class);
                return (T) new FolderDTO(
                    f.id().toString(),
                    f.type().toString(),
                    f.name().toString(),
                    f.title(),
                    f.folderCount()
                );

            case PAGE:
                final Page p = resource.as(Page.class);
                return (T) new PageDTO(
                    p.id().toString(),
                    p.type().toString(),
                    p.name().toString(),
                    p.title()
                );

            default:
                return (T) new ResourceDTO(
                    resource.id().toString(),
                    resource.type().toString(),
                    resource.name().toString(),
                    resource.title()
                );
        }
    }

}
