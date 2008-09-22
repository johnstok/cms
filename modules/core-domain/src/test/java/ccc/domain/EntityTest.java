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
package ccc.domain;

import java.util.UUID;

import junit.framework.TestCase;


/**
 * Tests for the {@link Entity} class.
 *
 * @author Civic Computing Ltd.
 */
public class EntityTest extends TestCase {

    /**
     * Test.
     */
    public void testEquals() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final Entity one = new Foo();
        one.id(id);
        final Entity two = new Foo();
        two.id(id);

        // ASSERT
        assertEquals(one, two);
        assertNotSame(one, two);
    }

    /**
     * Test.
     */
    public void testEqualsRejectsDissimilarClasses() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final Entity one = new Foo();
        one.id(id);
        final Entity two = new Bar();
        two.id(id);

        // ACT
        final boolean areEqual = one.equals(two);

        // ASSERT
        assertFalse(
            "Objects with different classes should not be equal",
            areEqual);
        assertNotSame(one, two);
    }

    /**
     * Foo.
     *
     * @author Civic Computing Ltd.
     */
    static class Foo extends Entity {
    }

    /**
     * Bar.
     *
     * @author Civic Computing Ltd.
     */
    static class Bar extends Entity {
    }
}
