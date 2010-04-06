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
import ccc.commons.Exceptions;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


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
        final Page page = new Page("Title", _rm);

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
                _rm,
                Paragraph.fromText("header", "Header"));
        page.setOrUpdateWorkingCopy(
            new PageDelta(
                Collections.singleton(
                    Paragraph.fromBoolean("meh", Boolean.TRUE))));
        page.applyWorkingCopy(
            new RevisionMetadata(
                new Date(), User.SYSTEM_USER, true, "Updated."));

        // ACT
        final PageRevision rev0 = page.revision(0);
        final PageRevision rev1 = page.revision(1);

        // ASSERT
        assertEquals(1, rev0.getParagraphs().size());
        assertEquals("Header", rev0.getParagraphs().iterator().next().getText());
        assertEquals(1, rev1.getParagraphs().size());
        assertEquals(
            Boolean.TRUE, rev1.getParagraphs().iterator().next().getBoolean());
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
                _rm,
                Paragraph.fromText("header", "Header"));

        // ACT
        final PageRevision rev = page.currentRevision();

        // ASSERT
        assertEquals(1, rev.getParagraphs().size());
        assertEquals("Header", rev.getParagraphs().iterator().next().getText());
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
                _rm,
                Paragraph.fromText("header", "Header"));
        final PageDelta s = page.createSnapshot();

        // ACT
        final Set<Paragraph> paras = new HashSet<Paragraph>(){{
            add(Paragraph.fromBoolean("A boolean", Boolean.TRUE));
            add(Paragraph.fromDate("A date", then));
        }};

        s.setParagraphs(paras);
        page.setOrUpdateWorkingCopy(s);
        page.applyWorkingCopy(
            new RevisionMetadata(
                new Date(), User.SYSTEM_USER, true, "Updated."));

        // ASSERT
        assertEquals(2, page.currentRevision().getParagraphs().size());
        assertEquals(
            Boolean.TRUE,
            page.currentRevision().getParagraph("A boolean").getBoolean());
        final Date now = new Date();
        assertTrue(
            page.currentRevision().getParagraph("A date").getDate().compareTo(now)
            <= 0);
    }

    /**
     * Test.
     */
    public void testUpdateWorkingCopy() { // TODO: Enable assertions.

        // ARRANGE
        final Page page = new Page("foo", _rm);

        // ACT
        page.setOrUpdateWorkingCopy(new PageDelta(new HashSet<Paragraph>()));

        // ASSERT
        assertEquals("foo", page.getTitle()); // The page hasn't changed.
//        assertNull(page.workingCopy().getTitle());
    }

    /**
     * Test.
     */
    public void testClearWorkingCopy() { // TODO: Enable assertions.

        // ARRANGE
        final Page page = new Page("foo", _rm);
        page.setOrUpdateWorkingCopy(page.createSnapshot());

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
                _rm,
                header);

        // ACT
        final PageDelta s = page.createSnapshot();

        // ASSERT
        assertEquals(1, s.getParagraphs().size());
        assertEquals("header", s.getParagraphs().iterator().next().getName());
        assertEquals("Header", s.getParagraphs().iterator().next().getText());
    }

    /**
     * Test.
     */
    public void testTakeSnapshotWithNoParagraphs() {

        // ARRANGE
        final Page page =
            new Page("Title", _rm);

        // ACT
        final PageDelta s = page.createSnapshot();

        // ASSERT
        assertEquals(0, s.getParagraphs().size());
    }

    /**
     * Test.
     */
    public void testParagraphsCanBeRetrievedByName() {

        // ARRANGE
        final Paragraph header = Paragraph.fromText("header", "Header");
        final Page page =
            new Page(
                new ResourceName("foo"),
                "Title",
                null,
                _rm,
                header);

        // ACT
        final Paragraph p = page.currentRevision().getParagraph("header");

        // ASSERT
        assertEquals(header.getText(), p.getText());
        assertEquals(header, p);
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
                _rm,
                Paragraph.fromText("header", "<H1>Header</H1>"));

        // ACT
        try {
            page.currentRevision().getParagraphs().add(
                Paragraph.fromText("foo", "aaa"));
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
        final Page page = new Page("foo", _rm);

        // ASSERT
        assertEquals("foo", page.getTitle());
        assertEquals(new ResourceName("foo"), page.getName());
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
                _rm,
                Paragraph.fromText("header", "<H1>Header</H1>"));

        // Assert
        assertEquals(1, page.currentRevision().getParagraphs().size());

    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyTitles() {
        // ACT
        try {
            new Page(null, _rm);
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
            new Page(new ResourceName("foo"), "Title", null, _rm, paras);

        // ASSERT
        assertEquals(
            Page.MAXIMUM_PARAGRAPHS,
            page.currentRevision().getParagraphs().size());

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
            new Page(new ResourceName("foo"), "Title", null, _rm, paras);
            fail("Resources should reject adding more than 32 paragraphs.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be under 32.", e.getMessage());
        }
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
