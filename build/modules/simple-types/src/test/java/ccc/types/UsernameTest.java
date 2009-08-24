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
