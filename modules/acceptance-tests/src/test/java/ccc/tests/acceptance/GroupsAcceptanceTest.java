/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.UUID;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.types.SortOrder;


/**
 * Tests for the manipulating groups.
 *
 * @author Civic Computing Ltd.
 */
public class GroupsAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testCreateGroup() throws Exception {

        // ARRANGE
        final UUID name = UUID.randomUUID();
        final Group g = new Group();
        g.setName(name.toString());
        g.setPermissions(Collections.singleton("bar"));

        // ACT
        final Group actual = getGroups().create(g);

        // ASSERT
        assertEquals(name.toString(), actual.getName());
        assertEquals(Collections.singleton("bar"), actual.getPermissions());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUpdateGroup() throws Exception {

        // ARRANGE
        final UUID name = UUID.randomUUID();
        final Group g = new Group();
        g.setName(name.toString());
        final Group created = getGroups().create(g);

        // ACT
        final String newName = UUID.randomUUID().toString();
        created.setName(newName);
        created.setPermissions(Collections.singleton("foo"));
        final Group actual = getGroups().update(created.getId(), created);

        // ASSERT
        assertEquals(newName.toString(), actual.getName());
        assertEquals(1, actual.getPermissions().size());
        assertEquals("foo", actual.getPermissions().iterator().next());
    }


    /**
     * Test.
     */
    public void testListGroupByName() {

        // ARRANGE

        // ACT
        final PagedCollection<Group> actual =
            getGroups().query("ADMINISTRATOR", "name", SortOrder.ASC, 1, 20);

        // ASSERT
        assertEquals(1, actual.getTotalCount());
        assertEquals("ADMINISTRATOR",
            actual.getElements().iterator().next().getName());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFindGroup() throws Exception {

        // ARRANGE
        final UUID name = UUID.randomUUID();
        final Group g = new Group();
        g.setName(name.toString());
        final Group created = getGroups().create(g);

        // ACT
        final Group actual = getGroups().retrieve(created.getId());

        // ASSERT
        assertEquals(name.toString(), actual.getName());
        assertEquals(0, actual.getPermissions().size());
    }
}
