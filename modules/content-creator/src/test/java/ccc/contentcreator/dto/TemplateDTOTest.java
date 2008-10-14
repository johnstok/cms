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
package ccc.contentcreator.dto;

import junit.framework.TestCase;
import ccc.domain.UUID;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class TemplateDTOTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE
        final String title = "title";
        final String description = "description";
        final String body = "body";
        final String id = UUID.randomUUID().toString();

        // ACT
        final TemplateDTO dto =
            new TemplateDTO(
                id,
                0,
                "name",
                title,
                description,
                body);

        // ASSERT
        assertEquals(title, dto.getTitle());
        assertEquals(description, dto.getDescription());
        assertEquals(body, dto.getBody());
        assertEquals(id, dto.getId());
    }
}
