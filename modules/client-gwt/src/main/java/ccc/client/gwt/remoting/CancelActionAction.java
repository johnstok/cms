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
package ccc.client.gwt.remoting;

import ccc.api.types.ActionStatus;
import ccc.client.gwt.binding.ActionSummaryModelData;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.widgets.ActionTable;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    extends
        RemotingAction {

    private final ActionTable _table;


    /**
     * Constructor.
     *
     * @param table The action table to work with.
     */
    public CancelActionAction(final ActionTable table) {
        _table = table;
    }


    /** {@inheritDoc} */
    @Override protected boolean beforeExecute() {
        final ActionSummaryModelData action = _table.getSelectedItem();
        if (null==action) {
            GLOBALS.alert(UI_CONSTANTS.pleaseChooseAnAction());
            return false;
        } else if (ActionStatus.SCHEDULED!=action.getStatus()) {
            GLOBALS.alert(UI_CONSTANTS.thisActionHasAlreadyCompleted());
            return false;
        }
        return true;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return _table.getSelectedItem().cancel();
    }
}
