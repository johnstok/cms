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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import junit.framework.TestCase;


/**
 * Tests for the {@link LinkFixer} class.
 *
 * @author Civic Computing Ltd.
 */
public class LinkFixerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testFixParagraph() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");
        final Map<String, StringBuffer> paras =
            new HashMap<String, StringBuffer>();
        paras.put(
            "foo",
            new StringBuffer(
                "<a href =\"mailto:kwj@civic.com\">kwj</a>"
                + "< href= \"1234.html\" />"));

        // ACT
        lf.extractURLs(paras);

        // ASSERT
        assertEquals(
            "<a href =\"mailto:kwj@civic.com\">kwj</a>"
            + "< href=\"ash/1234.html\" />",
            paras.get("foo").toString());
    }

    /**
     * Test.
     */
    public void testFixImageUrls() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");
        final Map<String, StringBuffer> paras =
            new HashMap<String, StringBuffer>();
        paras.put(
            "foo",
            new StringBuffer("< src= \"images/new.gif\" />"));

        // ACT
        lf.extractURLs(paras);

        // ASSERT
        assertEquals(
            "< src=\"ash/images/new.gif\" />", paras.get("foo").toString());
    }

    /**
     * Test.
     */
    public void testFixFileUrls() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");
        final Map<String, StringBuffer> paras =
            new HashMap<String, StringBuffer>();
        paras.put(
            "foo",
            new StringBuffer("< href= \"files/fo$o.pdf\" />"));

        // ACT
        lf.extractURLs(paras);

        // ASSERT
        assertEquals(
            "< href=\"ash/files/fo$o.pdf\" />", paras.get("foo").toString());
    }

    /**
     * Test.
     */
    public void testFindHrefAttributes() {

        // ARRANGE
        final String anchor =
            "<a href =  \"5240.html\">A page</a>"
            + "<a href\t=\n\"5241.html\">Another page</a>";

        // ACT
        final Matcher m = LinkFixer.HREF_PATTERN.matcher(anchor);

        // ASSERT
        assertTrue(m.find());
        assertEquals("5240.html", m.group(1));
        assertTrue(m.find());
        assertEquals("5241.html", m.group(1));
    }

    /**
     * Test.
     */
    public void testFixAbsoluteUrls() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");
        final Map<String, StringBuffer> paras =
            new HashMap<String, StringBuffer>();
        paras.put(
            "foo",
            new StringBuffer("<a href=\"/ash/1234.html\">kwj</a>"));

        // ACT
        lf.extractURLs(paras);

        // ASSERT
        assertEquals(
            "<a href=\"ash/1234.html\">kwj</a>",
            paras.get("foo").toString());
    }

    /**
     * Test.
     */
    public void testCorrectHandlesOldPageLinksA() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");

        // ACT
        final String corrected =
            lf.correct("ash_display.jsp?pContentID=4782&amp;p_applic=CCC");

        // ASSERT
        assertEquals("ash/4782.html", corrected);
    }

    /**
     * Test.
     */
    public void testCorrectHandlesOldPageLinksB() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");

        // ACT
        final String corrected =
            lf.correct("servlet/controller?pContentID=4264&amp;p_applic=CCC");

        // ASSERT
        assertEquals("ash/4264.html", corrected);
    }

    /**
     * Test.
     */
    public void testCorrectHandlesPageLinks() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");

        // ACT
        final String corrected = lf.correct("3481.html");

        // ASSERT
        assertEquals("ash/3481.html", corrected);
    }

    /**
     * Test.
     */
    public void testCorrectHandlesComplexPageLinks() {

        // ARRANGE
        final LinkFixer lf = new LinkFixer("ash/", "foo");

        // ACT
        final String corrected = lf.correct("3481.7.1071.html");

        // ASSERT
        assertEquals("ash/3481.html", corrected);
    }
}
