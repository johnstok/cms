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
package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.Action;
import ccc.contentcreator.core.SingleSelectionModel;

import com.google.gwt.user.client.Window;

/**
 * Open a dialog to preview the selected resource.
 *
 * @author Civic Computing Ltd.
 */
public final class PreviewAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private final boolean _useWorkingCopy;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param useWorkingCopy Boolean for working copy preview.
     */
    public PreviewAction(final SingleSelectionModel selectionModel,
                         final boolean useWorkingCopy) {
        _selectionModel = selectionModel;
        _useWorkingCopy = useWorkingCopy;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        final String url =
            GLOBALS.appURL()
                + "preview"
                + item.getAbsolutePath()
                + ((_useWorkingCopy) ? "?wc" : "");

        Window.open(
            url,
            "ccc_preview",
            "menubar=no,"
            + "width=640,"
            + "height=480,"
            + "location=yes,"
            + "toolbar=no,"
            + "resizable=yes,"
            + "scrollbars=yes,"
            + "status=no");
    }
}
