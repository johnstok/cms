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
 * Tests for the {@link Page} class.
 *
 * TODO: test that null keys are disallowed.
 * TODO: test that max key length is 256.
 *
 * @author Civic Computing Ltd
 */
public final class PageTest extends TestCase {

    /**
     * Test.
     */
    public void testToJson() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Foo?");
        page.displayTemplateName(new Template("template", "", ""));
        page.addParagraph("bar", new Paragraph("bar"));
        page.addParagraph("baz", new Paragraph("baz"));

        // ACT
        final String jsonString = page.toJSON();

        // ASSERT
        assertEquals(
            "{\"id\": \""
            + page.id().toString()
            + "\",\"title\": \"Foo?\""
            + ",\"displayTemplateName\": \"template\""
            + ",\"paragraphs\": "
            + "{\"bar\": {\"body\": \"bar\"},\"baz\": {\"body\": \"baz\"}}}",
            jsonString);
    }

    /**
     * Test.
     */
    public void testConstructorCanGenerateName() {

        // ARRANGE
        final ResourceName name = new ResourceName("foo");

        // ACT
        final Page page = new Page(name);

        // ASSERT
        assertEquals(name.toString(), page.title());
        assertEquals(name, page.name());
    }

    /**
     * Test.
     */
    public void testAddNewParagraph() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");

        // ACT
        page.addParagraph("header", new Paragraph("<H1>Header</H1>"));

        // Assert
        assertEquals(1, page.paragraphs().size());

    }

    /**
     * Test.
     */
    public void testDeleteParagraph() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        page.addParagraph("header", new Paragraph("<H1>Header</H1>"));
        page.addParagraph("footer", new Paragraph("<H1>Footer</H1>"));

        // ACT
        page.deleteParagraph("header");

        // Assert
        assertEquals(1, page.paragraphs().size());
        assertEquals(
            "<H1>Footer</H1>",
            page.paragraphs().get("footer").body());
    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyNames() {
        // ACT
        try {
            new Page(null);
            fail("Resources should reject NULL for the name parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testDeleteAllParagraphs() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        page.addParagraph("header", new Paragraph("<H1>Header</H1>"));
        page.addParagraph("footer", new Paragraph("<H1>Footer</H1>"));

        // ACT
        page.deleteAllParagraphs();

        // ASSERT
        assertEquals(0, page.paragraphs().size());
    }
}
