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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.acceptance.client.views.CreateActionFake;
import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.core.Globals;
import ccc.client.presenters.CreateActionPresenter;
import ccc.client.views.CreateAction;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreateActionPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testCreateActionSuccess() {

        // ARRANGE
        Map<String, String> params = new HashMap<String, String>();
        CreateAction view = new CreateActionFake(
            new Date(),
            CommandType.RESOURCE_PUBLISH,
            params);

        ResourceSummary model = tempFolder();
        getCommands().lock(model.getId());

        CreateActionPresenter p =  new CreateActionPresenter(view, model);

        // ACT
        p.save();

        // ASSERT
        PagedCollection<ActionSummary> list = getActions().listPendingActions(
                null,
                SortOrder.ASC,
                1,
                Globals.MAX_FETCH);

        assertNotNull("Pending actions list should not be null", list);
        ActionSummary found = null;
        for (ActionSummary action : list.getElements()) {
            if (action.getSubjectPath().equals(model.getAbsolutePath())) {
                found = action;
            }
        }

        assertNotNull("Action should be in list of pending actions", found);
        assertEquals(ResourceType.FOLDER, found.getSubjectType());
        assertEquals(model.getAbsolutePath(), found.getSubjectPath());
    }
}
