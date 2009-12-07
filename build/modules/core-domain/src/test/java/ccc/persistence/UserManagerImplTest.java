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
package ccc.persistence;


import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;
import ccc.commands.CreateUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.rest.dto.UserDto;
import ccc.types.CreatorRoles;
import ccc.types.EmailAddress;
import ccc.types.Username;


/**
 * Tests for the {@link UserRepositoryImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerImplTest extends TestCase {

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnTrue() {

        // ARRANGE
        expect(Boolean.valueOf(
            _repository.exists(
            QueryNames.USERS_WITH_USERNAME, User.class, "blat")))
            .andReturn(Boolean.TRUE);
        replayAll();

        // ACT
        final boolean actual = _um.usernameExists("blat");

        // ASSERT
        assertEquals(true, actual);
        verifyAll();
    }

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnFalse() {

        // ARRANGE
        expect(Boolean.valueOf(
            _repository.exists(
                QueryNames.USERS_WITH_USERNAME, User.class, "blat")))
            .andReturn(Boolean.FALSE);
        replayAll();


        // ACT
        final boolean actual = _um.usernameExists("blat");

        // ASSERT
        assertEquals(false, actual);
        verifyAll();
    }

    /**
     * Test.
     * TODO: Test the actual values of the created user.
     */
    public void testCreateUser() {

        // ARRANGE
        final Date now = new Date();
        _repository.create(isA(User.class));
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final CreateUserCommand cu = new CreateUserCommand(_um, _audit);

        // ACT
        final User u = cu.execute(_u, now, _uDelta);

        // ASSERT
        verifyAll();
        assertEquals("newNameUser", u.username().toString());
    }

    /**
     * Test.
     */
    public void testListUsers() {

        // ARRANGE
        expect(_repository.uniquify(QueryNames.USERS, User.class))
            .andReturn(new ArrayList<User>());
        replayAll();


        // ACT
        _um.listUsers();

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithRole() {

        // ARRANGE
        expect(_repository.uniquify(QueryNames.USERS_WITH_ROLE,
                            User.class,
                            CreatorRoles.ADMINISTRATOR))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        _um.listUsersWithRole(CreatorRoles.ADMINISTRATOR);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithUsername() {

        // ARRANGE
        expect(_repository.list(QueryNames.USERS_WITH_USERNAME,
            User.class, "testname"))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        _um.listUsersWithUsername("testname");

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithEmail() {

        // ARRANGE
        expect(_repository.list(QueryNames.USERS_WITH_EMAIL,
            User.class, "test@civicuk.com"))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        _um.listUsersWithEmail("test@civicuk.com");

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     * TODO: Test the actual values of the created user.
     *
     * @throws Exception If the test fails.
     */
    public void testUpdateUser() throws Exception {

        // ARRANGE
        final Date now = new Date();
        expect(_repository.find(User.class, _u.id())).andReturn(_u);
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final UpdateUserCommand uu =
            new UpdateUserCommand(_um, _audit, _u.id(), _uDelta);

        // ACT
        uu.execute(_u, now);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUpdateUserPassword() throws Exception {

        // ARRANGE
        final Date now = new Date();

        expect(_repository.find(User.class, _u.id())).andReturn(_u);
        _audit.record(isA(LogEntry.class));
        replayAll();

        final UpdatePasswordAction up =
            new UpdatePasswordAction(_um, _audit);

        // ACT
        up.execute(_u, now, _u.id(), "newPass");

        // ASSERT
        verifyAll();
        assertTrue(_u.matches("newPass"));
    }

    private User _u;
    private LogEntryRepository _audit;
    private UserDto _uDelta;
    private Repository _repository;
    private UserRepositoryImpl _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _u = new User(new Username("testUser"), "password");
        _uDelta =
            new UserDto(
                "new.email@civicuk.com",
                new Username("newNameUser"),
                "newNameUser",
                new HashSet<String>(),
                new HashMap<String, String>(),
                "foopass");
        _u.email(new EmailAddress("test@civicuk.com"));
        _repository = createStrictMock(Repository.class);
        _audit = createStrictMock(LogEntryRepository.class);
        _um = new UserRepositoryImpl(_repository);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _audit = null;
        _u = null;
        _uDelta = null;
        _repository = null;
        _um = null;
    }

    private void verifyAll() {
        verify(_repository, _audit);
    }

    private void replayAll() {
        replay(_repository, _audit);
    }
}
