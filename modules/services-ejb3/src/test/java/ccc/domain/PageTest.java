/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.api.core.Page;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.commons.Exceptions;


/**
 * Tests for the {@link PageEntity} class.
 *
 * @author Civic Computing Ltd
 */
public final class PageTest extends TestCase {

    /**
     * Test.
     */
    public void testNewPageHasNoWorkingCopy() {

        // ARRANGE
        final PageEntity page = new PageEntity("Title", _rm);

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
        final PageEntity page =
            new PageEntity(
                new ResourceName("foo"),
                "Title",
                null,
                _rm,
                Paragraph.fromText("header", "Header"));
        page.setOrUpdateWorkingCopy(
            Page.delta(
                Collections.singleton(
                    Paragraph.fromBoolean("meh", Boolean.TRUE))));
        page.applyWorkingCopy(
            new RevisionMetadata(
                new Date(), UserEntity.SYSTEM_USER, true, "Updated."));

        // ACT
        final PageRevision rev0 = page.revision(0);
        final PageRevision rev1 = page.revision(1);

        // ASSERT
        assertEquals(1, rev0.getParagraphs().size());
        assertEquals(
            "Header", rev0.getParagraphs().iterator().next().getText());
        assertEquals(1, rev1.getParagraphs().size());
        assertEquals(
            Boolean.TRUE, rev1.getParagraphs().iterator().next().getBoolean());
    }

    /**
     * Test.
     */
    public void testFindCurrentRevision() {

        // ARRANGE
        final PageEntity page =
            new PageEntity(
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
        final PageEntity page =
            new PageEntity(
                new ResourceName("foo"),
                "Title",
                null,
                _rm,
                Paragraph.fromText("header", "Header"));
        final Page s = page.forCurrentRevision();

        // ACT
        final Set<Paragraph> paras = new HashSet<Paragraph>(){{
            add(Paragraph.fromBoolean("A boolean", Boolean.TRUE));
            add(Paragraph.fromDate("A date", then));
        }};

        s.setParagraphs(paras);
        page.setOrUpdateWorkingCopy(s);
        page.applyWorkingCopy(
            new RevisionMetadata(
                new Date(), UserEntity.SYSTEM_USER, true, "Updated."));

        // ASSERT
        assertEquals(2, page.currentRevision().getParagraphs().size());
        assertEquals(
            Boolean.TRUE,
            page.currentRevision().getParagraph("A boolean").getBoolean());
        final Date now = new Date();
        assertTrue(
            page.currentRevision()
                .getParagraph("A date")
                .getDate()
                .compareTo(now)
            <= 0);
    }

    /**
     * Test.
     */
    public void testUpdateWorkingCopy() { // TODO: Enable assertions.

        // ARRANGE
        final PageEntity page = new PageEntity("foo", _rm);

        // ACT
        page.setOrUpdateWorkingCopy(new Page());

        // ASSERT
        assertEquals("foo", page.getTitle()); // The page hasn't changed.
//        assertNull(page.workingCopy().getTitle());
    }

    /**
     * Test.
     */
    public void testClearWorkingCopy() { // TODO: Enable assertions.

        // ARRANGE
        final PageEntity page = new PageEntity("foo", _rm);
        page.setOrUpdateWorkingCopy(page.forCurrentRevision());

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
        final PageEntity page =
            new PageEntity(
                new ResourceName("foo"),
                "Title",
                null,
                _rm,
                header);

        // ACT
        final Page s = page.forCurrentRevision();

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
        final PageEntity page =
            new PageEntity("Title", _rm);

        // ACT
        final Page s = page.forCurrentRevision();

        // ASSERT
        assertEquals(0, s.getParagraphs().size());
    }

    /**
     * Test.
     */
    public void testParagraphsCanBeRetrievedByName() {

        // ARRANGE
        final Paragraph header = Paragraph.fromText("header", "Header");
        final PageEntity page =
            new PageEntity(
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
        final PageEntity page =
            new PageEntity(
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
        final PageEntity page = new PageEntity("foo", _rm);

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
        final PageEntity page =
            new PageEntity(
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
            new PageEntity(null, _rm);
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
        final Paragraph[] paras = new Paragraph[Template.MAXIMUM_PARAGRAPHS];
        for (int a=0; a < Template.MAXIMUM_PARAGRAPHS; a++) {
            paras[a] =
                Paragraph.fromText("header"+a, "<H1>Header"+a+"</H1>");
        }

        // ACT
        final PageEntity page =
            new PageEntity(new ResourceName("foo"), "Title", null, _rm, paras);

        // ASSERT
        assertEquals(
            Template.MAXIMUM_PARAGRAPHS,
            page.currentRevision().getParagraphs().size());

    }

    /**
     * Test.
     */
    public void testAdd33NewParagraphs() {

        // ARRANGE
        final Paragraph[] paras =
            new Paragraph[Template.MAXIMUM_PARAGRAPHS+1];
        for (int a=0; a < Template.MAXIMUM_PARAGRAPHS+1; a++) {
            paras[a] =
                Paragraph.fromText("header"+a, "<H1>Header"+a+"</H1>");
        }

        // ACT
        try {
            new PageEntity(new ResourceName("foo"), "Title", null, _rm, paras);
            fail("Resources should reject adding more than 32 paragraphs.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be under 32.", e.getMessage());
        }
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(
            new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
