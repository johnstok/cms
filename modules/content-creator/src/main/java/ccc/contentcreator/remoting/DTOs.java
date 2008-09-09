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
package ccc.contentcreator.remoting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.Template;


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

        final TemplateDTO dto =
            new TemplateDTO(
                template.title(),
                template.description(),
                template.body(),
                template.id().toString());
        return dto;
    }

    /**
     * Convert a resource to a dto.
     *
     * @param resource The resource to convert.
     * @param <T> The type of the dto to return.
     * @return A dto representing the specified resource.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ResourceDTO> T dtoFrom(final Resource resource) {

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
                final Map<String, String> paragraphs =
                    new HashMap<String, String>();
                for (final Map.Entry<String, Paragraph> para
                        : p.paragraphs().entrySet()) {
                    paragraphs.put(para.getKey(), para.getValue().body());
                }
                return (T) new PageDTO(
                    p.id().toString(),
                    p.type().toString(),
                    p.name().toString(),
                    p.title(),
                    paragraphs
                );

            case TEMPLATE:
                return (T) dtoFrom(resource.as(Template.class));
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
