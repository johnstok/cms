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
package ccc.client.gwt.remoting;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.SingleSelectionModel;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyAction
    extends
        RemotingAction {

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
    protected Request getRequest() {
        return clearWorkingCopy(_selectionModel.tableSelection());
    }


    private Request clearWorkingCopy(final ResourceSummary rs) {
        return new Request(
            HttpMethod.DELETE,
            Globals.API_URL
                + rs.wc().build(new GWTTemplateEncoder()),
            "",
            new WCClearedCallback(
                I18n.UI_CONSTANTS.deleteWorkingCopy(), rs));
    }


    /**
     * Callback handler for clearing a working copy.
     *
     * @author Civic Computing Ltd.
     */
    private static class WCClearedCallback extends ResponseHandlerAdapter {

        private final Event<CommandType> _event;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param resource The resource whose WC has been applied.
         */
        WCClearedCallback(final String name,
                          final ResourceSummary resource) {
            super(name);
            _event = new Event<CommandType>(CommandType.RESOURCE_CLEAR_WC);
            _event.addProperty("resource", resource);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final ccc.client.core.Response response) {
            InternalServices.REMOTING_BUS.fireEvent(_event);
        }
    }
}
