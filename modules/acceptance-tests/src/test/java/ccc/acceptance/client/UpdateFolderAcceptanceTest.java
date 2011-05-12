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

import ccc.acceptance.client.views.UpdateFolderFake;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.client.presenters.UpdateFolderPresenter;
import ccc.client.views.UpdateFolder;
import ccc.tests.acceptance.AbstractAcceptanceTest;

/**
 * Tests for the {@link UpdateFolderPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderAcceptanceTest extends AbstractAcceptanceTest {


    /**
     * Test.
     *
     */
    public void testCreateFolderSuccess() {

        // ARRANGE
        final Folder f = tempFolder();
        final Template template = dummyTemplate(f);
        tempPage(f.getId(), template.getId());
        final Page p = tempPage(f.getId(), template.getId());
        tempPage(f.getId(), template.getId());
        getCommands().lock(f.getId());

        final UpdateFolder view = new UpdateFolderFake();
        final UpdateFolderPresenter presenter =
            new UpdateFolderPresenter(view, f);
        view.setIndexPage(p.getId());

        // ACT
        presenter.save();

        // ASSERT
        final ResourceSummary folder = getCommands().retrieve(f.getId());
        assertNotNull("Folder should exist", folder);
        assertEquals(p.getId(), folder.getIndexPageId());
    }
}
