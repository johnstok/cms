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

import ccc.acceptance.client.views.CreateAliasFake;
import ccc.api.core.Folder;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.client.presenters.CreateAliasPresenter;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreateAliasPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testCreateAliasSuccess() {

        // ARRANGE
        final String exists =
            getCommands()
            .resourceForPath("")
            .getLinks()
            .get(Folder.Links.EXISTS);
        final ResourceSummary rs = new ResourceSummary();
        rs.addLink(Folder.Links.EXISTS, exists);
        final Resource welcome = getCommands().resourceForPath("/welcome");

        final CreateAliasPresenter p =
            new CreateAliasPresenter(
                new CreateAliasFake("welcome", rs),
                welcome);

        // ACT
        p.save();

        // ASSERT
        assertEquals(
            "A resource with name 'welcome' already exists in this folder.",
            getWindow().getLatestAlert());
    }
}
