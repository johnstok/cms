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

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.core.UserCriteria;
import ccc.api.types.Link;
import ccc.api.types.SortOrder;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.plugins.s11n.json.PagedCollectionReader;

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
    private SortOrder _order;

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
                     final SortOrder order) {
        _uc = uc;
        _pageNo = pageNo;
        _pageSize = pageSize;
        _sort = sort;
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("page",  new String[] {""+_pageNo});
        params.put("count", new String[] {""+_pageSize});
        params.put("sort",  new String[] {_sort});
        params.put("order", new String[] {_order.name()});
        if (null != _uc) {
            if (null != _uc.getEmail()) {
                params.put("email", new String[] {_uc.getEmail()});
            }
            if (null != _uc.getUsername()) {
                params.put("username", new String[] {_uc.getUsername()});
            }
            if (null != _uc.getGroups()) {
                params.put("groups", new String[] {_uc.getGroups()});
            }
        }

        return
            Globals.API_URL
            + new Link(GLOBALS.users().getLink("self"))
                .build(params, new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(USER_ACTIONS.viewUsers()) {
                    /** {@inheritDoc} */
                    @Override public void onOK(final Response response) {

                        final JSONObject obj =
                            JSONParser.parse(response.getText()).isObject();

                        final PagedCollection<User> uc =
                            PagedCollectionReader
                            .read(new GwtJson(obj), User.class);

                        execute(uc);
                    }
                });
    }

    /**
     * Handle the result of a successful call.
     *
     * @param users The collection of users returned.
     */
    protected abstract void execute(PagedCollection<User> users);
}
