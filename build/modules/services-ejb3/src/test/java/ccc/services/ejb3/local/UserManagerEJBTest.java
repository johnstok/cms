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
package ccc.services.ejb3.local;


import static org.easymock.EasyMock.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.EJBContext;

import junit.framework.TestCase;
import ccc.commons.EmailAddress;
import ccc.domain.CreatorRoles;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.Dao;
import ccc.services.api.ID;
import ccc.services.api.UserDelta;
import ccc.services.api.Username;


/**
 * Tests for the {@link UserManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testLoggedInUserHandlesAnonymousUsers() {

        // ARRANGE
        expect(_context.getCallerPrincipal())
            .andThrow(new IllegalStateException());
        replay(_context, _em);

        // ACT
        final User actual = _um.loggedInUser();

        // ASSERT
        verify(_context, _em);
        assertNull(actual);
    }

    /**
     * Test.
     */
    public void testLoggedInUser() {

        // ARRANGE
        expect(_context.getCallerPrincipal()).andReturn(_p);
        expect(_em.find("usersWithUsername", User.class, _p.getName()))
            .andReturn(_u);
        replay(_context, _em);

        // ACT
        final User actual = _um.loggedInUser();

        // ASSERT
        verify(_context, _em);
        assertNotNull("Shouldn't be null.", actual);
        assertEquals(_u, actual);
    }

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnTrue() {

        // ARRANGE
        expect(_em.exists("usersWithUsername", User.class, "blat"))
            .andReturn(Boolean.TRUE);
        replay(_context, _em);

        // ACT
        final boolean actual = _um.usernameExists("blat");

        // ASSERT
        assertEquals(true, actual);
        verify(_context, _em);
    }

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnFalse() {

        // ARRANGE
        expect(_em.exists("usersWithUsername", User.class, "blat"))
            .andReturn(Boolean.FALSE);
        replay(_context, _em);


        // ACT
        final boolean actual = _um.usernameExists("blat");

        // ASSERT
        assertEquals(false, actual);
        verify(_context, _em);
    }

    /**
     * Test.
     * TODO: Test the actual values of the created user.
     */
    public void testCreateUser() {

        // ARRANGE
        _em.create(isA(User.class));
        _em.create(isA(Password.class));
        replay(_context, _em);

        // ACT
        _um.createUser(_uDelta, "foopass");

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     */
    public void testListUsers() {

        // ARRANGE
        expect(_em.uniquify("users", User.class))
            .andReturn(new ArrayList<User>());
        replay(_context, _em);


        // ACT
        _um.listUsers();

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithRole() {

        // ARRANGE
        expect(_em.uniquify("usersWithRole",
                            User.class,
                            CreatorRoles.ADMINISTRATOR))
            .andReturn(new ArrayList<User>());
        replay(_context, _em);

        // ACT
        _um.listUsersWithRole(CreatorRoles.ADMINISTRATOR);

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithUsername() {

        // ARRANGE
        expect(_em.list("usersWithUsername", User.class, "testname"))
            .andReturn(new ArrayList<User>());
        replay(_context, _em);

        // ACT
        _um.listUsersWithUsername("testname");

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithEmail() {

        // ARRANGE
        expect(_em.list("usersWithEmail", User.class, "test@civicuk.com"))
            .andReturn(new ArrayList<User>());
        replay(_context, _em);

        // ACT
        _um.listUsersWithEmail("test@civicuk.com");

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     * TODO: Test the actual values of the created user.
     */
    public void testUpdateUser() {

        // ARRANGE
        expect(_em.find(User.class, _u.id())).andReturn(_u);
        replay(_context, _em);

        // ACT
        _um.updateUser(_uDelta);

        // ASSERT
        verify(_context, _em);

    }

    /**
     * Test.
     */
    public void testUpdateUserPassword() {

        // ARRANGE
        final Password pw = new Password(_u, "foo");

        expect(_em.find("passwordForUser", Password.class, _u.id()))
            .andReturn(pw);
        replay(_context, _em);

        // ACT
        _um.updatePassword(_u.id(), "newPass");

        // ASSERT
        verify(_context, _em);
        assertTrue(pw.matches("newPass"));
    }

    private User _u;
    private UserDelta _uDelta;
    private Dao _em;
    private EJBContext _context;
    private Principal _p;
    private UserManagerEJB _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _u = new User("testUser");
        _uDelta =
            new UserDelta(
                new ID(_u.id().toString()),
                "test@civicuk.com",
                new Username("testUser"),
                new HashSet<String>());
        _u.email(new EmailAddress("test@civicuk.com"));
        _p = new Principal(){
            @Override public String getName() {
                return _u.id().toString();
            }
        };
        _em = createStrictMock(Dao.class);
        _context = createStrictMock(EJBContext.class);
        _um = new UserManagerEJB(_em, _context);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _u = null;
        _uDelta = null;
        _p = null;
        _em = null;
        _context = null;
        _um = null;
    }
}
