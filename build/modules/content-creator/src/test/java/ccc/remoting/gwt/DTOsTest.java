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

import junit.framework.TestCase;
import ccc.domain.Template;
import ccc.view.contentcreator.dto.TemplateDTO;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class DTOsTest extends TestCase {

    public void testFromTemplateDto() {

        // ARRANGE
        final TemplateDTO dto =
            new TemplateDTO("title",
                            "description",
                            "body");

        // ACT
        final Template actual = DTOs.templateFrom(dto);

        // ASSERT
        assertEquals("title", actual.title());
        assertEquals("description", actual.description());
        assertEquals("body", actual.body());
    }
}
