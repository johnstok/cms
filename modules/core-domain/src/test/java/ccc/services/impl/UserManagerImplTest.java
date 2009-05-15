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
import java.util.HashSet;

import junit.framework.TestCase;
import ccc.api.UserDelta;
import ccc.api.Username;
import ccc.commands.CreateUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.commons.EmailAddress;
import ccc.domain.CreatorRoles;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


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
        expect(_dao.exists("usersWithUsername", User.class, "blat"))
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
        expect(_dao.exists("usersWithUsername", User.class, "blat"))
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
        _dao.create(isA(User.class));
        _dao.create(isA(Password.class));
        _audit.recordUserCreate(isA(User.class), eq(_u), eq(now));
        replayAll();

        final CreateUserCommand cu = new CreateUserCommand(_dao, _audit);

        // ACT
        final User u = cu.execute(_u, now, _uDelta, "foopass");

        // ASSERT
        verifyAll();
        assertEquals("newNameUser", u.username());
    }

    /**
     * Test.
     */
    public void testListUsers() {

        // ARRANGE
        expect(_dao.uniquify("users", User.class))
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
        expect(_dao.uniquify("usersWithRole",
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
        expect(_dao.list("usersWithUsername", User.class, "testname"))
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
        expect(_dao.list("usersWithEmail", User.class, "test@civicuk.com"))
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
        expect(_dao.find(User.class, _u.id())).andReturn(_u);
        _audit.recordUserUpdate(_u, _u, now);
        replayAll();

        final UpdateUserCommand uu = new UpdateUserCommand(_dao, _audit);

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

        expect(_dao.find("passwordForUser", Password.class, _u.id()))
            .andReturn(pw);
        _audit.recordUserChangePassword(null, _u, now); // FIXME: Broken.
        replayAll();

        final UpdatePasswordAction up = new UpdatePasswordAction(_dao, _audit);

        // ACT
        up.execute(_u, now, _u.id(), "newPass");

        // ASSERT
        verifyAll();
        assertTrue(pw.matches("newPass"));
    }

    private User _u;
    private AuditLog _audit;
    private UserDelta _uDelta;
    private Dao _dao;
    private Principal _p;
    private UserManagerImpl _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _u = new User("testUser");
        _uDelta =
            new UserDelta(
                "new.email@civicuk.com",
                new Username("newNameUser"),
                new HashSet<String>());
        _u.email(new EmailAddress("test@civicuk.com"));
        _p = new Principal(){
            @Override public String getName() {
                return _u.id().toString();
            }
        };
        _dao = createStrictMock(Dao.class);
        _audit = createStrictMock(AuditLog.class);
        _um = new UserManagerImpl(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _audit = null;
        _u = null;
        _uDelta = null;
        _p = null;
        _dao = null;
        _um = null;
    }

    private void verifyAll() {
        verify(_dao, _audit);
    }

    private void replayAll() {
        replay(_dao, _audit);
    }
}
