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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.acceptance.client.views.CreatePageFake;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.presenters.CreatePagePresenter;
import ccc.client.views.CreatePage;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link CreatePagePresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testCreatePageSuccess() {

        // ARRANGE

        final Folder model = tempFolder();
        final Template testTemplate = dummyTemplate(model);

        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        paragraphs.add(Paragraph.fromText("content", "sample text"));

        final CreatePage view = new CreatePageFake(
            "testname"+UUID.randomUUID().toString(),
            "a title",
            true,
            "testComment",
            paragraphs,
            getTemplates().retrieve(testTemplate.getId()));

        final CreatePagePresenter p = new CreatePagePresenter(view, model);

        // ACT
        p.save();

        // ASSERT
        final Resource pr = getCommands().resourceForPath(
            model.getAbsolutePath()+"/"+view.getName());

        final Page page = getPages().retrieve(pr.getId());

        assertEquals(view.getName(), pr.getName().toString());
        assertEquals(view.getResourceTitle(), pr.getTitle());
        assertEquals("sample text", page.getParagraph("content").getText());

    }

}
