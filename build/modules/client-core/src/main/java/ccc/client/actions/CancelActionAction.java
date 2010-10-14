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

import ccc.api.core.ActionSummary;
import ccc.api.types.ActionStatus;
import ccc.client.callbacks.ActionCancelledCallback;
import ccc.client.commands.CancelActionCommand;
import ccc.client.core.Action;
import ccc.client.core.HasSelection;
import ccc.client.core.InternalServices;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    implements
        Action {

    private final HasSelection<ActionSummary> _table;


    /**
     * Constructor.
     *
     * @param table The action table to work with.
     */
    public CancelActionAction(final HasSelection<ActionSummary> table) {
        _table = table;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        final ActionSummary action = _table.getSelectedItem();

        if (null==action) {
            InternalServices.window.alert(
                UI_CONSTANTS.pleaseChooseAnAction());

        } else if (
            ActionStatus.SCHEDULED != action.getStatus()) {
            InternalServices.window.alert(
                UI_CONSTANTS.thisActionHasAlreadyCompleted());

        } else {
            new CancelActionCommand().invoke(
                action,
                new ActionCancelledCallback(action));
        }
    }
}
