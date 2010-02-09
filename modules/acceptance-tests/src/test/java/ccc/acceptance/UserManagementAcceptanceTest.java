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
package ccc.acceptance;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import ccc.rest.RestException;
import ccc.rest.dto.UserDto;
import ccc.types.Username;


/**
 * Acceptance for user management.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagementAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdatePassword() throws RestException {

        // ARRANGE
        final UserDto us = tempUser();

        // ACT
        getUsers().updateUserPassword(
            us.getId(), new UserDto("Another00-"));

        // ASSERT
        assertFalse(
            getSecurity().login(us.getUsername().toString(), "Testtest00-")
            .booleanValue());
        assertTrue(
            getSecurity().login(us.getUsername().toString(), "Another00-")
            .booleanValue());
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testSearchForUsersWithUsername() throws RestException {

        // ARRANGE
        final UserDto us = tempUser();

        // ACT
        final Collection<UserDto> ul =
            getUsers().listUsersWithUsername(us.getUsername());

        // ASSERT
        assertEquals(1, ul.size());
        final UserDto uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("CONTENT_CREATOR"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testSearchForUsersWithEmail() throws RestException {

        // ARRANGE
        final UserDto us = tempUser();

        // ACT
        final Collection<UserDto> ul =
            getUsers().listUsersWithEmail(us.getEmail());

        // ASSERT
        assertEquals(1, ul.size());
        final UserDto uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("CONTENT_CREATOR"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdateUser() throws RestException {

        // ARRANGE
        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";
        final String name = "testuser";

        final UserDto us = tempUser();

        // ACT
        getUsers().updateUser(
            us.getId(),
            new UserDto(
                email,
                username,
                name,
                Collections.singleton("a2"),
                Collections.singletonMap("key2", "value2")));

        // ASSERT
        final UserDto ud = getUsers().userDelta(us.getId());
//        assertEquals(username, ud.getUsername());
        assertEquals(email, ud.getEmail());
        assertEquals(name, ud.getName());
        assertEquals(1, ud.getRoles().size());
        assertTrue(ud.getRoles().contains("a2"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testCreateUser() throws RestException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";
        final String name = "testuser";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                name,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");


        final UserDto us = getUsers().createUser(u);
        assertEquals(username, us.getUsername());
        assertEquals(email, us.getEmail());
        assertEquals(1, us.getRoles().size());
        assertTrue(us.getRoles().contains("a"));
        // TODO: Test metadata set correctly.
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdateYourUser() throws RestException {

        // ARRANGE
        UserDto user = tempUser();

        final String email = "username@abc.def";
        final String password = "test Test00-";

        getSecurity().logout();
        getSecurity().login(user.getUsername().toString(), "Testtest00-");
        user = getUsers().loggedInUser();

        user.setEmail(email);
        user.setPassword(password);

        // ACT
        getUsers().updateYourUser(user.getId(), user);
        user = getUsers().loggedInUser();

        // ASSERT
        assertEquals(email, user.getEmail());
        assertTrue(
            getSecurity().login(user.getUsername().toString(), password)
            .booleanValue());
        assertFalse(
            getSecurity().login(user.getUsername().toString(), "Testtest00-")
            .booleanValue());
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUsernameExists() throws RestException {

        // ARRANGE
        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";
        final String name = "testuser";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                name,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");


        getUsers().createUser(u);

        // ACT
        final Boolean exists = getUsers().usernameExists(username);

        // ASSERT
        assertTrue("Username should exists", exists.booleanValue());
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUsernameSensitiveExists() throws RestException {

        // ARRANGE
        final String uuid = UUID.randomUUID().toString();
        final Username originalUsername = new Username(uuid+"A");
        final Username testUsername = new Username(uuid+"a");
        final String email = originalUsername+"@abc.def";
        final String name = "testuser";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                originalUsername,
                name,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");

        getUsers().createUser(u);

        // ACT
        final Boolean exists = getUsers().usernameExists(testUsername);

        // ASSERT
        assertFalse("Username should not exists", exists.booleanValue());
    }

    
    /**
     * Test.
     */
    public void testLoggedInUser() {

        // ACT
        final UserDto user = getUsers().loggedInUser();

        // ASSERT
        assertEquals(new Username("super"), user.getUsername());
    }



    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testSearchForUsersWithLegacyId() throws RestException {

        // ARRANGE
        final UserDto us = tempUser();

        final int legacyId = new Random().nextInt(100000);

        getUsers().updateUser(
            us.getId(),
            new UserDto(
                us.getEmail(),
                us.getUsername(),
                us.getName(),
                Collections.singleton("a2"),
                Collections.singletonMap("legacyId", ""+legacyId)));

        // ACT
        final UserDto ul = getUsers().userByLegacyId(""+legacyId);

        // ASSERT
        assertNotNull(ul);
        assertEquals(us.getUsername(), ul.getUsername());
        assertEquals(us.getEmail(), ul.getEmail());
        assertEquals(1, ul.getRoles().size());
        assertTrue(ul.getRoles().contains("a2"));
    }

    private UserDto tempUser() throws RestException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";
        final String name = "testuser";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                name,
                Collections.singleton("CONTENT_CREATOR"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");

        return getUsers().createUser(u);
    }
}
