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
package ccc.view.contentcreator.dto;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class TemplateDTOTest extends TestCase {

    public void testConstructor() {

        // ARRANGE
        final String title = "title";
        final String description = "description";
        final String body = "body";

        // ACT
        final TemplateDTO dto = new TemplateDTO(title, description, body);

        // ASSERT
        assertEquals(title, dto.getTitle());
        assertEquals(description, dto.getDescription());
        assertEquals(body, dto.getBody());
    }
}
