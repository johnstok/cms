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
package ccc.services.ejb3;


import static org.easymock.EasyMock.*;

import java.security.Principal;
import java.util.ArrayList;

import javax.ejb.EJBContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.CreatorRoles;
import ccc.domain.Password;
import ccc.domain.User;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testLoggedInUser() {

        // ARRANGE
        expect(_context.getCallerPrincipal()).andReturn(_p);
        expect(_em.find(User.class, _u.id())).andReturn(_u);
        replay(_context, _q, _em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        final User actual = um.loggedInUser();

        // ASSERT
        verify(_q, _em, _context);
        assertNotNull("Shouldn't be null.", actual);
        assertEquals(_u, actual);
    }

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnTrue() {

        // ARRANGE
        expect(_q.setParameter("username", "blat")).andReturn(_q);
        expect(_q.getSingleResult()).andReturn(new User("blat"));
        replay(_q);

        expect(
        _em.createQuery(
            UserManagerEJB.NamedQueries.USERS_WITH_USERNAME.queryString()))
            .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        final boolean actual = um.usernameExists("blat");

        // ASSERT
        assertEquals(true, actual);
        verify(_q, _em);
    }

    /**
     * Test.
     */
    public void testUsernameExistsCanReturnFalse() {

        // ARRANGE
        expect(_q.setParameter("username", "blat")).andReturn(_q);
        expect(_q.getSingleResult()).andThrow(new NoResultException());
        replay(_q);

        expect(
        _em.createQuery(
            UserManagerEJB.NamedQueries.USERS_WITH_USERNAME.queryString()))
            .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        final boolean actual = um.usernameExists("blat");

        // ASSERT
        assertEquals(false, actual);
        verify(_q, _em);
    }

    /**
     * Test.
     */
    public void testCreateUser() {

        // ARRANGE
        final User u = new User("fooDummy");
        _em.persist(u);
        _em.persist(isA(Password.class));
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.createUser(u, "foopass");

        // ASSERT
        verify(_em);

    }

    /**
     * Test.
     */
    public void testListUsers() {

        // ARRANGE
        expect(_q.getResultList()).andReturn(new ArrayList<User>());
        replay(_q);

        expect(
        _em.createQuery(
            UserManagerEJB.NamedQueries.ALL_USERS.queryString()))
            .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.listUsers();

        // ASSERT
        verify(_q, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithRole() {

        // ARRANGE
        expect(_q.setParameter("role", CreatorRoles.ADMINISTRATOR.name()))
            .andReturn(_q);
        expect(_q.getResultList()).andReturn(new ArrayList<User>());
        replay(_q);

        expect(
        _em.createQuery(
            UserManagerEJB.NamedQueries.USERS_WITH_ROLE.queryString()))
            .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.listUsersWithRole(CreatorRoles.ADMINISTRATOR);

        // ASSERT
        verify(_q, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithUsername() {

        // ARRANGE
        expect(_q.setParameter("username", "testname"))
        .andReturn(_q);
        expect(_q.getResultList()).andReturn(new ArrayList<User>());
        replay(_q);

        expect(
            _em.createQuery(
                UserManagerEJB.NamedQueries.USERS_WITH_USERNAME.queryString()))
                .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.listUsersWithUsername("testname");

        // ASSERT
        verify(_q, _em);

    }

    /**
     * Test.
     */
    public void testListUsersWithEmail() {

        // ARRANGE
        expect(_q.setParameter("email", "test@civicuk.com"))
        .andReturn(_q);
        expect(_q.getResultList()).andReturn(new ArrayList<User>());
        replay(_q);

        expect(
            _em.createQuery(
                UserManagerEJB.NamedQueries.USERS_WITH_EMAIL.queryString()))
                .andReturn(_q);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.listUsersWithEmail("test@civicuk.com");

        // ASSERT
        verify(_q, _em);

    }

    /**
     * Test.
     */
    public void testUpdateUser() {

        // ARRANGE
        final User u = new User("testUser");
        u.email("test@civicuk.com");
        expect(_em.find(User.class, u.id())).andReturn(u);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em, _context);

        // ACT
        um.updateUser(u);

        // ASSERT
        verify(_em);

    }

    private User _u;
    private EntityManager _em;
    private Query _q;
    private EJBContext _context;
    private Principal _p;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _u = new User("testUser");
        _p = new Principal(){
            @Override public String getName() {
                return _u.id().toString();
            }
        };
        _em = createStrictMock(EntityManager.class);
        _q = createStrictMock(Query.class);
        _context = createStrictMock(EJBContext.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _u = null;
        _p = null;
        _em = null;
        _q = null;
        _context = null;
    }
}
