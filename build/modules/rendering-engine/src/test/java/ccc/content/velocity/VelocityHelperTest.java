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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ccc.api.Paragraph;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;


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
     *
     * @throws ResourceExistsException If a folder already contains a page.
     */
    public void testSelectPathElements() throws ResourceExistsException {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page = new Page("page");
        final Page page2 = new Page("page2");
        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<Resource> list = helper.selectPathElements(page);

        // ASSERT
        assertEquals(3, list.size());
        assertEquals(root, list.get(0));
        assertEquals(page, list.get(2));
        assertEquals(page2, list.get(2).parent().entries().get(1));

    }

    /**
     * Test.
     *
     * @throws ResourceExistsException If a folder already contains a page.
     */
    public void testSelectPagesForContentIndex()
                                                throws ResourceExistsException {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page = new Page("page");
        final Paragraph p1 = Paragraph.fromText("HEADER", "headertext");
        page.addParagraph(p1);

        final Page page2 = new Page("page2");
        final Paragraph p2 = Paragraph.fromText("HEADER", "headertext2");
        page2.addParagraph(p2);

        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<String> contentElements = new ArrayList<String>();
        contentElements.add("HEADER");
        final List<Map<String, String>> result =
            helper.selectPagesForContentIndex(folder, 1, contentElements);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("headertext", result.get(0).get("HEADER"));
    }


    /**
     * Test.
     *
     * @throws ResourceExistsException If a folder already contains a page.
     */
    public void testSelectPagesForContentIndexNoLimit()
    throws ResourceExistsException {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final Page page = new Page("page");
        final Paragraph p1 = Paragraph.fromText("HEADER", "headertext");
        page.addParagraph(p1);

        final Page page2 = new Page("page2");
        final Paragraph p2 = Paragraph.fromText("HEADER", "headertext2");
        page2.addParagraph(p2);

        final Folder folder = new Folder("folder");
        final Folder root = new Folder("root");
        root.add(folder);
        folder.add(page);
        folder.add(page2);

        // ACT
        final List<String> contentElements = new ArrayList<String>();
        contentElements.add("HEADER");
        final List<Map<String, String>> result =
            helper.selectPagesForContentIndex(folder, -1, contentElements);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("headertext", result.get(0).get("HEADER"));
        assertEquals("headertext2", result.get(1).get("HEADER"));
    }
}
