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

package ccc.api.types;

import java.util.List;

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
    public void testParentReturnsNullForEmptyPath() {

        // ARRANGE

        // ACT
        final ResourcePath p = new ResourcePath().parent();

        // ASSERT
        assertNull(p);
    }

    /**
     * Test.
     */
    public void testParentMethodRemovesLeafElement() {

        // ARRANGE

        // ACT
        final ResourcePath p = new ResourcePath("/foo/bar/baz").parent();

        // ASSERT
        assertEquals(new ResourcePath("/foo/bar"), p);
    }

    /**
     * Test.
     */
    public void testConstructorRejectsInvalidStrings() {

        // ARRANGE

        // ACT
        try {
            new ResourcePath("\\hello\\world");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "\\hello\\world does not match the regular expression:"
                    + " (/[\\.\\-\\w]+)*",
                e.getMessage());
        }


    }

    /**
     * Test.
     */
    public void testTopReturnsNullForAnEmptyPath() {

        // ARRANGE
        final ResourcePath path = new ResourcePath("");

        // ACT

        // ASSERT
        assertNull(path.top());
    }

    /**
     * Test.
     */
    public void testRemoveTop() {

        // ARRANGE
        final ResourcePath path = new ResourcePath("/foo/bar/baz");

        // ACT
        final ResourcePath noTop = path.removeTop();

        // ASSERT
        assertEquals(new ResourcePath("/bar/baz"), noTop);
    }

    /**
     * Test.
     */
    public void testGetTopOfTheResourcePath() {

        // ARRANGE
        final ResourcePath path = new ResourcePath("/foo/bar/baz");

        // ACT
        final ResourceName name = path.top();

        // ASSERT
        assertEquals(new ResourceName("foo"), name);
    }

    /**
     * Test.
     */
    public void testEmptyPathsAreAllowed() {

        // ACT
        final ResourcePath empty = new ResourcePath();

        // ASSERT
        assertEquals(0, empty.elements().size());
    }

    /**
     * Test.
     */
    public void testAppendReturnsNewInstances() {

        // ARRANGE
        final ResourcePath path = new ResourcePath();

        // ACT
        final ResourcePath firstPath = path.append(new ResourceName("f.oo"));
        final ResourcePath secondPath =
            firstPath.append(new ResourceName("ba_r"));

        // ASSERT
        assertEquals("", path.toString());
        assertEquals("/f.oo", firstPath.toString());
        assertEquals("/f.oo/ba_r", secondPath.toString());
    }

    /**
     * Test.
     */
    public void testParsingOfStringToPath() {

        // ARRANGE
        final String pathString = "/fo_o/b.ar/baz";

        // ACT
        final ResourcePath path = new ResourcePath(pathString);

        // ASSERT
        assertEquals(pathString, path.toString());
    }

    /**
     * Test.
     *
     */
    public void testCreatePathFromResourceName() {

        // ARRANGE
        final ResourceName name = new ResourceName("foo");

        // ACT
        final ResourcePath path = new ResourcePath(name);

        // ASSERT
        assertEquals("/foo", path.toString());
    }

    /**
     * Test.
     *
     */
    public void testStringConstructorRejectsNull() {

        // ACT
        try {
            new ResourcePath((String) null);
            fail("Should reject NULL.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     *
     */
    public void testConstructorRejectsNull() {

        // ACT
        try {
            new ResourcePath((ResourceName) null);
            fail("Should reject NULL.");

            // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Tests element path subset.
     *
     */
    public void testElementsToTop() {

        // ARRANGE
        final ResourcePath rootPath = new ResourcePath();
        final ResourcePath path = new ResourcePath("/first/second");

        // ACT
        final List<ResourceName> rootElements = rootPath.elementsToTop();
        final List<ResourceName> elements = path.elementsToTop();

        // ASSERT
        assertEquals(0, rootElements.size());
        assertEquals(1, elements.size());
    }

    /**
     * Test equals.
     */
    public void testEquals() {

        // ARRANGE
        final ResourcePath p1 = new ResourcePath("/p");
        final ResourcePath p2 = new ResourcePath("/p");

        // ACT
        final boolean areEqual = p1.equals(p2);

        // ASSERT
        assertTrue("Should be true.", areEqual);
        assertTrue(p1.equals(p1));
        assertFalse(p1.equals(new ResourcePath("/q")));
        assertFalse(p1.equals(new Object()));
        assertFalse(p1.equals(null));
    }
}
