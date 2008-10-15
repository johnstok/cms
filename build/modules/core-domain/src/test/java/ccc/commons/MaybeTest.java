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

import java.io.Serializable;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MaybeTest extends TestCase {

    /**
     * Test.
     */
    public void testGetWhenPresent() {

        // ARRANGE
        final Serializable o = new Exception();
        final Maybe<Serializable> m = new Maybe<Serializable>(o);

        // ACT
        final Object actual = m.get();

        // ASSERT
        assertSame(o, actual);
    }

    /**
     * Test.
     */
    public void testGetWhenNotPresent() {

        // ARRANGE
        final Maybe<Serializable> m = new Maybe<Serializable>();

        // ACT
        try {
            m.get();
            fail("Get should throw an Illegal");

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Maybe does not contain a value.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testIsPresentWhenPresent() {

        // ARRANGE
        final Maybe<Serializable> m =
            new Maybe<Serializable>(new Exception());

        // ACT
        final boolean isPresent = m.isDefined();

        // ASSERT
        assertTrue("isPresent should be true.", isPresent);
    }

    /**
     * Test.
     */
    public void testIsPresentWhenNotPresent() {

        // ARRANGE
        final Maybe<Serializable> m = new Maybe<Serializable>();

        // ACT
        final boolean isPresent = m.isDefined();

        // ASSERT
        assertFalse("isPresent should be false.", isPresent);
    }

    /**
     * Test.
     */
    public void testIsPresentWithNullPassedToConstructor() {

        // ARRANGE
        final Maybe<Serializable> m =
            new Maybe<Serializable>(null);

        // ACT
        final boolean isPresent = m.isDefined();

        // ASSERT
        assertFalse("isPresent should be false.", isPresent);
    }
}
