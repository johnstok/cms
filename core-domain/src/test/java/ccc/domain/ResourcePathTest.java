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

package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for {@link ResourcePath}.
 *
 * @author Civic Computing Ltd
 */
public final class ResourcePathTest extends TestCase {

    /**
     * Test.
     */
    public void testAppendReturnsNewInstances() {

        // ARRANGE
        ResourcePath path = new ResourcePath();

        // ACT
        ResourcePath firstPath = path.append(new ResourceName("foo"));
        ResourcePath secondPath = firstPath.append(new ResourceName("bar"));

        // ASSERT
        assertEquals("", path.toString());
        assertEquals("/foo", firstPath.toString());
        assertEquals("/foo/bar", secondPath.toString());
    }

    /**
     * Test.
     */
    public void testParsingOfStringToPath() {

        // ARRANGE
        String pathString = "/foo/bar/baz";

        // ACT
        ResourcePath path = new ResourcePath(pathString);

        // ASSERT
        assertEquals(pathString, path.toString());
    }
}
