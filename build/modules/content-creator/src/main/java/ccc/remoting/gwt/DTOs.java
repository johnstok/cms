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
        return new Template(
            templateDTO.getTitle(),
            templateDTO.getDescription(),
            templateDTO.getBody());
    }

}
