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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import ccc.api.core.ResourceSummary;
import ccc.client.callbacks.WCAppliedCallback;
import ccc.client.commands.ApplyWorkingCopyCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;
import ccc.client.core.SingleSelectionModel;


/**
 * Applies working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ApplyWorkingCopyAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        final ResourceSummary rs = _selectionModel.tableSelection();

        new ApplyWorkingCopyCommand().invoke(
            rs,
            new WCAppliedCallback(I18n.uiConstants.applyWorkingCopy(), rs));
    }
}
