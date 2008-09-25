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
import ccc.contentcreator.dto.FileDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.domain.Alias;
import ccc.domain.Data;
import ccc.domain.File;
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
        assertEquals("alias", actual.getName());
        assertEquals(target.id().toString(), actual.getTargetId());
    }

    /**
     * Test.
     */
    public void testDtoFromFile() {

        // ARRANGE
        final File f = new File(new ResourceName("a"), "b", "c", new Data(), 0);

        // ACT
        final FileDTO actual = DTOs.dtoFrom(f);

        // ASSERT
        assertEquals("a", actual.getName().toString());
        assertEquals(Long.valueOf(0), actual.getSize());
        assertEquals("application/octet-stream", actual.getMimeType());
        assertEquals("FILE", actual.getType());
        assertEquals("c", actual.getDescription());
    }
}
