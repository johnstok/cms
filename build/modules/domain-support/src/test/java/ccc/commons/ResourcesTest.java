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
