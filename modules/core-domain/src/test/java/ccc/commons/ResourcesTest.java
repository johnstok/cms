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
package ccc.commons;

import java.nio.charset.Charset;

import junit.framework.TestCase;


/**
 * Tests for the resources class.
 *
 * TODO: Test that readIntoString() closes in-memory stream under error.
 *
 * @author Civic Computing Ltd
 */
public final class ResourcesTest extends TestCase {

    /**
     * Test.
     */
    public void testReadIntoMemory() {

        // ARRANGE
        final String expected = "hello\r\nworld";

        // ACT
        final String actual =
            Resources.readIntoString(
                getClass().getResource("simple.txt"),
                Charset.forName("ISO-8859-1"));

        // ASSERT
        assertEquals(expected, actual);
    }

}
