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
package ccc.client.remoting;

import ccc.api.core.ActionSummary;
import ccc.api.types.ActionStatus;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HasSelection;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    extends
        RemotingAction<Void> {

    private final HasSelection<ActionSummary> _table;


    /**
     * Constructor.
     *
     * @param table The action table to work with.
     */
    public CancelActionAction(final HasSelection<ActionSummary> table) {
        super(I18n.UI_CONSTANTS.cancel());
        _table = table;
    }


    /** {@inheritDoc} */
    @Override protected boolean beforeExecute() {
        final ActionSummary action = _table.getSelectedItem();
        if (null==action) {
            InternalServices.WINDOW.alert(
                UI_CONSTANTS.pleaseChooseAnAction());
            return false;
        } else if (
            ActionStatus.SCHEDULED != action.getStatus()) {
            InternalServices.WINDOW.alert(
                UI_CONSTANTS.thisActionHasAlreadyCompleted());
            return false;
        }
        return true;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Void> callback) {
        final ActionSummary action = _table.getSelectedItem();

        final String path = Globals.API_URL + action.self();
        return
            new Request(
                HttpMethod.DELETE,
                path,
                "",
                new CallbackResponseHandler<Void>(
                    I18n.UI_CONSTANTS.cancel(),
                    callback,
                    new Parser<Void>() {
                        @Override public Void parse(final Response response) {
                            return null;
                        }}));
    }
}
