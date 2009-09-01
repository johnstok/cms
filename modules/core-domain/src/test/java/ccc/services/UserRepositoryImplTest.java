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
import ccc.persistence.QueryNames;
import ccc.persistence.Repository;
import ccc.persistence.UserRepositoryImpl;


/**
 * Tests for the {@link UserRepositoryImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserRepositoryImplTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testLoggedInUserHandlesAnonymousUsers() {

        // ARRANGE
        final Principal p = new Principal() {
            @Override public String getName() { return "foo"; }};
        expect(_repository.find(QueryNames.USERS_WITH_USERNAME, User.class, "foo"))
            .andReturn(null);
        replay(_repository);
        final UserRepositoryImpl ul = new UserRepositoryImpl(_repository);

        // ACT
        final User actual = ul.loggedInUser(p);

        // ASSERT
        verify(_repository);
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
        expect(_repository.find(QueryNames.USERS_WITH_USERNAME, User.class, "user"))
            .andReturn(u);
        replay(_repository);
        final UserRepositoryImpl ul = new UserRepositoryImpl(_repository);

        // ACT
        final User actual = ul.loggedInUser(p);

        // ASSERT
        verify(_repository);
        assertNotNull("Shouldn't be null.", actual);
        assertEquals(u, actual);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _repository = createStrictMock(Repository.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _repository = null;
    }

    private Repository _repository;
}
