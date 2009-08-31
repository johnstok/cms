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
package ccc.services.impl;


import static org.easymock.EasyMock.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;
import ccc.api.UserSummary;
import ccc.commands.CreateUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.LogEntry;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Repository;
import ccc.services.QueryNames;
import ccc.types.CreatorRoles;
import ccc.types.EmailAddress;
import ccc.types.Username;


/**
 * Tests for the {@link UserManagerImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerImplTest extends TestCase {

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnTrue() {

        // ARRANGE
        expect(_repository.exists(QueryNames.USERS_WITH_USERNAME, User.class, "blat"))
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
        expect(_repository.exists(QueryNames.USERS_WITH_USERNAME, User.class, "blat"))
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
        _repository.create(isA(Password.class));
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final CreateUserCommand cu = new CreateUserCommand(_repository, _audit);

        // ACT
        final User u = cu.execute(_u, now, _uDelta);

        // ASSERT
        verifyAll();
        assertEquals("newNameUser", u.username());
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
     */
    public void testUpdateUser() {

        // ARRANGE
        final Date now = new Date();
        expect(_repository.find(User.class, _u.id())).andReturn(_u);
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final UpdateUserCommand uu = new UpdateUserCommand(_repository, _audit);

        // ACT
        uu.execute(_u, now, _u.id(), _uDelta);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testUpdateUserPassword() {

        // ARRANGE
        final Date now = new Date();
        final Password pw = new Password(_u, "foo");

        expect(_repository.find(QueryNames.PASSWORD_FOR_USER, Password.class, _u.id()))
            .andReturn(pw);
        _audit.record(isA(LogEntry.class));
        replayAll();

        final UpdatePasswordAction up = new UpdatePasswordAction(_repository, _audit);

        // ACT
        up.execute(_u, now, _u.id(), "newPass");

        // ASSERT
        verifyAll();
        assertTrue(pw.matches("newPass"));
    }

    private User _u;
    private AuditLog _audit;
    private UserSummary _uDelta;
    private Repository _repository;
    private Principal _p;
    private UserManagerImpl _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _u = new User("testUser");
        _uDelta =
            new UserSummary(
                "new.email@civicuk.com",
                new Username("newNameUser"),
                new HashSet<String>(),
                new HashMap<String, String>(),
                "foopass");
        _u.email(new EmailAddress("test@civicuk.com"));
        _p = new Principal(){
            @Override public String getName() {
                return _u.id().toString();
            }
        };
        _repository = createStrictMock(Repository.class);
        _audit = createStrictMock(AuditLog.class);
        _um = new UserManagerImpl(_repository);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _audit = null;
        _u = null;
        _uDelta = null;
        _p = null;
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
