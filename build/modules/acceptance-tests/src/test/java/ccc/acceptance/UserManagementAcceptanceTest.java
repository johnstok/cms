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
import java.util.UUID;

import ccc.rest.CommandFailedException;
import ccc.rest.dto.UserSummary;
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
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdatePassword() throws CommandFailedException {

        // ARRANGE
        final UserSummary us = tempUser();

        // ACT
        _users.updateUserPassword(
            us.getId(), new UserSummary("Another00-"));

        // ASSERT
        assertFalse(
            _security.login(us.getUsername().toString(), "Testtest00-"));
        assertTrue(
            _security.login(us.getUsername().toString(), "Another00-"));
    }


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
            _users.listUsersWithUsername(us.getUsername().toString());

        // ASSERT
        assertEquals(1, ul.size());
        final UserSummary uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("CONTENT_CREATOR"));
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
            _users.listUsersWithEmail(us.getEmail());

        // ASSERT
        assertEquals(1, ul.size());
        final UserSummary uq = ul.iterator().next();
        assertEquals(us.getUsername(), uq.getUsername());
        assertEquals(us.getEmail(), uq.getEmail());
        assertEquals(1, uq.getRoles().size());
        assertTrue(uq.getRoles().contains("CONTENT_CREATOR"));
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
        _users.updateUser(
            us.getId(),
            new UserSummary(
                email,
                username,
                Collections.singleton("a2"),
                Collections.singletonMap("key2", "value2")));

        // ASSERT
        final UserSummary ud = _users.userDelta(us.getId());
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
        final UserSummary u =
            new UserSummary(
                email,
                username,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");


        final UserSummary us = _users.createUser(u);
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
    public void testUpdateYourUser() throws CommandFailedException {

        // ARRANGE
        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";
        final String password = "test Test00-"+username;

        UserSummary user = _users.loggedInUser();
        final UserSummary uo =new UserSummary(email, "test Test00-"+username);

        // ACT
        _users.updateYourUser(user.getId(), uo);
        user = _users.loggedInUser();

        // ASSERT
        assertEquals(user.getEmail(), uo.getEmail());
        assertFalse(
            _security.login(user.getUsername().toString(), "Testtest00-"));
        assertTrue(
            _security.login(user.getUsername().toString(), password));
    }


    private UserSummary tempUser() throws CommandFailedException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        // Create the user
        final UserSummary u =
            new UserSummary(
                email,
                username,
                Collections.singleton("CONTENT_CREATOR"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");

        return _users.createUser(u);
    }
}
