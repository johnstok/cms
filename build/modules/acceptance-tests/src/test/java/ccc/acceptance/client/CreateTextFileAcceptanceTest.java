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

import ccc.acceptance.client.views.CreateTextFileFake;
import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.presenters.CreateTextFilePresenter;
import ccc.client.views.CreateTextFile;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreateTextFilePresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTextFileAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testCreateTextFileSuccess() {

        // ARRANGE
        ResourceSummary model = tempFolder();
        CreateTextFile view = new CreateTextFileFake("text text text",
            "file"+UUID.randomUUID().toString(),
            "text",
            "html",
            "test comment",
            true);
        CreateTextFilePresenter p = new CreateTextFilePresenter(view, model);

        // ACT
        p.save();

        // ASSERT
        ResourceSummary rs = getCommands().resourceForPath(
            model.getAbsolutePath()+"/"+view.getName());
        File file = getFiles().retrieve(rs.getId());
        assertEquals("text text text", file.getContent());
        assertEquals("test comment", file.getComment());
        assertEquals("text", file.getMimeType().getPrimaryType());
        assertEquals("html", file.getMimeType().getSubType());

    }
}
