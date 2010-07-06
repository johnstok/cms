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

import ccc.api.core.ActionSummary;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.client.gwt.widgets.ActionTable;

import com.extjs.gxt.ui.client.data.BeanModel;


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
        final BeanModel action = _table.getSelectedItem();
        if (null==action) {
            InternalServices.WINDOW.alert(
                UI_CONSTANTS.pleaseChooseAnAction());
            return false;
        } else if (
            ActionStatus.SCHEDULED!=
                action.<ActionSummary>getBean().getStatus()) {
            InternalServices.WINDOW.alert(
                UI_CONSTANTS.thisActionHasAlreadyCompleted());
            return false;
        }
        return true;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return cancel(_table.getSelectedItem().<ActionSummary>getBean());
    }


    /**
     * Cancel an action.
     *
     * @return The HTTP request to cancel this action.
     */
    public Request cancel(final ActionSummary action) {
        final String path = Globals.API_URL + action.self();
        return
            new Request(
                HttpMethod.DELETE,
                path,
                "",
                new ActionCancelledCallback(action));
    }


    /**
     * Callback handler for applying a working copy.
     *
     * @author Civic Computing Ltd.
     */
    public static class ActionCancelledCallback extends ResponseHandlerAdapter {

        private final Event<CommandType> _event;

        /**
         * Constructor.
         *
         * @param action The resource whose WC has been applied.
         */
        public ActionCancelledCallback(final ActionSummary action) {
            super(I18n.UI_CONSTANTS.cancel());
            _event = new Event<CommandType>(CommandType.ACTION_CANCEL);
            _event.addProperty("action", action);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final ccc.client.core.Response response) {
            InternalServices.REMOTING_BUS.fireEvent(_event);
        }
    }
}
