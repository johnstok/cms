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
package ccc.client.actions;

import ccc.api.core.ResourceSummary;
import ccc.client.callbacks.WCClearedCallback;
import ccc.client.commands.ClearWcCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;
import ccc.client.core.SingleSelectionModel;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model for this action.
     */
    public ClearWorkingCopyAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        final ResourceSummary rs = _selectionModel.tableSelection();
        new ClearWcCommand().invoke(
            rs,
            new WCClearedCallback(
                I18n.uiConstants.deleteWorkingCopy(),
                rs));
    }
}
