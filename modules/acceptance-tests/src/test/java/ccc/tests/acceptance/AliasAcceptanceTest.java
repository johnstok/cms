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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ACL;
import ccc.api.core.Alias;
import ccc.api.core.Folder;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.ResourceName;


/**
 * Tests for the manipulating resources.
 *
 * @author Civic Computing Ltd.
 */
public class AliasAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Alias rs = tempAlias();

        // ACT
        getCommands().lock(rs.getId());
        getAliases().update(rs.getId(), new Alias(folder.getId()));

        // ASSERT
        final String targetName = getAliases().aliasTargetName(rs.getId());
        assertEquals(targetName, folder.getName().toString());
    }


    /**
     * Test.
     */
    public void testCreateAlias() {

        final String name = UUID.randomUUID().toString();
        final Folder folder = tempFolder();
        final Alias alias =
            new Alias(
                folder.getId(), new ResourceName(name), folder.getId());

        // ACT
        final Alias rs = getAliases().create(alias);

        // ASSERT
        final String targetName = getAliases().aliasTargetName(rs.getId());
        assertEquals(name, rs.getName().toString());
        assertEquals(folder.getId(), rs.getParent());
        assertEquals(targetName, folder.getName().toString());
    }

    /**
     * Test.
     */
    public void testUpdateAliasFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Alias rs = tempAlias();

        getCommands().lock(rs.getId());

        final ACL acl = new ACL();
        final List<Entry> users = new ArrayList<Entry>();
        final Entry e = new Entry();
        final User u = getUsers().retrieveCurrent();
        e.setName(u.getName());
        e.setPrincipal(u.getId());
        e.setReadable(true);
        e.setWriteable(false);
        users.add(e);
        acl.setUsers(users);
        getCommands().changeAcl(rs.getId(), acl);

        // ACT
        try {
            getAliases().update(rs.getId(), new Alias(folder.getId()));
            // ASSERT
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(rs.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }

    }

}
