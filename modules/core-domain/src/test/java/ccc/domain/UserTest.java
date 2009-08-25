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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.types.CreatorRoles;
import ccc.types.EmailAddress;


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
    public void testRolesAccessorHandlesNoRoles() {

        // ARRANGE
        final User u = new User("dummy");

        // ACT
        final Set<String> roles = u.roles();

        // ASSERT
        assertEquals(0, roles.size());
    }

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
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        final String email = u.email().getText();

        // ASSERT
        assertEquals("fooEmail@test.com", email);
    }

    /**
     * Test.
     */
    public void testCreatorRoles() {

        // ARRANGE
        final User u = new User("dummy");
        final Set<String> expected =
            new HashSet<String>() {{
                add(CreatorRoles.CONTENT_CREATOR);
                add(CreatorRoles.SITE_BUILDER);
            }};

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
     * Disabled for now, to allow migration of old data.
     */
    public void testRejectsInvalidEmail() {
//
//        // ARRANGE
//        final User u = new User("dummy");
//
//        // ACT
//        try {
//            u.email(new EmailAddress("blaablaa"));
//            fail("Invalid email should be rejected.");
//
//        // ASSERT
//        } catch (final IllegalArgumentException e) {
//            assertEquals(
//                "Specified expression must be true.", e.getMessage());
//        }
    }

    /**
     * Test.
     */
    public void testUsernameMutatorRejectsNullUsername() {

        // ARRANGE
        final User u = new User("dummy");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.username(null);
        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testUsernameMutatorRejectsEmptyUsername() {

        // ARRANGE
        final User u = new User("dummy");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.username("");
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
    public void testUsernameMutatorRejectsInvalidUsername() {

        // ARRANGE
        final User u = new User("dummy");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.username("blaa blaa");
            // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string (blaa blaa) does not match [\\w]*",
                e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testUsernameMutator() {

        // ARRANGE
        final User u = new User("dummy");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        u.username("newDummy");

        // ASSERT
        assertEquals("newDummy", u.username());
    }

    /**
     * Test.
     */
    public void testReplaceRoles() {

        // ARRANGE
        final User u = new User("dummy");
        u.addRole(CreatorRoles.CONTENT_CREATOR);
        u.addRole(CreatorRoles.SITE_BUILDER);
        u.addRole(CreatorRoles.SITE_BUILDER);

        final Set<String> expected =
            new HashSet<String>() {{
                add(CreatorRoles.ADMINISTRATOR);
                add(CreatorRoles.SITE_BUILDER);
            }};

        // ACT
        final Set<String> newRoles = new HashSet<String>();
        newRoles.add(CreatorRoles.ADMINISTRATOR);
        newRoles.add(CreatorRoles.SITE_BUILDER);
        u.roles(newRoles);

        // ASSERT
        assertEquals(2, u.roles().size());
        assertEquals(expected, u.roles());
        assertTrue(
            "Should be site builder",
            u.hasRole(CreatorRoles.SITE_BUILDER));
        assertTrue(
            "Should be administrator",
            u.hasRole(CreatorRoles.ADMINISTRATOR));
    }

}
