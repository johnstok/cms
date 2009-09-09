/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import junit.framework.TestCase;


/**
 * Tests for the {@link Username} class.
 *
 * @author Civic Computing Ltd.
 */
public class UsernameTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testUsernameHascode() {

        // ARRANGE
        final Username u1 = new Username("jack");
        final Username u2 = new Username("jack");

        // ACT

        // ASSERT
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    /**
     * Test.
     */
    public void testUsernameToString() {

        // ARRANGE
        final Username uname = new Username("jack");

        // ACT
        final String unString = uname.toString();

        // ASSERT
        assertEquals("jack", unString);
    }

    /**
     * Test.
     */
    public void testUsernameEquality() {

        // ARRANGE
        final Username uname = new Username("jack");

        // ACT

        // ASSERT
        assertTrue(uname.equals(uname));
        assertTrue(uname.equals(new Username("jack")));
        assertFalse(uname.equals(new Username("jill")));
        assertFalse(uname.equals(null));
        assertFalse(uname.equals(new Object()));
    }


    /**
     * Test.
     */
    public void testNullUsernamesAreRejected() {

        // ACT
        try {
            new Username(null);
            fail("NULL usernames should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified value may not be NULL.",
                e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testZlsUsernamesAreRejected() {

        // ACT
        try {
            new Username("");
            fail("ZLS usernames should be rejected.");

            // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }
    }


//    /**
//     * Test.
//     */
//    public void testUsernamesAreLongerThanThreeChars() {
//
//        // ACT
//        try {
//            new Username("aaa");
//            fail("Short usernames should be rejected.");
//
//            // ASSERT
//        } catch (final IllegalArgumentException e) {
//            assertEquals(
//                "Specified string must have a min length of 4.",
//                e.getMessage());
//        }
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testConstructorRejectsInvalidUsername() {
//
//        // ACT
//        try {
//            new Username("Empty name");
//            fail("Spaces should be rejected.");
//
//        // ASSERT
//        } catch (final IllegalArgumentException e) {
//            assertEquals(
//                "Specified string (Empty name) does not match [\\w]*",
//                e.getMessage());
//        }
//    }
}
