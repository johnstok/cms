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
package ccc.content.server;

import java.util.regex.Matcher;

import junit.framework.TestCase;


/**
 * Tests for the {@link BrokenLinkServlet} class. 
 *
 * @author Civic Computing Ltd.
 */
public class BrokenLinkServletTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testPagesMatchedCorrectly() {

        // ARRANGE

        // ACT
        final Matcher m =
            BrokenLinkServlet.PAGE_PATTERN.matcher("/1234.html");

        // ASSERT
        assertTrue(m.matches());
        assertEquals("1234", m.group(1));
    }

    /**
     * Test.
     */
    public void testFilesMatchedCorrectly() {

        // ARRANGE

        // ACT
        final Matcher m =
            BrokenLinkServlet.FILE_PATTERN.matcher("/files/foo.pdf");

        // ASSERT
        assertTrue(m.matches());
        assertEquals("foo.pdf", m.group(1));
    }
}
