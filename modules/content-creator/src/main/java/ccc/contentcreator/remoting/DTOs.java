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
import ccc.domain.CCCException;
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
                return (T) dtoFrom(resource.as(Folder.class));

            case PAGE:
                return (T) dtoFrom(resource.as(Page.class));

            case TEMPLATE:
                return (T) dtoFrom(resource.as(Template.class));

            default:
                throw new CCCException(
                    "Cannot convert resource of type "+resource.type());
        }
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
                template.id().toString(),
                template.version(),
                template.name().toString(),
                template.title(),
                template.description(),
                template.body());
        return dto;
    }

    /**
     * Create a DTO for a folder resource.
     *
     * @param f The folder.
     * @return The DTO.
     */
    private static FolderDTO dtoFrom(final Folder f) {

        return new FolderDTO(
            f.id().toString(),
            f.version(),
            f.name().toString(),
            f.title(),
            f.folderCount()
        );
    }

    /**
     * Create a DTO for a page resource.
     *
     * @param p The page.
     * @return The DTO.
     */
    private static PageDTO dtoFrom(final Page p) {

        final Map<String, String> paragraphs =
            new HashMap<String, String>();
        for (final Map.Entry<String, Paragraph> para
                : p.paragraphs().entrySet()) {
            paragraphs.put(para.getKey(), para.getValue().body());
        }
        return new PageDTO(
            p.id().toString(),
            p.version(),
            p.name().toString(),
            p.title(),
            paragraphs
        );
    }

}
