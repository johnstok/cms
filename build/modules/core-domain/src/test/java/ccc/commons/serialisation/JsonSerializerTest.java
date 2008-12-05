/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons.serialisation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


/**
 * Tests for the {@link JsonSerializer} class.
 *
 * @author Civic Computing Ltd.
 */
public class JsonSerializerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSerialiseNamedArray() {

        // ARRANGE
        final List<String> strings = new ArrayList<String>(){{
            add("foo");
            add("bar");
        }};
        final JsonSerializer ser = new JsonSerializer();

        // ACT
        ser.array("name", strings);
        final String actual = ser.toString();

        // ASSERT
        assertEquals("\"name\":[\"foo\",\"bar\"],", actual);
    }

    /**
     * Test.
     */
    public void testSpecialCharactersEscaped() {

        // ARRANGE
        final String specialChars = "\" \\ / \b \f \n \r \t";

        // ACT
        final String jsonOutput = new JsonSerializer().escape(specialChars);

        // ASSERT
        assertEquals("\\\" \\\\ \\/ \\b \\f \\n \\r \\t", jsonOutput);
    }

    /**
     * Test.
     */
    public void testAsciiControlCharactersEscaped() {

        // ARRANGE
        final String controlChars = "\u0000 \u0001 \u0010 \u001f";

        // ACT
        final String jsonOutput = new JsonSerializer().escape(controlChars);

        // ASSERT
        assertEquals("\\u0000 \\u0001 \\u0010 \\u001F", jsonOutput);
    }

    /**
     * Test.
     */
    public void testSmallNumbersEscapedCorrectly() {

        // ARRANGE
        final double smallNumber = 0.00000000567d;

        // ACT
        final String jsonOutput = new JsonSerializer().escape(smallNumber);

        // ASSERT
        assertEquals("5.67E-9", jsonOutput);
    }

    /**
     * Test.
     */
    public void testLargeNumbersEscapedCorrectly() {

        // ARRANGE
        final double largeNumber = -56700000000d;

        // ACT
        final String jsonOutput = new JsonSerializer().escape(largeNumber);

        // ASSERT
        assertEquals("-5.67E10", jsonOutput);
    }
}
