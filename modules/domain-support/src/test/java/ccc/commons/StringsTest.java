/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import junit.framework.TestCase;


/**
 * Tests for the {@link Strings} class.
 *
 * @author Civic Computing Ltd.
 */
public class StringsTest
    extends
        TestCase {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testEscapeString() throws Exception {

        // ARRANGE
        final String foo = "\"foo%";

        // ACT
        final String escaped =
            foo.replace("%",  Strings.hex('%', "utf-8"))
               .replace("\"", Strings.hex('"', "utf-8"));

        // ASSERT
        assertEquals(escaped, "%22foo%25");
    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHexPercent() throws Exception {

        // ARRANGE

        // ACT

        // ASSERT
        assertEquals("%25", Strings.hex('%', "utf-8"));
    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHexQuote() throws Exception {

        // ARRANGE

        // ACT

        // ASSERT
        assertEquals("%22", Strings.hex('"', "iso-8859-1"));
    }

    /**
     * Test.
     */
    public void testNvl() {

        // ARRANGE
        final String foo = null;

        // ACT
        final String actual = Strings.nvl(foo, "/");

        // ASSERT
        assertEquals("/", actual);
    }

    /**
     * Test.
     */
    public void testRemoveTrailingRemoves() {

        // ARRANGE
        final String foo = "foo//";

        // ACT
        final String actual = Strings.removeTrailing('/', foo);

        // ASSERT
        assertEquals("foo/", actual);
    }

    /**
     * Test.
     */
    public void testRemoveTrailingCanCreateZls() {

        // ARRANGE
        final String foo = "/";

        // ACT
        final String actual = Strings.removeTrailing('/', foo);

        // ASSERT
        assertEquals("", actual);
    }

    /**
     * Test.
     */
    public void testRemoveTrailingDoesNothing() {

        // ARRANGE
        final String foo = "foo";

        // ACT
        final String actual = Strings.removeTrailing('/', foo);

        // ASSERT
        assertEquals("foo", actual);
    }
}
