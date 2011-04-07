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

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.Username;


/**
 * Acceptance for user management.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagementAcceptanceTest
    extends
        AbstractAcceptanceTest {

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
                .query(
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
                .query(
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
            getGroups().query("SITE_BUILDER",
                              1,
                              PAGE_SIZE).getElements();
        final Group siteBuilder = groups.iterator().next();

        final User us = tempUser();

        // ACT

        getUsers().update(
            us.getId(),
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setGroups(Collections.singleton(siteBuilder.getId()))
                .setMetadata(Collections.singletonMap("key2", "value2")));

        // ASSERT
        final User ud = getUsers().retrieve(us.getId());
        assertEquals(username, ud.getUsername());
        assertEquals(email, ud.getEmail());
        assertEquals(name, ud.getName());
        assertEquals(1, ud.getGroups().size());
        assertEquals(siteBuilder.getId(), ud.getGroups().iterator().next());
        // TODO: Test metadata set correctly.
    }

    
    /**
     * Test.
     */
    public void testUpdateCurrentUser() {

        // ARRANGE
        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";

      
        final User us = getUsers().retrieveCurrent();

        // ACT

        getUsers().update(
            us.getId(),
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setGroups(us.getGroups())
                .setMetadata(Collections.singletonMap("key2", "value2")));

        // ASSERT
        final User ud = getUsers().retrieve(us.getId());
        // must not be able to change own username
        assertEquals(us.getUsername(), ud.getUsername()); 
        assertEquals(email, ud.getEmail());
        assertEquals(name, ud.getName());
    }
    

    /**
     * Test.
     */
    public void testCreateUser() {

        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";

        final List<Group> groups =
            getGroups().query("SITE_BUILDER",
                              1,
                              PAGE_SIZE).getElements();
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
        final User us = getUsers().create(u);


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
            new Username(uid()+"ЊЋЌ");
        final String email = "foo@abc.def";
        final String name = "testuser";
        final List<Group> groups =
            getGroups().query("SITE_BUILDER",
                1,
                PAGE_SIZE).getElements();
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
        final User us = getUsers().create(u);


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
        user = getUsers().retrieveCurrent();

        user.setEmail(email);
        user.setPassword(password);

        // ACT
        getUsers().updateCurrent(user);
        user = getUsers().retrieveCurrent();

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

        getUsers().create(u);

        // ACT
        final Boolean exists = getUsers().usernameExists(username);

        // ASSERT
        assertTrue("Username should exist", exists.booleanValue());
    }

    /**
     * Test.
     */
    public void testUsernameSensitiveExists() {

        // ARRANGE
        final String uuid = uid();
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

        getUsers().create(u);

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
        final User user = getUsers().retrieveCurrent();

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

        getUsers().update(
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
    
    
    /**
     * Test.
     */
    public void testSetTokenAndVerifyDTO() {
        
        // ARRANGE
        final User us = tempUser();
        
        // ACT
        getUsers().sendToken(us.getUsername().toString());
        
        // ASSERT
        final User ud = getUsers().retrieve(us.getId());
        assertEquals(us.getUsername(), ud.getUsername());
        assertEquals(1, ud.getMetadata().size());
        assertFalse("Token must not be set",
            ud.getMetadata().keySet().contains("token"));
        assertFalse("Token expiry must not be set",
            ud.getMetadata().keySet().contains("tokenExpiry"));
    }
    
    
    /**
     * Test.
     */
    public void testResetPasswordWithBadToken() {
        
        // ARRANGE
        final User us = tempUser();
        getUsers().sendToken(us.getUsername().toString());
        
        // ACT
        try {
            getUsers().resetPassword("TestTest123--", "faultytoken");
        } catch (CCException e) {
            // ASSERT
            assertEquals(
                "No user found with the token.",
                e.getMessage());
        }
    }
    
    
    /**
     * Test.
     */
    public void testResetPasswordWithExpiredToken() {
        
        // ARRANGE
        User us = tempUser();
        Map<String, String> meta = us.getMetadata();
        meta.put("token", "abc"+us.getId().toString());
        meta.put("tokenExpiry", "100");
        us.setMetadata(meta);
        getUsers().update(us.getId(), us);
        
        // ACT
        try {
            getUsers().resetPassword("TestTest123--", 
                "abc"+us.getId().toString());
        } catch (CCException e) {
            // ASSERT
            assertEquals(
                    "Token has expired.",
                e.getMessage());
        }
    }
    
    
    /**
     * Test.
     */
    public void testResetPassword() {
        
        // ARRANGE
        User user = tempUser();
        
        String token = user.getId().toString();
        Map<String, String> meta = user.getMetadata();
        meta.put("token", token);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        
        meta.put("tokenExpiry", ""+cal.getTime().getTime());
        user.setMetadata(meta);
        getUsers().update(user.getId(), user);
        
        
        // ACT
        getUsers().resetPassword("TestTest123--", token);
        getSecurity().logout();
        
        // ASSERT
        assertTrue(
            getSecurity().login(user.getUsername().toString(), "TestTest123--")
            .booleanValue());
        assertFalse(
            getSecurity().login(user.getUsername().toString(), "Testtest00-")
            .booleanValue());
        
    }
    
    
    /**
     * Test.
     */
    public void testQueryAndDeletedUsers() {
        // ARRANGE
        final User us = tempUser();
        getUsers().delete(us.getId());
        
        // ACT
        final List<User> ul =
            getUsers()
                .query(
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
        assertEquals(0, ul.size());
    }
    
    
    /**
     * Test.
     */
    public void testUsernameExistsDeletedUsers() {

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

        User user = getUsers().create(u);
        getUsers().delete(user.getId());

        // ACT
        final Boolean exists = getUsers().usernameExists(username);

        // ASSERT
        assertTrue("Username should exists", exists.booleanValue());
    }
    
    
    /**
     * Test.
     */
    public void testRetrieveDeletedUsers() {
        // ARRANGE
        final User us = tempUser();
        getUsers().delete(us.getId());
        
        // ACT
        // ASSERT
        try {
            getUsers().retrieve(us.getId());
            fail("User should not be found");
        } catch (final EntityNotFoundException e) {
            assertEquals(null, e.getId());
        }
    }
    
    
    /**
     * Test.
     */
    public void testUserByLegacyIdDeletedUsers() {
        // ARRANGE
        final User us = tempUser();

        final int legacyId = new Random().nextInt(100000);

        Map<String, String> metadata = us.getMetadata();
        metadata.put("legacyId", ""+legacyId);
        us.setMetadata(metadata);
        getUsers().update(us.getId(), us);
        getUsers().delete(us.getId());

        // ACT
        // ASSERT
        try {
            getUsers().userByLegacyId(""+legacyId);
            fail("User should not be found");
        } catch (final EntityNotFoundException e) {
            assertEquals(null, e.getId());
        }
    }
}
