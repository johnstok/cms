/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.types.CreatorRoles;
import ccc.types.EmailAddress;
import ccc.types.Username;


/**
 * Tests for the {@link User} class.
 * TODO: Disallow NULL or empty password.
 * TODO: Validate password characters, min length, max length(?)
 *
 * @author Civic Computing Ltd.
 */
public class UserTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testPasswordSetFromConstructor() {

        // ARRANGE

        // ACT
        final User u = new User(new Username("dummy"), "password");

        // ASSERT
        assertTrue("Password should match.", u.matches("password"));
    }

    /**
     * Test.
     */
    public void testHash() {

        // ARRANGE
        final String password = "password";
        final User u = new User(new Username("dummy"), password);

        // ACT
        final byte[] hash = User.hash(password, u.id().toString());

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, hash.length);
        assertTrue(
            "Hashes should be equal.",
            Arrays.equals(hash(u, "password"), hash));
    }

    /**
     * Test.
     */
    public void testChangePassword() {
        // ARRANGE
        final String password = "newPass";
        final User u = new User(new Username("dummy"), "password");

        // ACT
        u.password("newPass");
        final byte[] hash = User.hash(password, u.id().toString());

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, hash.length);
        assertTrue(
            "Hashes should be equal.",
            Arrays.equals(hash(u, password), hash));
    }

    private static final int SHA_HASH_LENGTH = 32;

    /**
     * Test.
     */
    public void testRolesAccessorHandlesNoRoles() {

        // ARRANGE
        final User u = new User(new Username("dummy"), "password");

        // ACT
        final Set<Group> roles = u.roles();

        // ASSERT
        assertEquals(0, roles.size());
    }

    /**
     * Test.
     */
    public void testEqualityIsIdBased() {

        // ARRANGE
        final User u1 = new User(new Username("dummy"), "password");
        final User u2 = new User(new Username("dummy"), "password");

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
        final User u = new User(new Username("dummy"), "password");

        // ACT
        final Username username = u.username();

        // ASSERT
        assertEquals(new Username("dummy"), username);
    }

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyUsername() {

        // ACT
        try {
            new User(new Username(""), "password");
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
            new User(null, "password");
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
        final User u = new User(new Username("dummy"), "password");
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
        final User u = new User(new Username("dummy"), "password");
        final Set<Group> expected =
            new HashSet<Group>() {{
                add(CONTENT_CREATOR);
                add(SITE_BUILDER);
            }};

        // ACT
        u.addRole(CONTENT_CREATOR);
        u.addRole(SITE_BUILDER);
        u.addRole(SITE_BUILDER);

        // ASSERT
        assertEquals(2, u.roles().size());
        assertEquals(expected, u.roles());
        assertTrue(
            "Should be site builder",
            u.memberOf(SITE_BUILDER));
        assertTrue(
            "Should be content creator",
            u.memberOf(CONTENT_CREATOR));
    }

    /**
     * Test.
     */
    public void testRejectsEmptyEmail() {

        // ARRANGE
        final User u = new User(new Username("dummy"), "password");

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
     * TODO: Disabled for now, to allow migration of old data.
     */
    public void testRejectsInvalidEmail() {
//
//        // ARRANGE
//        final User u = new User(new Username("dummy"));
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
        final User u = new User(new Username("dummy"), "password");
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
        final User u = new User(new Username("dummy"), "password");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.username(new Username(""));
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
        final User u = new User(new Username("dummy"), "password");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.username(new Username("blaa blaa"));
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
        final User u = new User(new Username("dummy"), "password");
        u.email(new EmailAddress("fooEmail@test.com"));

        // ACT
        u.username(new Username("newDummy"));

        // ASSERT
        assertEquals("newDummy", u.username().toString());
    }

    /**
     * Test.
     */
    public void testReplaceRoles() {

        // ARRANGE
        final User u = new User(new Username("dummy"), "password");
        u.addRole(CONTENT_CREATOR);
        u.addRole(SITE_BUILDER);
        u.addRole(SITE_BUILDER);

        final Set<Group> expected =
            new HashSet<Group>() {{
                add(ADMINISTRATOR);
                add(SITE_BUILDER);
            }};

        // ACT
        u.clearGroups();
        u.addRole(ADMINISTRATOR);
        u.addRole(SITE_BUILDER);

        // ASSERT
        assertEquals(2, u.roles().size());
        assertEquals(expected, u.roles());
        assertTrue(
            "Should be site builder",
            u.memberOf(SITE_BUILDER));
        assertTrue(
            "Should be administrator",
            u.memberOf(ADMINISTRATOR));
    }

    private byte[] hash(final User u, final String passwordString) {
        try {
            // Prepare
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final Charset utf8 = Charset.forName("UTF-8");
            final byte[] salt = u.id().toString().getBytes(utf8);
            final byte[] password = passwordString.getBytes(utf8);

            // Compute
            digest.reset();
            digest.update(salt);
            digest.update(password);
            byte[] hash = digest.digest();

            final int hashRepetitions = 1000;
            for (int i = 0; i < hashRepetitions; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;

        } catch (final NoSuchAlgorithmException e) {
            throw new CCCException("Failed to compute password digest.", e);
        }
    }

    private static final Group SITE_BUILDER =
        new Group(CreatorRoles.SITE_BUILDER);
    private static final Group CONTENT_CREATOR =
        new Group(CreatorRoles.CONTENT_CREATOR);
    private static final Group ADMINISTRATOR =
        new Group(CreatorRoles.ADMINISTRATOR);
}
