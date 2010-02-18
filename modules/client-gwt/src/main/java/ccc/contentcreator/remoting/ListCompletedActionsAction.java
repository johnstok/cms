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
package ccc.contentcreator.remoting;

import java.util.Collection;

import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.widgets.ActionTable;
import ccc.rest.dto.ActionSummary;

import com.google.gwt.http.client.Response;


/**
 * Display the list of completed actions.
 *
 * @author Civic Computing Ltd.
 */
public class ListCompletedActionsAction
    extends
        RemotingAction {

    private final ActionTable _actionTable;

    /**
     * Constructor.
     *
     * @param actionTable The table to update.
     */
    public ListCompletedActionsAction(final ActionTable actionTable) {
        super(USER_ACTIONS.viewActions());
        _actionTable = actionTable;
    }

    /** {@inheritDoc} */
    @Override protected String getPath() { return "/actions/completed"; }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final Collection<ActionSummary> actions =
            parseActionSummaryCollection(response);
        _actionTable.updatePagingModel(actions);
    }
}
