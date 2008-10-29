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

import java.util.UUID;

import junit.framework.TestCase;


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
        final String definition = "def";
        final String id = UUID.randomUUID().toString();

        // ACT
        final TemplateDTO dto =
            new TemplateDTO(
                id,
                0,
                "name",
                title,
                description,
                body,
                definition);

        // ASSERT
        assertEquals(title, dto.getTitle());
        assertEquals(description, dto.getDescription());
        assertEquals(definition, dto.getDefinition());
        assertEquals(body, dto.getBody());
        assertEquals(id, dto.getId());
    }
}
