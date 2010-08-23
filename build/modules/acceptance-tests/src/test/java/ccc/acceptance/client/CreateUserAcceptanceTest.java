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
 * Revision      $Rev: 2979 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-07-12 10:12:56 +0100 (Mon, 12 Jul 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.acceptance.client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.acceptance.client.views.CreateUserFake;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.types.SortOrder;
import ccc.client.presenters.CreateUserPresenter;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreateUserPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserAcceptanceTest extends AbstractAcceptanceTest {

    private final String _username = "username"+UUID.randomUUID().toString();

    /**
     * Test.
     */
    public void testCreateUserSuccess() {
        Set<UUID> groups = new HashSet<UUID>();
        // ARRANGE
        CreateUserPresenter p =
            new CreateUserPresenter(
                new CreateUserFake("testName",
                   _username ,
                   "PassWord22-",
                   "PassWord22-",
                   groups,
                   "test@test.tt"));

        // ACT
        p.save();

        // ASSERT
        PagedCollection<User> users = getUsers().query(_username,
            null,
            null,
            null,
            null,
            "name",
            SortOrder.ASC,
            1,
            1);

        assertEquals(1, users.getTotalCount());
        assertEquals(_username,
            users.getElements().get(0).getUsername().toString());
    }

    // TODO: Implement after the 'user already exists' validation has been
    // moved to the presenter.
    // public void testCreateUserAlreadyExists()

}
