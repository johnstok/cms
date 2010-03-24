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
import java.util.List;

import junit.framework.TestCase;
import ccc.domain.User;
import ccc.rest.dto.UserCriteria;
import ccc.rest.dto.UserDto;
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
            QueryNames.USER_WITH_MATCHING_USERNAME, User.class, "blat")))
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
                QueryNames.USER_WITH_MATCHING_USERNAME, User.class, "blat")))
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
     */
    public void testListUsers() {

        // ARRANGE
        final List<Object> params = new ArrayList<Object>();
        expect(_repository.listDyn(
            "select u "
            + "from ccc.domain.User as u",
            User.class,
            1,
            1,
            params.toArray()))
            .andReturn(new ArrayList<User>());
        replayAll();


        // ACT
        final UserCriteria uc = new UserCriteria();
        _um.listUsers(uc, null, null, 1, 1);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithGroup() {

        // ARRANGE
        final List<Object> params = new ArrayList<Object>();
        params.add("ADMINISTRATOR");
        expect(_repository.listDyn(
            "select u from ccc.domain.User as u where ? in ("
            + "select r._name "
            + "from ccc.domain.User as u2 left join u2._roles as r "
            + "where u=u2) ",
            User.class,
            1,
            1,
            params.toArray()))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        final UserCriteria uc = new UserCriteria();
        uc.setGroups("ADMINISTRATOR");
        _um.listUsers(uc, null, null, 1, 1);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithUsername() {

        // ARRANGE
        final List<Object> params = new ArrayList<Object>();
        params.add("testname");
        expect(_repository.listDyn(
            "select u"
            +" from ccc.domain.User as u"
            + " where lower(u._username._value) like lower(?)",
            User.class,
            1,
            1,
            params.toArray()))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        final UserCriteria uc = new UserCriteria();
        uc.setUsername("testname");
        _um.listUsers(uc, null, null, 1, 1);

        // ASSERT
        verifyAll();

    }

    /**
     * Test.
     */
    public void testListUsersWithEmail() {

        // ARRANGE
        final List<Object> params = new ArrayList<Object>();
        params.add("test@civicuk.com");
        expect(_repository.listDyn(
            "select u"
            + " from ccc.domain.User as u"
            + " where lower(u._email._text) like lower(?)",
            User.class,
            1,
            1,
            params.toArray()))
            .andReturn(new ArrayList<User>());
        replayAll();

        // ACT
        final UserCriteria uc = new UserCriteria();
        uc.setEmail("test@civicuk.com");
        _um.listUsers(uc, null, null, 1, 1);

        // ASSERT
        verifyAll();

    }


    private User _u;
    private UserDto _uDelta;
    private Repository _repository;
    private UserRepositoryImpl _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _u = new User(new Username("testUser"), "password");
        _u.setEmail(new EmailAddress("test@civicuk.com"));

        _uDelta = new UserDto();
        _uDelta.setEmail("new.email@civicuk.com");
        _uDelta.setUsername(new Username("newNameUser"));
        _uDelta.setName("newNameUser");
        _uDelta.setPassword("foopass");

        _repository = createStrictMock(Repository.class);
        _um = new UserRepositoryImpl(_repository);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _u = null;
        _uDelta = null;
        _repository = null;
        _um = null;
    }

    private void verifyAll() {
        verify(_repository);
    }

    private void replayAll() {
        replay(_repository);
    }
}
