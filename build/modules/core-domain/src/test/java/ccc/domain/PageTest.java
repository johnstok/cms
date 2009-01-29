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
 * @author Civic Computing Ltd
 */
public final class PageTest extends TestCase {

    /**
     * Test.
     */
    public void testTakeSnapshot() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        final Paragraph header = Paragraph.fromText("header", "Header");
        page.addParagraph(header);

        // ACT
        final Snapshot s = page.createSnapshot();

        // ASSERT
        assertEquals(
            "{\"title\":\"Title\","
            + "\"paragraphs\":[{\"text\":\"Header\",\"name\":\"header\"}]}",
            s.getDetail());
    }

    /**
     * Test.
     */
    public void testTakeSnapshotWithNoParagraphs() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");

        // ACT
        final Snapshot s = page.createSnapshot();

        // ASSERT
        assertEquals("{\"title\":\"Title\",\"paragraphs\":[]}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testParagraphsCanBeRetrievedByName() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        final Paragraph header = Paragraph.fromText("header", "Header");
        page.addParagraph(header);

        // ACT
        final Paragraph p = page.paragraph("header");

        // ASSERT
        assertSame(header, p);
    }

    /**
     * Test.
     */
    public void testParagraphsAccessorReturnsDefensiveCopy() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        page.addParagraph(Paragraph.fromText("header", "<H1>Header</H1>"));

        // ACT
        try {
            page.paragraphs().add(Paragraph.fromText("foo", "aaa"));
            fail("Should be rejected.");

        // ASSERT
        } catch (final RuntimeException e) {
            // no-op
        }
    }

    /**
     * Test.
     */
    public void testConstructorCanGenerateName() {
        // ACT
        final Page page = new Page("foo");

        // ASSERT
        assertEquals("foo", page.title());
        assertEquals(new ResourceName("foo"), page.name());
    }

    /**
     * Test.
     */
    public void testAddNewParagraph() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");

        // ACT
        page.addParagraph(Paragraph.fromText("header", "<H1>Header</H1>"));

        // Assert
        assertEquals(1, page.paragraphs().size());

    }

    /**
     * Test.
     */
    public void testDeleteParagraph() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");
        page.addParagraph(Paragraph.fromText("header", "<H1>Header</H1>"));
        page.addParagraph(Paragraph.fromText("footer", "<H1>Footer</H1>"));

        // ACT
        page.deleteParagraph("header");

        // Assert
        assertEquals(1, page.paragraphs().size());
        assertEquals(
            "<H1>Footer</H1>",
            page.paragraph("footer").text());
    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyTitles() {
        // ACT
        try {
            new Page(null);
            fail("Resources should reject NULL for the title parameter.");

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
        page.addParagraph(Paragraph.fromText("header", "<H1>Header</H1>"));
        page.addParagraph(Paragraph.fromText("footer", "<H1>Footer</H1>"));

        // ACT
        page.deleteAllParagraphs();

        // ASSERT
        assertEquals(0, page.paragraphs().size());
    }
}
