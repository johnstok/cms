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
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class DTOs {

    /**
     * TODO: Add a description of this method.
     *
     * @param templateDTO
     * @return
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
     * TODO: Add a description of this method.
     *
     * @param template
     * @return
     */
    public static TemplateDTO dtoFrom(final Template template) {
        final TemplateDTO dto = new TemplateDTO(template.title(),
            template.description(),
            template.body());
        dto.setId(template.id().toString());
        return dto;
    }

}
