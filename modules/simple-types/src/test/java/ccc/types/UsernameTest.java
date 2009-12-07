/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
