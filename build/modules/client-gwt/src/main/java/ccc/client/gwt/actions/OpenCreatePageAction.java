/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.actions;

import java.util.Collection;

import ccc.api.core.TemplateDto;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.Action;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.presenters.CreatePagePresenter;
import ccc.client.gwt.remoting.GetTemplatesAction;
import ccc.client.gwt.views.gxt.CreatePageDialog;

/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreatePageAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreatePageAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(UI_CONSTANTS.noFolderSelected());
            return;
        }
        new GetTemplatesAction(UI_CONSTANTS.createPage()){
            @Override protected void execute(
                                 final Collection<TemplateDto> templates) {
                new CreatePagePresenter(
                    GLOBALS,
                    new CreatePageDialog(templates, item),
                    item);
            }
        }.execute();
    }
}
