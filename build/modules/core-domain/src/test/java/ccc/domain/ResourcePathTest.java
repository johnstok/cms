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

import java.util.List;

import junit.framework.TestCase;


/**
 * Tests for {@link ResourcePath}.
 * TODO: Check for null input to constructor.
 *
 * @author Civic Computing Ltd
 */
public final class ResourcePathTest extends TestCase {

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
    }
}
