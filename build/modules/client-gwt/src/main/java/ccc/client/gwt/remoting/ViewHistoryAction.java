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

import ccc.api.core.PagedCollection;
import ccc.api.core.Revision;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.HistoryDialog;

/**
 * View resource's history.
 *
 * @author Civic Computing Ltd.
 */
public final class ViewHistoryAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ViewHistoryAction(final SingleSelectionModel selectionModel) {
        super(I18n.UI_CONSTANTS.viewHistory());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                HttpMethod.GET,
                Globals.API_URL
                    + _selectionModel.tableSelection().revisionsPath(),
                "",
                new ResponseHandlerAdapter(UI_CONSTANTS.viewHistory()) {
                    /** {@inheritDoc} */
                    @Override public void onOK(
                               final ccc.client.core.Response response) {
                        final PagedCollection<Revision> rsCollection =
                            readRevisionCollection(response);

                        new HistoryDialog(
                            rsCollection.getElements(),
                            _selectionModel.tableSelection().getType(),
                            _selectionModel)
                        .show();
                    }
                });
    }
}
