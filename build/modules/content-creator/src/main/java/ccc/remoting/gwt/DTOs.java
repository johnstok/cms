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

import ccc.domain.Template;
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

}
