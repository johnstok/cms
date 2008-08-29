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
    public void testRootPathIsAllowed() {

        // ACT
        final ResourcePath root = new ResourcePath("/");

        // ASSERT
        assertEquals(0, root.elements().size());
    }

    /**
     * Test.
     */
    public void testAppendReturnsNewInstances() {

        // ARRANGE
        final ResourcePath path = new ResourcePath();

        // ACT
        final ResourcePath firstPath = path.append(new ResourceName("foo"));
        final ResourcePath secondPath =
            firstPath.append(new ResourceName("bar"));

        // ASSERT
        assertEquals("/", path.toString());
        assertEquals("/foo/", firstPath.toString());
        assertEquals("/foo/bar/", secondPath.toString());
    }

    /**
     * Test.
     */
    public void testParsingOfStringToPath() {

        // ARRANGE
        final String pathString = "/foo/bar/baz/";

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
        assertEquals("/foo/", path.toString());
    }

    /**
     * Tests element path subset.
     *
     */
    public void testElementsToTop() {

        // ARRANGE
        final ResourcePath rootPath = new ResourcePath();
        final ResourcePath path = new ResourcePath("/first/second/");

        // ACT
        final List<ResourceName> rootElements = rootPath.elementsToTop();
        final List<ResourceName> elements = path.elementsToTop();

        // ASSERT
        assertEquals(0, rootElements.size());
        assertEquals(1, elements.size());
    }

}
