/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import static org.easymock.EasyMock.*;

import java.security.Principal;

import junit.framework.TestCase;
import ccc.domain.User;


/**
 * Tests for the {@link UserLookup} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserLookupTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testLoggedInUserHandlesAnonymousUsers() {

        // ARRANGE
        final Principal p = new Principal() {
            @Override public String getName() { return "foo"; }};
        expect(_dao.find(QueryNames.USERS_WITH_USERNAME, User.class, "foo"))
            .andReturn(null);
        replay(_dao);
        final UserLookup ul = new UserLookup(_dao);

        // ACT
        final User actual = ul.loggedInUser(p);

        // ASSERT
        verify(_dao);
        assertNull(actual);
    }

    /**
     * Test.
     */
    public void testLoggedInUser() {

        // ARRANGE
        final User u = new User("user");
        final Principal p = new Principal() {
            @Override public String getName() { return "user"; }};
        expect(_dao.find(QueryNames.USERS_WITH_USERNAME, User.class, "user"))
            .andReturn(u);
        replay(_dao);
        final UserLookup ul = new UserLookup(_dao);

        // ACT
        final User actual = ul.loggedInUser(p);

        // ASSERT
        verify(_dao);
        assertNotNull("Shouldn't be null.", actual);
        assertEquals(u, actual);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _dao = null;
    }

    private Dao _dao;
}
