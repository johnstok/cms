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
package ccc.migration;

import junit.framework.TestCase;

import org.htmlcleaner.TagNode;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LinkFixerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCorrectHandlesOldPageLinksA() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer();
        final TagNode tn = new TagNode("a");

        // ACT
        lf.correct(tn, "ash_display.jsp?pContentID=4782&amp;p_applic=CCC&amp;p_service=Content.show&amp;");

        // ASSERT
        assertEquals("/ash/4782.html", tn.getAttributeByName("href"));
    }

    /**
     * Test.
     */
    public void testCorrectHandlesOldPageLinksB() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer();
        final TagNode tn = new TagNode("a");

        // ACT
        lf.correct(tn, "servlet/controller?p_service=Content.show&amp;p_applic=CCC&amp;pContentID=4264");

        // ASSERT
        assertEquals("/ash/4264.html", tn.getAttributeByName("href"));
    }

    /**
     * Test.
     */
    public void testCorrectHandlesPageLinks() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer();
        final TagNode tn = new TagNode("a");

        // ACT
        lf.correct(tn, "3481.html");

        // ASSERT
        assertEquals("/ash/3481.html", tn.getAttributeByName("href"));
    }

    /**
     * Test.
     */
    public void testCorrectHandlesComplexPageLinks() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer();
        final TagNode tn = new TagNode("a");

        // ACT
        lf.correct(tn, "3481.7.1071.html");

        // ASSERT
        assertEquals("/ash/3481.html", tn.getAttributeByName("href"));
    }
}
