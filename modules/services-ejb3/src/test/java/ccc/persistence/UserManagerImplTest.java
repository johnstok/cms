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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ccc.api.core.UserCriteria;
import ccc.api.core.User;
import ccc.api.types.EmailAddress;
import ccc.api.types.Username;
import ccc.domain.UserEntity;


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
            QueryNames.USER_WITH_MATCHING_USERNAME, UserEntity.class, "blat")))
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
                QueryNames.USER_WITH_MATCHING_USERNAME, UserEntity.class, "blat")))
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
        final Map<String, Object> params = new HashMap<String, Object>();
        expect(_repository.listDyn(
            "select u "
            + "from ccc.domain.UserEntity as u",
            UserEntity.class,
            1,
            1,
            params))
            .andReturn(new ArrayList<UserEntity>());
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
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("groups", "ADMINISTRATOR");
        expect(_repository.listDyn(
            "select u from ccc.domain.UserEntity as u where :groups in ("
            + "select r._name "
            + "from ccc.domain.UserEntity as u2 left join u2._groups as r "
            + "where u=u2) ",
            UserEntity.class,
            1,
            1,
            params))
            .andReturn(new ArrayList<UserEntity>());
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
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", "testname");
        expect(_repository.listDyn(
            "select u"
            +" from ccc.domain.UserEntity as u"
            + " where lower(u._username._value) like lower(:username)",
            UserEntity.class,
            1,
            1,
            params))
            .andReturn(new ArrayList<UserEntity>());
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
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", "test@civicuk.com");
        expect(_repository.listDyn(
            "select u"
            + " from ccc.domain.UserEntity as u"
            + " where lower(u._email._text) like lower(:email)",
            UserEntity.class,
            1,
            1,
            params))
            .andReturn(new ArrayList<UserEntity>());
        replayAll();

        // ACT
        final UserCriteria uc = new UserCriteria();
        uc.setEmail("test@civicuk.com");
        _um.listUsers(uc, null, null, 1, 1);

        // ASSERT
        verifyAll();

    }


    private UserEntity _u;
    private User _uDelta;
    private Repository _repository;
    private UserRepositoryImpl _um;

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _u = new UserEntity(new Username("testUser"), "password");
        _u.setEmail(new EmailAddress("test@civicuk.com"));

        _uDelta = new User();
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
