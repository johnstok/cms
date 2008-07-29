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

package ccc.commons.jee;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UIDTest extends TestCase {

    public void testToString() {

        // ARRANGE
        final UID a = new UID(1L);

        // ACT
        final String aString = a.toString();

        // ASSERT
        assertEquals("1", aString);
    }

    public void testUniqueness() {

        // ARRANGE
        final Set<UID> uids = new HashSet<UID>();

        // ACT
        for (int i = 0; i < 1000000; i++) {
            final UID uid = new UID();

            // ASSERT
            assertFalse("Duplicate found!", uids.contains(uid));
            uids.add(uid);
        }
    }

    public void testEqualUidsAreEqual() {

        // ARRANGE
        final UID a = new UID(1L);
        final UID b = new UID(1L);

        // ASSERT
        assertEquals(a, b);
    }

    public void testInequalUidsAreNotEqual() {

        // ARRANGE
        final UID a = new UID(1L);
        final UID b = new UID(2L);

        // ASSERT
        assertFalse("UIDs should not be considered equal!", a.equals(b));
    }

    public void testValueAccessor() {

        // ARRANGE
        final UID a = new UID(1L);

        // ACT
        final long value = a.value();

        // ASSERT
        assertEquals(1L, value);
    }
}
