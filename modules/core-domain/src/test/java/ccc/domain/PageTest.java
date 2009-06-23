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

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.commons.Exceptions;


/**
 * Tests for the {@link Page} class.
 *
 * @author Civic Computing Ltd
 */
public final class PageTest extends TestCase {

    /**
     * Test.
     */
    public void testNewPageHasNoWorkingCopy() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");

        // ACT
        final boolean hasWC = page.hasWorkingCopy();

        // ASSERT
        assertFalse(hasWC);
    }

    /**
     * Test.
     */
    public void testFindSpecificRevision() {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                Paragraph.fromText("header", "Header"));
        page.workingCopy(
            new PageDelta(
                "another title",
                Collections.singleton(
                    Paragraph.fromBoolean("meh", Boolean.TRUE))));
        page.applySnapshot();

        // ACT
        final PageRevision rev0 = page.revision(0);
        final PageRevision rev1 = page.revision(1);

        // ASSERT
        assertEquals(0, rev0.getIndex());
        assertEquals(1, rev0.getContent().size());
        assertEquals("Header", rev0.getContent().iterator().next().text());
        assertEquals(1, rev1.getIndex());
        assertEquals(1, rev1.getContent().size());
        assertEquals(Boolean.TRUE, rev1.getContent().iterator().next().bool());
    }

    /**
     * Test.
     */
    public void testFindCurrentRevision() {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                Paragraph.fromText("header", "Header"));

        // ACT
        final PageRevision rev = page.currentRevision();

        // ASSERT
        assertEquals(0, rev.getIndex());
        assertEquals(1, rev.getContent().size());
        assertEquals("Header", rev.getContent().iterator().next().text());
    }


    /**
     * Test.
     */
    public void testApplySnapshot() {

        // ARRANGE
        final Date then = new Date();
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                Paragraph.fromText("header", "Header"));
        final PageDelta s = page.createSnapshot();

        // ACT
        final Set<Paragraph> paras = new HashSet<Paragraph>(){{
            add(Paragraph.fromBoolean("A boolean", Boolean.TRUE));
            add(Paragraph.fromDate("A date", then));
        }};

        s.setParagraphs(paras);
        s.setTitle("new title");
        page.workingCopy(s);
        page.applySnapshot();

        // ASSERT
        assertEquals("new title", page.title());
        assertEquals(2, page.paragraphs().size());
        assertEquals(Boolean.TRUE, page.paragraph("A boolean").bool());
        final Date now = new Date();
        assertTrue(
            page.paragraph("A date").date().compareTo(now) <= 0);
    }

    /**
     * Test.
     */
    public void testUpdateWorkingCopy() { // FIXME

        // ARRANGE
        final Page page = new Page("foo");

        // ACT
        page.workingCopy(new PageDelta("title", new HashSet<Paragraph>()));

        // ASSERT
        assertEquals("foo", page.title()); // The page hasn't changed.
//        assertNull(page.workingCopy().getTitle());
    }

    /**
     * Test.
     */
    public void testClearWorkingCopy() { // FIXME

        // ARRANGE
        final Page page = new Page("foo");
        page.workingCopy(page.createSnapshot());

        // ACT
        page.clearWorkingCopy();

        // ASSERT
//        assertNull(page.workingCopy());
    }

    /**
     * Test.
     */
    public void testTakeSnapshot() {

        // ARRANGE
        final Paragraph header = Paragraph.fromText("header", "Header");
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                header);

        // ACT
        final PageDelta s = page.createSnapshot();

        // ASSERT
        assertEquals("Title", s.getTitle());
        assertEquals(1, s.getParagraphs().size());
        assertEquals("header", s.getParagraphs().iterator().next().name());
        assertEquals("Header", s.getParagraphs().iterator().next().text());
    }

    /**
     * Test.
     */
    public void testTakeSnapshotWithNoParagraphs() {

        // ARRANGE
        final Page page = new Page(new ResourceName("foo"), "Title");

        // ACT
        final PageDelta s = page.createSnapshot();

        // ASSERT
        assertEquals("Title", s.getTitle());
        assertEquals(0, s.getParagraphs().size());
    }

    /**
     * Test.
     */
    public void testParagraphsCanBeRetrievedByName() {

        // ARRANGE
        final Paragraph header = Paragraph.fromText("header", "Header");
        final Page page =
            new Page(new ResourceName("foo"), "Title", null, header);

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
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                Paragraph.fromText("header", "<H1>Header</H1>"));

        // ACT
        try {
            page.paragraphs().add(Paragraph.fromText("foo", "aaa"));
            fail("Should be rejected.");

        // ASSERT
        } catch (final RuntimeException e) {
            Exceptions.swallow(e);
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

        // ACT
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                Paragraph.fromText("header", "<H1>Header</H1>"));

        // Assert
        assertEquals(1, page.paragraphs().size());

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
    public void testAdd32NewParagraphs() {

        // ARRANGE
        final Paragraph[] paras = new Paragraph[Page.MAXIMUM_PARAGRAPHS];
        for (int a=0; a < Page.MAXIMUM_PARAGRAPHS; a++) {
            paras[a] =
                Paragraph.fromText("header"+a, "<H1>Header"+a+"</H1>");
        }

        // ACT
        final Page page =
            new Page(new ResourceName("foo"), "Title", null, paras);

        // ASSERT
        assertEquals(Page.MAXIMUM_PARAGRAPHS, page.paragraphs().size());

    }

    /**
     * Test.
     */
    public void testAdd33NewParagraphs() {

        // ARRANGE
        final Paragraph[] paras = new Paragraph[Page.MAXIMUM_PARAGRAPHS+1];
        for (int a=0; a < Page.MAXIMUM_PARAGRAPHS+1; a++) {
            paras[a] =
                Paragraph.fromText("header"+a, "<H1>Header"+a+"</H1>");
        }

        // ACT
        try {
            final Page page =
                new Page(new ResourceName("foo"), "Title", null, paras);
            fail("Resources should reject adding more than 32 paragraphs.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be under 32.", e.getMessage());
        }

    }
}
