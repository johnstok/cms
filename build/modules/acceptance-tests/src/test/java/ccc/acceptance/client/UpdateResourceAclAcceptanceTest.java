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
import java.util.Collection;

import ccc.acceptance.client.views.UpdateResourceAclFake;
import ccc.api.core.ACL;
import ccc.api.core.Folder;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.UnauthorizedException;
import ccc.client.presenters.UpdateResourceAclPresenter;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link UpdateResourceAclPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testUpdateAclSuccess() {

        // ARRANGE
        final Folder f = tempFolder();
        getCommands().lock(f.getId());

        final ACL acl = new ACL();
        final User u = getUsers().retrieveCurrent();
        final Collection<Entry> users = new ArrayList<Entry>();
        final Entry e = new Entry();
        e.setName(u.getName());
        e.setPrincipal(u.getId());
        e.setReadable(false);
        e.setWriteable(false);
        users.add(e);
        acl.setUsers(users);
        final UpdateResourceAclFake view = new UpdateResourceAclFake(acl , f);

        final UpdateResourceAclPresenter presenter =
            new UpdateResourceAclPresenter(view, f);

        // ACT
        presenter.save();

        // ASSERT
        try {
            getCommands().retrieve(f.getId());
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(f.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }
}
