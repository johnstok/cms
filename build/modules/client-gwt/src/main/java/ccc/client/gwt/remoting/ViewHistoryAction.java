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

import java.util.ArrayList;
import java.util.Collection;

import ccc.api.core.Revision;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.HistoryDialog;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

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
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                RequestBuilder.GET,
                "api/secure"+_selectionModel.tableSelection().revisionsPath(),
                "",
                new ResponseHandlerAdapter(UI_CONSTANTS.viewHistory()){
                    /** {@inheritDoc} */
                    @Override public void onOK(final Response response) {
                        final JSONArray result = JSONParser.parse(response.getText()).isArray();
                        final Collection<Revision> history =
                            new ArrayList<Revision>();
                        for (int i=0; i<result.size(); i++) {
                            history.add(
                                new Revision(new GwtJson(result.get(i).isObject())));
                        }

                        new HistoryDialog(
                            history, _selectionModel.tableSelection().getId(), _selectionModel)
                        .show();
                    }
                });
    }
}
