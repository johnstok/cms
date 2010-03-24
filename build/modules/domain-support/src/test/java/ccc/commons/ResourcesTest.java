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
import java.util.List;

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

    /**
     * Test.
     */
    public void testReadIntoProps() {

        // ARRANGE

        // ACT
        final java.util.Properties actual =
            Resources.readIntoProps("ccc/commons/simple.properties");

        // ASSERT
        assertEquals("bar", actual.get("foo"));
    }

    /**
     * Test.
     */
    public void testReadIntoList() {

        // ARRANGE

        // ACT
        final List<String> actual =
            Resources.readIntoList(
                "ccc/commons/simple.txt", Charset.forName("UTF-8"));

        // ASSERT
        assertEquals(2, actual.size());
        assertEquals("hello", actual.get(0));
        assertEquals("world", actual.get(1));
    }
}
