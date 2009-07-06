/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.velocity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.Paragraph;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.snapshots.PageSnapshot;
import ccc.snapshots.ResourceSnapshot;


/**
 * Tests for the {@link VelocityHelper} class.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityHelperTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testPath() {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final String expected = ""+Calendar.getInstance().get(Calendar.YEAR);

        // ACT
        final String year = helper.currentYear();

        // ASSERT
        assertEquals(expected, year);
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testSelectPathElements() throws RemoteExceptionSupport {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page =
            new Page(new ResourceName("page"), "page", null, _rm);
        final Page page2 =
            new Page(new ResourceName("page2"), "page2", null, _rm);
        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<ResourceSnapshot> list =
            helper.selectPathElements(page.forCurrentRevision());

        // ASSERT
        assertEquals(3, list.size());
        assertEquals(root.id(), list.get(0).id());
        assertEquals(page.id(), list.get(2).id());
        assertEquals(page2, list.get(2).parent().entries().get(1));

    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testSelectPagesForContentIndex() throws RemoteExceptionSupport {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page =
            new Page(
                new ResourceName("page"),
                "page",
                null,
                _rm,
                Paragraph.fromText("HEADER", "headertext"));

        final Page page2 =
            new Page(
                new ResourceName("page2"),
                "page2",
                null,
                _rm,
                Paragraph.fromText("HEADER", "headertext2"));

        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<PageSnapshot> result =
            helper.selectPagesForContentIndex(
                folder.forCurrentRevision(), 1);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("headertext",
            result.get(0).paragraph("HEADER").text());
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testSelectPagesForContentIndexNoLimit()
    throws RemoteExceptionSupport {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page =
            new Page(
                new ResourceName("page"),
                "page",
                null,
                _rm,
                Paragraph.fromText("HEADER", "headertext"));

        final Page page2 =
            new Page(
                new ResourceName("page2"),
                "page2",
                null,
                _rm,
                Paragraph.fromText("HEADER", "headertext2"));

        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<PageSnapshot> result =
            helper.selectPagesForContentIndex(
                folder.forCurrentRevision(), -1);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("headertext",
            result.get(0).paragraph("HEADER").text());
        assertEquals("headertext2",
            result.get(1).paragraph("HEADER").text());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
