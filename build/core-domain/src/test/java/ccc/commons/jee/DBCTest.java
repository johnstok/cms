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

import static ccc.commons.jee.DBC.*;
import junit.framework.TestCase;


/**
 * Tests for the DBC class.
 *
 * @author Civic Computing Ltd
 */
public final class DBCTest extends TestCase {

    /**
     * Test.
     */
    public void testRequireNotNull() {

        // ACT
        require().notNull(new Object());

        try {
            require().notNull(null);
            fail("NULL should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRequireNotEmpty() {

        // ACT
        require().notEmpty("foo");

        try {
            require().notEmpty(null);
            fail("NULL should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified string may not be NULL.", e.getMessage());
        }

        try {
            require().notEmpty("");
            fail("Zero length string should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }

        try {
            require().notEmpty("   ");
            fail("A string with only spaces should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }
    }
}
