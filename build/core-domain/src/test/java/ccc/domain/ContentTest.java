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
 * TODO: test that null keys are disallowed.
 * TODO: test that max key length is 256.
 *
 * @author Civic Computing Ltd
 */
public final class ContentTest extends TestCase {

    /**
     * Test.
     */
    public void testToJson() {

        // ARRANGE
        final Content content = new Content(new ResourceName("foo"), "Foo?");
        content.addParagraph("bar", new Paragraph("bar"));
        content.addParagraph("baz", new Paragraph("baz"));

        // ACT
        final String jsonString = content.toJSON();

        // ASSERT
        assertEquals("{\"id\": \""+content.id().toString()+"\",\"title\": \"Foo?\",\"paragraphs\": {\"bar\": {\"body\": \"bar\"},\"baz\": {\"body\": \"baz\"}}}", jsonString);
    }

    /**
     * Test.
     */
    public void testConstructorCanGenerateName() {

        // ARRANGE
        final ResourceName name = new ResourceName("foo");

        // ACT
        final Content content = new Content(name);

        // ASSERT
        assertEquals(name.toString(), content.title());
        assertEquals(name, content.name());
    }

    /**
     * Test.
     */
    public void testAddNewParagraph() {

        // ARRANGE
        final Content content = new Content(new ResourceName("foo"), "Title");

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
        final Content content = new Content(new ResourceName("foo"), "Title");
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
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
    
    public void testDeleteAllParagraphs() {

        // ARRANGE
        final Content content = new Content(new ResourceName("foo"), "Title");
        content.addParagraph("header", new Paragraph("<H1>Header</H1>"));
        content.addParagraph("footer", new Paragraph("<H1>Footer</H1>"));

        // ACT
        content.deleteAllParagraphs();

        // ASSERT
        assertEquals(0, content.paragraphs().size());
    }
}
