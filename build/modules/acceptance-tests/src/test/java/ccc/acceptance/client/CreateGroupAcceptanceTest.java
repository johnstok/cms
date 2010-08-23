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
package ccc.acceptance.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.acceptance.client.views.GroupViewFake;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.client.presenters.CreateGroupPresenter;
import ccc.client.presenters.GroupPresenter.GroupView;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreateGroupPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateGroupAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testCreateGroupSuccess() {

        // ARRANGE
        GroupView view = new GroupViewFake();
        CreateGroupPresenter p = new CreateGroupPresenter(view);
        view.setName("newGroup"+UUID.randomUUID().toString());
        Set<String> permissions = new HashSet<String>();
        permissions.add("TEST");
        view.setPermissions(permissions);

        // ACT
        p.save();

        // ASSERT
        PagedCollection<Group> groups = getGroups().query(view.getName(), 1, 1);
        assertNotNull("The group collection should not be null", groups);
        assertEquals(view.getName(), groups.getElements().get(0).getName());
        Set<String> list = groups.getElements().get(0).getPermissions();
        assertEquals("TEST", new ArrayList<String>(list).get(0));
    }
}
