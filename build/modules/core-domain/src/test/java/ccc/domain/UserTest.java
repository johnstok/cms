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

import java.util.EnumSet;

import junit.framework.TestCase;


/**
 * Tests for the {@link User} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testEqualityIsIdBased() {

        // ARRANGE
        final User u1 = new User("dummy");
        final User u2 = new User("dummy");

        // ASSERT
        assertEquals(u1, u1);
        assertEquals(u2, u2);
        assertFalse("Shouldn't be equal", u1.equals(u2));
        assertFalse("Shouldn't be equal", u2.equals(u1));

    }

    /**
     * Test.
     */
    public void testAccessorForUsername() {

        // ARRANGE
        final User u = new User("dummy");

        // ACT
        final String username = u.username();

        // ASSERT
        assertEquals("dummy", username);
    }

    /**
     * Test.
     */
    public void testUsernamesAreLongerThanThreeChars() {

        // ACT
        try {
            new User("aaa");
            fail("Short usernames should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have a min length of 4.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyUsername() {

        // ACT
        try {
            new User("");
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructorRejectsNullUsername() {

        // ACT
        try {
            new User(null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testAccessorForEmail() {

        // ARRANGE
        final User u = new User("dummy");
        u.email("fooEmail@test.com");

        // ACT
        final String email = u.email();

        // ASSERT
        assertEquals("fooEmail@test.com", email);
    }

    /**
     * Test.
     */
    public void testCreatorRoles() {

        // ARRANGE
        final User u = new User("dummy");
        final EnumSet<CreatorRoles> expected =
            EnumSet.of(
                CreatorRoles.CONTENT_CREATOR,
                CreatorRoles.SITE_BUILDER);

        // ACT
        u.addRole(CreatorRoles.CONTENT_CREATOR);
        u.addRole(CreatorRoles.SITE_BUILDER);
        u.addRole(CreatorRoles.SITE_BUILDER);

        // ASSERT
        assertEquals(2, u.roles().size());
        assertEquals(expected, u.roles());
        assertTrue(
            "Should be site builder",
            u.hasRole(CreatorRoles.SITE_BUILDER));
        assertTrue(
            "Should be content creator",
            u.hasRole(CreatorRoles.CONTENT_CREATOR));
    }

    /**
     * Test.
     */
    public void testRejectsEmptyEmail() {

        // ARRANGE
        final User u = new User("dummy");

        // ACT
        try {
            u.email(null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRejectsInvalidEmail() {

        // ARRANGE
        final User u = new User("dummy");

        // ACT
        try {
            u.email("blaablaa");
            fail("Invalid email should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified expression must be true.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructorRejectsInvalidUsername() {

        // ACT
        try {
            new User("Empty name");
            fail("Spaces should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string does not match [\\w]*",
                e.getMessage());
        }
    }

}
