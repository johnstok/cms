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

import java.util.UUID;

import ccc.acceptance.client.views.CreateFolderFake;
import ccc.api.core.Resource;
import ccc.client.presenters.CreateFolderPresenter;
import ccc.client.views.CreateFolder;
import ccc.tests.acceptance.AbstractAcceptanceTest;

/**
 * Tests for the {@link CreateFolderPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderAcceptanceTest extends AbstractAcceptanceTest {


    /**
     * Test.
     *
     */
    public void testCreateFolderSuccess() {

        // ARRANGE
        final String name = "testFolder"+UUID.randomUUID().toString();
        final Resource model = getCommands().resourceForPath("");
        final CreateFolder view = new CreateFolderFake();
        view.setName(name);
        final CreateFolderPresenter p = new CreateFolderPresenter(view, model);

        // ACT
        p.save();

        // ASSERT
        final Resource folder = getCommands().resourceForPath("/"+name);
        assertNotNull("Folder should exist", folder);
        assertEquals(name, folder.getName().toString());
    }
}
