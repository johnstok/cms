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

import java.util.UUID;

import junit.framework.TestCase;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.domain.Alias;
import ccc.domain.Folder;
import ccc.domain.ResourceName;
import ccc.domain.Template;


/**
 * Tests for the {@link DTOs} class.
 *
 * @author Civic Computing Ltd
 */
public class DTOsTest extends TestCase {

    /**
     * Test.
     */
    public void testFromTemplateDto() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final TemplateDTO dto =
            new TemplateDTO(
                id.toString(),
                0,
                "name",
                "title",
                "description",
                "body");

        // ACT
        final Template actual = DTOs.templateFrom(dto);

        // ASSERT
        assertEquals("title", actual.title());
        assertEquals("description", actual.description());
        assertEquals("body", actual.body());
    }

    /**
     * Test.
     */
    public void testDtoFromAlias() {

        // ARRANGE
        final Folder target = new Folder(new ResourceName("target"));
        final Alias alias = new Alias(new ResourceName("alias"), target);

        // ACT
        final AliasDTO actual = DTOs.dtoFrom(alias);

        // ASSERT
        assertEquals("alias", actual.getName().toString());
        assertEquals(target.id().toString(), actual.getTargetId());
    }
}
