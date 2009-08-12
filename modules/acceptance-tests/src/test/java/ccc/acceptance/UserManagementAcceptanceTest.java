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
package ccc.acceptance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.api.rest.UserNew;
import ccc.commands.CommandFailedException;


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
     * @throws CommandFailedException If the test fails.
     */
    public void testSearchForUsersWithUsername() throws CommandFailedException {

        // ARRANGE
        final UserSummary us = tempUser();

        // ACT
        final Collection<UserSummary> ul =
            _queries.listUsersWithUsername(us.getUsername().toString());

        // ASSERT
        assertEquals(1, ul.size());
        final UserSummary uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("a"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testSearchForUsersWithEmail() throws CommandFailedException {

        // ARRANGE
        final UserSummary us = tempUser();

        // ACT
        final Collection<UserSummary> ul =
            _queries.listUsersWithEmail(us.getEmail());

        // ASSERT
        assertEquals(1, ul.size());
        final UserSummary uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("a"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateUser() throws CommandFailedException {

        // ARRANGE
        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        final UserSummary us = tempUser();

        // ACT
        _commands.updateUser(
            us.getId(),
            new UserDelta(
                email,
                username,
                Collections.singleton("a2"),
                Collections.singletonMap("key2", "value2")));

        // ASSERT
        final UserDelta ud = _queries.userDelta(us.getId());
//        assertEquals(username, ud.getUsername());
        assertEquals(email, ud.getEmail());
        assertEquals(1, ud.getRoles().size());
        assertTrue(ud.getRoles().contains("a2"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateUser() throws CommandFailedException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        // Create the user
        final UserNew u =
            new UserNew(
                new UserDelta(
                    email,
                    username,
                    Collections.singleton("a"),
                    Collections.singletonMap("key", "value")),
                "Testtest00-");


        final UserSummary us = _commands.createUser(u);
        assertEquals(username, us.getUsername());
        assertEquals(email, us.getEmail());
        assertEquals(1, us.getRoles().size());
        assertTrue(us.getRoles().contains("a"));
        // TODO: Test metadata set correctly.
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateUser2() throws CommandFailedException {

        // ARRANGE
        final UserDelta d =
            new UserDelta(
                "abc@def.com",
                new Username("qwerty"),
                new HashSet<String>(),
                new HashMap<String, String>());

        // ACT
        _commands.createUser(new UserNew(d, "Testtest00-"));
        final Collection<UserSummary> users =
            _queries.listUsersWithUsername(d.getUsername().toString());

        // ASSERT
        assertEquals(1, users.size());
    }


    private UserSummary tempUser() throws CommandFailedException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        // Create the user
        final UserNew u =
            new UserNew(
                new UserDelta(
                    email,
                    username,
                    Collections.singleton("a"),
                    Collections.singletonMap("key", "value")),
                "Testtest00-");

        return _commands.createUser(u);
    }
}
