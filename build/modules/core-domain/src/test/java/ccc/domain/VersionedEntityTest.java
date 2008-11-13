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
 * Tests for the {@link VersionedEntity} class.
 *
 * @author Civic Computing Ltd.
 */
public class VersionedEntityTest extends TestCase {

    /**
     * Test.
     */
    public void testVersionMutator() {

        // ARRANGE
        final VersionedEntity e = new Foo();

        // ACT
        e.version(2);

        // ASSERT
        assertEquals(2, e.version());

    }

    /**
     * Test.
     */
    public void testEquals() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final VersionedEntity one = new Foo();
        one.id(id);
        final VersionedEntity two = new Foo();
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
        final VersionedEntity one = new Foo();
        one.id(id);
        final VersionedEntity two = new Bar();
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
    static class Foo extends VersionedEntity { /* No methods. */ }

    /**
     * Bar.
     *
     * @author Civic Computing Ltd.
     */
    static class Bar extends VersionedEntity { /* No methods. */ }
}
