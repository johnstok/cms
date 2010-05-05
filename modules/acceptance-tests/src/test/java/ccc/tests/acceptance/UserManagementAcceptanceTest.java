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
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ccc.api.core.Group;
import ccc.api.core.User;
import ccc.api.types.PagedCollection;
import ccc.api.types.Username;


/**
 * Acceptance for user management.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagementAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /** PAGE_SIZE : int. */
    private static final int PAGE_SIZE = 20;



    /**
     * Test.
     */
    public void testUpdatePassword() {

        // ARRANGE
        final User us = tempUser();

        // ACT
        us.setPassword("Another00-");
        getUsers().updateUserPassword(us.getId(), us);

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
     */
    public void testSearchForUsersWithUsername() {

        // ARRANGE
        final User us = tempUser();

        // ACT
        final List<User> ul =
            getUsers()
                .listUsers(
                    us.getUsername().toString(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    1,
                    PAGE_SIZE)
                .getElements();

        // ASSERT
        assertEquals(1, ul.size());
        final User uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getGroups().size());
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     */
    public void testSearchForUsersWithEmail() {

        // ARRANGE
        final User us = tempUser();

        // ACT
        final PagedCollection<User> ul =
            getUsers()
                .listUsers(
                    null,
                    us.getEmail(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    1,
                    PAGE_SIZE);

        // ASSERT
        assertEquals(1, ul.getTotalCount());
        final User uq = ul.getElements().iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getGroups().size());
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     */
    public void testUpdateUser() {

        // ARRANGE
        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";

        final List<Group> groups =
            getGroups().list("SITE_BUILDER", 1, 20).getElements();
        final Group siteBuilder = groups.iterator().next();

        final User us = tempUser();

        // ACT

        getUsers().updateUser(
            us.getId(),
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setGroups(Collections.singleton(siteBuilder.getId()))
                .setMetadata(Collections.singletonMap("key2", "value2")));

        // ASSERT
        final User ud = getUsers().userDelta(us.getId());
//        assertEquals(username, ud.getUsername());
        assertEquals(email, ud.getEmail());
        assertEquals(name, ud.getName());
        assertEquals(1, ud.getGroups().size());
        assertEquals(siteBuilder.getId(), ud.getGroups().iterator().next());
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     */
    public void testCreateUser() {

        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";

        final List<Group> groups =
            getGroups().list("SITE_BUILDER", 1, 20).getElements();
        final Group siteBuilder = groups.iterator().next();

        // Create the user
        final User u =
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setGroups(Collections.singleton(siteBuilder.getId()))
                .setMetadata(Collections.singletonMap("key", "value"))
                .setPassword("Testtest00-");
        final User us = getUsers().createUser(u);


        assertEquals(username, us.getUsername());
        assertEquals(email, us.getEmail());
        assertEquals(1, us.getGroups().size());
        assertEquals(siteBuilder.getId(), us.getGroups().iterator().next());
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     */
    public void testUsernamesSupportNonAsciiChars() {

        final Username username =
            new Username(UUID.randomUUID().toString().substring(0, 8)+"ЊЋЌ");
        final String email = "foo@abc.def";
        final String name = "testuser";
        final List<Group> groups =
            getGroups().list("SITE_BUILDER", 1, 20).getElements();
        final Group siteBuilder = groups.iterator().next();

        // Create the user
        final User u =
            new User()
        .setEmail(email)
        .setUsername(username)
        .setName(name)
        .setGroups(Collections.singleton(siteBuilder.getId()))
        .setMetadata(Collections.singletonMap("key", "value"))
        .setPassword("Testtest00-");
        final User us = getUsers().createUser(u);


        assertEquals(username, us.getUsername());
        assertEquals(email, us.getEmail());
        assertEquals(1, us.getGroups().size());
        assertEquals(siteBuilder.getId(), us.getGroups().iterator().next());
        // TODO: Test metadata set correctly.
    }

    /**
     * Test.
     */
    public void testUpdateYourUser() {

        // ARRANGE
        User user = tempUser();

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
     */
    public void testUsernameExists() {

        // ARRANGE
        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";

        // Create the user
        final User u =
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setMetadata(Collections.singletonMap("key", "value"))
                .setPassword("Testtest00-");

        getUsers().createUser(u);

        // ACT
        final Boolean exists = getUsers().usernameExists(username);

        // ASSERT
        assertTrue("Username should exists", exists.booleanValue());
    }

    /**
     * Test.
     */
    public void testUsernameSensitiveExists() {

        // ARRANGE
        final String uuid = UUID.randomUUID().toString().substring(0, 8);
        final Username originalUsername = new Username(uuid+"A");
        final Username testUsername = new Username(uuid+"a");
        final String email = originalUsername+"@abc.def";
        final String name = "testuser";

        // Create the user
        final User u =
            new User()
        .setEmail(email)
        .setUsername(originalUsername)
        .setName(name)
        .setMetadata(Collections.singletonMap("key", "value"))
        .setPassword("Testtest00-");

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
        final User user = getUsers().loggedInUser();

        // ASSERT
        assertEquals(new Username("migration"), user.getUsername());
    }



    /**
     * Test.
     */
    public void testSearchForUsersWithLegacyId() {

        // ARRANGE
        final User us = tempUser();

        final int legacyId = new Random().nextInt(100000);

        getUsers().updateUser(
            us.getId(),
            new User()
                .setEmail(us.getEmail())
                .setUsername(us.getUsername())
                .setName(us.getName())
                .setMetadata(
                    Collections.singletonMap("legacyId", ""+legacyId)));

        // ACT
        final User ul = getUsers().userByLegacyId(""+legacyId);

        // ASSERT
        assertNotNull(ul);
        assertEquals(us.getUsername(), ul.getUsername());
        assertEquals(us.getEmail(), ul.getEmail());
        assertEquals(0, ul.getGroups().size());
    }
}
