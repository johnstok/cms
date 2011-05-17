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
import ccc.acceptance.client.views.EditTextFileFake;
import ccc.api.core.File;
import ccc.api.core.Folder;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.client.presenters.EditTextFilePresenter;
import ccc.client.views.EditTextFile;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link EditTextFilePresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testEditTextFileSuccess() {

        // ARRANGE
        final Folder parent = tempFolder();
        final EditTextFile view =
            new EditTextFileFake("", "", "", "", false);

        final File textFile =
            new File(
                parent.getId(),
                "content",
                new MimeType("text", "html"),
                true,
                "none",
                "nocontent");
        textFile.setName(
            new ResourceName("testFile"+UUID.randomUUID().toString()));
        final File fileRs = getFiles().createTextFile(textFile);
        getCommands().lock(fileRs.getId());
        final EditTextFilePresenter p =
            new EditTextFilePresenter(
                view, getFiles().retrieve(fileRs.getId()));

        view.setText("another text");
        view.setSubMime("plain");
        view.setPrimaryMime("application");

        // ACT
        p.save();

        // ASSERT
        final File file = getFiles().retrieve(fileRs.getId());
        assertEquals("plain", file.getMimeType().getSubType());
        assertEquals("application", file.getMimeType().getPrimaryType());
        assertEquals("another text", file.getContent());

    }

}
