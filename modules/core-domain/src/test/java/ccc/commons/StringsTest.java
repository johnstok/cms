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
package ccc.commons;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class StringsTest
    extends
        TestCase {

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
