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
        _users.updateUserPassword(
            us.getId(), new UserDto("Another00-"));

        // ASSERT
        assertFalse(
            _security.login(us.getUsername().toString(), "Testtest00-"));
        assertTrue(
            _security.login(us.getUsername().toString(), "Another00-"));
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
            _users.listUsersWithUsername(us.getUsername());

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
            _users.listUsersWithEmail(us.getEmail());

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

        final UserDto us = tempUser();

        // ACT
        _users.updateUser(
            us.getId(),
            new UserDto(
                email,
                username,
                Collections.singleton("a2"),
                Collections.singletonMap("key2", "value2")));

        // ASSERT
        final UserDto ud = _users.userDelta(us.getId());
//        assertEquals(username, ud.getUsername());
        assertEquals(email, ud.getEmail());
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

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");


        final UserDto us = _users.createUser(u);
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

        _security.logout();
        _security.login(user.getUsername().toString(), "Testtest00-");
        user = _users.loggedInUser();

        final UserDto uo = new UserDto(email, password);

        // ACT
        _users.updateYourUser(user.getId(), uo);
        user = _users.loggedInUser();

        // ASSERT
        assertEquals(user.getEmail(), uo.getEmail());
        assertTrue(
            _security.login(user.getUsername().toString(), password));
        assertFalse(
            _security.login(user.getUsername().toString(), "Testtest00-"));
    }

    public void testUsernameExists() throws RestException {
        // ARRANGE
        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                Collections.singleton("a"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");


        _users.createUser(u);

        // ACT
        final Boolean exists = _users.usernameExists(username);

        // ASSERT
        assertTrue("Username should exists", exists.booleanValue());

    }

    private UserDto tempUser() throws RestException {

        final Username username = new Username(UUID.randomUUID().toString());
        final String email = username+"@abc.def";

        // Create the user
        final UserDto u =
            new UserDto(
                email,
                username,
                Collections.singleton("CONTENT_CREATOR"),
                Collections.singletonMap("key", "value"),
                "Testtest00-");

        return _users.createUser(u);
    }
}
