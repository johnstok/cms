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
package ccc.client.actions;

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.api.types.SortOrder;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Display the list of actions.
 *
 * @author Civic Computing Ltd.
 */
public class ListActionsAction
    extends
        RemotingAction<PagedCollection<ActionSummary>> {

    private final int           _page;
    private final int           _count;
    private final SortOrder     _order;
    private final String        _sort;
    private final String        _status;

    /**
     * Constructor.
     *
     * @param status The status of actions to return.
     * @param page The page of results to return.
     * @param count The number of results in a page.
     * @param sort The field to sort on.
     * @param order The order results be sorted in.
     */
    public ListActionsAction(final String status,
                             final int page,
                             final int count,
                             final String sort,
                             final SortOrder order) {
        super(USER_ACTIONS.viewActions());
        DBC.require().toBeTrue(page>0);
        DBC.require().toBeTrue(count>0);

        _status = status;
        _page = page;
        _count = count;
        _sort = sort;
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("status",  new String[] {_status});
        params.put("page",  new String[] {""+_page});
        params.put("count", new String[] {""+_count});
        params.put("sort",  new String[] {_sort});
        params.put("order", new String[] {_order.name()});

        final String path =
            Globals.API_URL
            + new Link(InternalServices.actions.getLink("list"))
                .build(params, InternalServices.encoder);
        return path;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(
                     final Callback<PagedCollection<ActionSummary>> callback) {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new CallbackResponseHandler<PagedCollection<ActionSummary>>(
                    USER_ACTIONS.viewActions(),
                    callback,
                    new Parser<PagedCollection<ActionSummary>>() {
                        @Override
                        public PagedCollection<ActionSummary> parse(
                                                    final Response response) {
                            return readActionSummaryCollection(response);
                        }}));
    }

}
