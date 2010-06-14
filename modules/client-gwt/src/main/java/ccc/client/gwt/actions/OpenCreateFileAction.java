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

import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.Action;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.UploadFileDialog;
import ccc.client.gwt.widgets.ContentCreator;

/**
 * Create a file.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateFileAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateFileAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData parent = _selectionModel.treeSelection();
        if (parent == null) {
            ContentCreator.WINDOW.alert(UI_CONSTANTS.noFolderSelected());
        } else {
            new UploadFileDialog(parent, _selectionModel).show();
        }
    }
}
