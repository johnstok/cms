/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.security.Principal;

import junit.framework.TestCase;
import ccc.domain.User;
import ccc.types.Username;


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
     *
     * @throws Exception If the test fails.
     */
    public void testLoggedInUserHandlesAnonymousUsers() throws Exception {

        // ARRANGE
        final Principal p = new Principal() {
            @Override public String getName() { return "foo"; }};
        expect(_repository.find(
            QueryNames.USERS_WITH_USERNAME, User.class, "foo"))
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
     *
     * @throws Exception If the test fails.
     */
    public void testLoggedInUser() throws Exception {

        // ARRANGE
        final User u = new User(new Username("user"), "password");
        final Principal p = new Principal() {
            @Override public String getName() { return "user"; }};
        expect(_repository.find(
            QueryNames.USERS_WITH_USERNAME, User.class, "user"))
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

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUserWithLegacyId() throws Exception {

        // ARRANGE
        final User u = new User(new Username("user"), "password");
        u.addMetadatum("legacyId", "8");

        expect(_repository.find(
            QueryNames.USERS_WITH_LEGACY_ID, User.class, "8"))
            .andReturn(u);
        replay(_repository);
        final UserRepositoryImpl ul = new UserRepositoryImpl(_repository);

        // ACT
        final User actual = ul.userByLegacyId("8");

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
