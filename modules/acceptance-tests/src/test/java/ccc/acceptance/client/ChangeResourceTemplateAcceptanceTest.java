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

import ccc.acceptance.client.views.ChangeResourceTemplateFake;
import ccc.api.core.ResourceSummary;
import ccc.client.presenters.ChangeResourceTemplatePresenter;
import ccc.client.views.ChangeResourceTemplate;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeResourceTemplateAcceptanceTest
    extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testChangeTemplateSuccess() {

        // ARRANGE
        ChangeResourceTemplate view = new ChangeResourceTemplateFake();
        ResourceSummary model = tempFolder();
        getCommands().lock(model.getId());
        ResourceSummary template = dummyTemplate(model);
        view.setSelectedTemplate(template.getId());
        model.setTemplateId(template.getId());

        ChangeResourceTemplatePresenter p =
            new ChangeResourceTemplatePresenter(view, model, null);

        // ACT
        p.save();

        // ASSERT
        ResourceSummary folder = getCommands().retrieve(model.getId());
        assertEquals(template.getId(), folder.getTemplateId());
    }
}
