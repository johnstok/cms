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

import static ccc.commons.Encryption.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.api.types.EmailAddress;
import ccc.api.types.Username;


/**
 * Tests for the {@link UserEntity} class.
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");

        // ASSERT
        assertTrue("Password should match.", u.hasPassword("password"));
    }


    /**
     * Test.
     */
    public void testChangePassword() {
        // ARRANGE
        final String password = "newPass";
        final UserEntity u = new UserEntity(new Username("dummy"), "password");

        // ACT
        u.setPassword("newPass");

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, u.getPassword().length);
        assertTrue(
            "Hashes should be equal.",
            Arrays.equals(
                hash(password, u.getId().toString()),
                u.getPassword()));
    }


    /**
     * Test.
     */
    public void testGroupsAccessorHandlesNoGroups() {

        // ARRANGE
        final UserEntity u = new UserEntity(new Username("dummy"), "password");

        // ACT
        final Set<GroupEntity> groups = u.getGroups();

        // ASSERT
        assertEquals(0, groups.size());
    }


    /**
     * Test.
     */
    public void testEqualityIsIdBased() {

        // ARRANGE
        final UserEntity u1 = new UserEntity(new Username("dummy"), "password");
        final UserEntity u2 = new UserEntity(new Username("dummy"), "password");

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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");

        // ACT
        final Username username = u.getUsername();

        // ASSERT
        assertEquals(new Username("dummy"), username);
    }


    /**
     * Test.
     */
    public void testConstructorRejectsEmptyUsername() {

        // ACT
        try {
            new UserEntity(new Username(""), "password");
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
            new UserEntity(null, "password");
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.setEmail(new EmailAddress("fooEmail@test.com"));

        // ACT
        final String email = u.getEmail().getText();

        // ASSERT
        assertEquals("fooEmail@test.com", email);
    }


    /**
     * Test.
     */
    public void testCreatorGroups() {

        // ARRANGE
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        final Set<GroupEntity> expected =
            new HashSet<GroupEntity>() {{
                add(CONTENT_CREATOR);
                add(SITE_BUILDER);
            }};

        // ACT
        u.addGroup(CONTENT_CREATOR);
        u.addGroup(SITE_BUILDER);
        u.addGroup(SITE_BUILDER);

        // ASSERT
        assertEquals(2, u.getGroups().size());
        assertEquals(expected, u.getGroups());
        assertTrue(
            "Should be site builder",
            SITE_BUILDER.includes(u));
        assertTrue(
            "Should be content creator",
            CONTENT_CREATOR.includes(u));
    }


    /**
     * Test.
     */
    public void testRejectsEmptyEmail() {

        // ARRANGE
        final UserEntity u = new UserEntity(new Username("dummy"), "password");

        // ACT
        try {
            u.setEmail(null);
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.setEmail(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.setUsername(null);
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.setEmail(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.setUsername(new Username(""));
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.setEmail(new EmailAddress("fooEmail@test.com"));

        // ACT
        try {
            u.setUsername(new Username("blaa blaa"));
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
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.setEmail(new EmailAddress("fooEmail@test.com"));

        // ACT
        u.setUsername(new Username("newDummy"));

        // ASSERT
        assertEquals("newDummy", u.getUsername().toString());
    }


    /**
     * Test.
     */
    public void testReplaceGroups() {

        // ARRANGE
        final UserEntity u = new UserEntity(new Username("dummy"), "password");
        u.addGroup(CONTENT_CREATOR);
        u.addGroup(SITE_BUILDER);
        u.addGroup(SITE_BUILDER);

        final Set<GroupEntity> expected =
            new HashSet<GroupEntity>() {{
                add(ADMINISTRATOR);
                add(SITE_BUILDER);
            }};

        // ACT
        u.clearGroups();
        u.addGroup(ADMINISTRATOR);
        u.addGroup(SITE_BUILDER);

        // ASSERT
        assertEquals(2, u.getGroups().size());
        assertEquals(expected, u.getGroups());
        assertTrue(
            "Should be site builder",
            SITE_BUILDER.includes(u));
        assertTrue(
            "Should be administrator",
            ADMINISTRATOR.includes(u));
    }


    private static final GroupEntity SITE_BUILDER =
        new GroupEntity("SITE_BUILDER");
    private static final GroupEntity CONTENT_CREATOR =
        new GroupEntity("CONTENT_CREATOR");
    private static final GroupEntity ADMINISTRATOR =
        new GroupEntity("ADMINISTRATOR");
}
