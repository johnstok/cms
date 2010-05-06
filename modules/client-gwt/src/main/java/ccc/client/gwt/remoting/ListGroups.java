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

import ccc.api.core.GroupCollection;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Retrieve a list of groups from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListGroups
    extends
        RemotingAction {

    private int _pageNo;
    private int _pageSize;
    private String _sort;
    private String _order; // FIXME: Should be type SortOrder.


    /**
     * Constructor.
     *
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @param sort The column to sort.
     * @param order The sort order (ASC/DESC).
     */
    public ListGroups(final int pageNo,
                      final int pageSize,
                      final String sort,
                      final String order) {
        _pageNo = pageNo;
        _pageSize = pageSize;
        _sort = sort;
        _order = order;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + GLOBALS.groups().list(_pageNo, _pageSize, _sort, _order);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return new Request(
            RequestBuilder.GET,
            getPath(),
            "",
            new ResponseHandlerAdapter(USER_ACTIONS.unknownAction()){
                /** {@inheritDoc} */
                @Override
                public void onOK(final Response response) {
                    final JSONObject obj =
                        JSONParser.parse(response.getText()).isObject();
                    final GroupCollection groups = new GroupCollection();
                    groups.fromJson(new GwtJson(obj));

                    execute(groups);
                }
            });
    }


    /**
     * Handle the result of a successful call.
     *
     * @param groups The groups returned.
     */
    protected abstract void execute(GroupCollection groups);
}
