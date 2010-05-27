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

import ccc.api.core.User;
import ccc.api.types.Username;
import ccc.domain.LogEntry;
import ccc.domain.UserEntity;


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
     */
    public void testCreateUser() {

        // ARRANGE
        final Date now = new Date();
        getUserRepo().create(isA(UserEntity.class));
        getAudit().record(isA(LogEntry.class));
        // TODO: Capture and test values.
        replayAll();

        final CreateUserCommand cu =
            new CreateUserCommand(getRepoFactory());

        // ACT
        final UserEntity u = cu.execute(getUser(), now, _uDelta);

        // ASSERT
        verifyAll();
        assertEquals("newNameUser", u.getUsername().toString());
    }


    /**
     * Test.
     * TODO: Test the actual values of the created user.
     */
    public void testUpdateUser() {

        // ARRANGE
        final Date now = new Date();
        expect(getUserRepo().find(getUser().getId())).andReturn(getUser());
        getAudit().record(isA(LogEntry.class));
        // TODO: Capture and test values.
        replayAll();

        final UpdateUserCommand uu =
            new UpdateUserCommand(getRepoFactory(), getUser().getId(), _uDelta);

        // ACT
        uu.execute(getUser(), now);

        // ASSERT
        verifyAll();

    }


    /**
     * Test.
     */
    public void testUpdateUserPassword() {

        // ARRANGE
        final Date now = new Date();

        expect(getUserRepo().find(getUser().getId())).andReturn(getUser());
        getAudit().record(isA(LogEntry.class));
        replayAll();

        final UpdatePasswordAction up =
            new UpdatePasswordAction(getRepoFactory());

        // ACT
        up.execute(getUser(), now, getUser().getId(), "newPass");

        // ASSERT
        verifyAll();
        assertTrue(getUser().hasPassword("newPass"));
    }


    /** {@inheritDoc} */
    @Override protected void setUp() {
        super.setUp();
        _uDelta = new User();
        _uDelta.setEmail("new.email@civicuk.com");
        _uDelta.setUsername(new Username("newNameUser"));
        _uDelta.setName("newNameUser");
        _uDelta.setPassword("foopass");
    }


    /** {@inheritDoc} */
    @Override protected void tearDown() {
        super.tearDown();
        _uDelta = null;
    }


    private User _uDelta;
}
