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

import ccc.acceptance.client.views.RenameResourceFake;
import ccc.api.core.ResourceSummary;
import ccc.client.presenters.RenameResourcePresenter;
import ccc.client.views.RenameResource;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link RenameResourcePresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testRenameSuccess() {

        // ARRANGE
        ResourceSummary model = tempFolder();
        getCommands().lock(model.getId());
        RenameResource view = new RenameResourceFake();
        String renamed = model.getName()+"2";
        model.setName(renamed);
        view.setName(renamed);

        RenameResourcePresenter p = new RenameResourcePresenter(view, model);

        // ACT
        p.save();

        // ASSERT
        ResourceSummary resource = getCommands().retrieve(model.getId());
        assertEquals(renamed, resource.getName());
    }
}
