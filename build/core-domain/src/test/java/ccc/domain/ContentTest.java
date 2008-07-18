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

package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for the {@link Content} class.
 *
 * @author Civic Computing Ltd
 */
public final class ContentTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructorCanGenerateName() {

        // ARRANGE
        ResourceName name = new ResourceName("foo");

        // ACT
        Content content = new Content(name);

        // ASSERT
        assertEquals(name.toString(), content.title());
        assertEquals(name, content.name());
    }

    /**
     * Test.
     */
    public void testAddNewParagraph() {

        // ARRANGE
        Content content = new Content(new ResourceName("foo"), "Title");

        // ACT
        content.addParagraph("header", new Paragraph("<H1>Header</H1>"));

        // Assert
        assertEquals(1, content.paragraphs().size());

    }

    /**
     * Test.
     */
    public void testDeleteParagraph() {

        // ARRANGE
        Content content = new Content(new ResourceName("foo"), "Title");
        content.addParagraph("header", new Paragraph("<H1>Header</H1>"));
        content.addParagraph("footer", new Paragraph("<H1>Footer</H1>"));

        // ACT
        content.deleteParagraph("header");

        // Assert
        assertEquals(1, content.paragraphs().size());
        assertEquals(
            "<H1>Footer</H1>",
            content.paragraphs().get("footer").body());
    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyNames() {
        // ACT
        try {
            new Content(null);
            fail("Resources should reject NULL for the name parameter.");

         // ASSERT
        } catch (IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
