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

import ccc.api.core.UserCollection;
import ccc.api.core.UserCriteria;
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
 * List available users.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListUsersAction
    extends
        RemotingAction {

    private UserCriteria _uc;
    private int _pageNo;
    private int _pageSize;
    private String _sort;
    private String _order; // FIXME: Should be type SortOrder.

    /**
     * Constructor.
     *
     * @param uc The UserCriteria object.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @param sort The column to sort.
     * @param order The sort order (ASC/DESC).
     */
    public ListUsersAction(final UserCriteria uc,
                     final int pageNo,
                     final int pageSize,
                     final String sort,
                     final String order) {
        _uc = uc;
        _pageNo = pageNo;
        _pageSize = pageSize;
        _sort = sort;
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        String email    = null;
        String username = null;
        String groups   = null;
        if (null != _uc) {
            if (null != _uc.getEmail()) {
                email=encode(_uc.getEmail());
            }
            if (null != _uc.getUsername()) {
                username=encode(_uc.getUsername());
            }
            if (null != _uc.getGroups()) {
                groups=encode(_uc.getGroups());
            }
        }

        return
            Globals.API_URL
            + GLOBALS.users().list(
                _pageNo,
                _pageSize,
                _sort,
                _order,
                email,
                username,
                groups);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                RequestBuilder.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(USER_ACTIONS.viewUsers()) {
                    /** {@inheritDoc} */
                    @Override public void onOK(final Response response) {

                        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

                        final UserCollection uc = new UserCollection();
                        uc.fromJson(new GwtJson(obj));

                        execute(uc);
                    }
                });
    }

    /**
     * Handle the result of a successful call.
     *
     * @param users The collection of users returned.
     * @param totalCount The total comments available on the server.
     */
    protected abstract void execute(UserCollection users);
}
