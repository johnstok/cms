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
package ccc.commons.jee;

import java.io.IOException;
import java.io.InputStream;

import ccc.commons.XHTML;

import junit.framework.TestCase;


/**
 * Tests for the {@link XHTML} class.
 *
 * TODO Add tests for printErrors().
 *
 * @author Civic Computing Ltd
 */
public final class XHTMLTest extends TestCase {

    /**
     * Test.
     *
     * @throws IOException If the server cannot be reached.
     */
    public void testIsValid() throws IOException {

        // ARRANGE
        final InputStream page =
            getClass()
                .getResource("minimal-strict-document.xhtml")
                .openStream();

        // ACT
        final boolean isValid = XHTML.isValid(page);

        // ASSERT
        assertTrue("isValid found a valid document to be invalid.", isValid);
    }

    /**
     * Test.
     *
     * @throws IOException If the server cannot be reached.
     */
    public void testEvaluateXpath() throws IOException {

        // ARRANGE
        final InputStream page =
            getClass()
                .getResource("minimal-strict-document.xhtml")
                .openStream();

        // ACT
        final String titleCount =
            XHTML.evaluateXPath(page, "count(//xhtml:title)");

        // ASSERT
        assertEquals("1", titleCount);
    }
}
