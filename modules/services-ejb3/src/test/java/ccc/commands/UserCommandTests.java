/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import static org.easymock.EasyMock.*;

import java.util.Date;

import ccc.api.dto.UserDto;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.types.Username;


/**
 * Tests for user commands.
 *
 * @author Civic Computing Ltd.
 */
public class UserCommandTests
    extends
        AbstractCommandTest {

    /**
     * Test.
     *
     */
    public void testCreateUser() {

        // ARRANGE
        final Date now = new Date();
        _um.create(isA(User.class));
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final CreateUserCommand cu =
            new CreateUserCommand(_repoFactory);

        // ACT
        final User u = cu.execute(_user, now, _uDelta);

        // ASSERT
        verifyAll();
        assertEquals("newNameUser", u.getUsername().toString());
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
        expect(_um.find(_user.getId())).andReturn(_user);
        _audit.record(isA(LogEntry.class)); // TODO: Capture and test values.
        replayAll();

        final UpdateUserCommand uu =
            new UpdateUserCommand(_repoFactory, _user.getId(), _uDelta);

        // ACT
        uu.execute(_user, now);

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

        expect(_um.find(_user.getId())).andReturn(_user);
        _audit.record(isA(LogEntry.class));
        replayAll();

        final UpdatePasswordAction up =
            new UpdatePasswordAction(_repoFactory);

        // ACT
        up.execute(_user, now, _user.getId(), "newPass");

        // ASSERT
        verifyAll();
        assertTrue(_user.hasPassword("newPass"));
    }


    /** {@inheritDoc} */
    @Override protected void setUp() throws Exception {
        super.setUp();
        _uDelta = new UserDto();
        _uDelta.setEmail("new.email@civicuk.com");
        _uDelta.setUsername(new Username("newNameUser"));
        _uDelta.setName("newNameUser");
        _uDelta.setPassword("foopass");
    }


    /** {@inheritDoc} */
    @Override protected void tearDown() throws Exception {
        super.tearDown();
        _uDelta = null;
    }


    private UserDto _uDelta;
}
