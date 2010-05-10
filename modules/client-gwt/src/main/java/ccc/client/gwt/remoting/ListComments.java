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

import ccc.api.types.CommentStatus;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.CommentCollection;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Retrieve a list of comments from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListComments
    extends
        RemotingAction {

    private CommentStatus _status;
    private int           _page;
    private int           _count;
    private SortOrder     _order;
    private String        _sort;


    /**
     * Constructor.
     *
     * @param status The status to filter.
     * @param page The page to display.
     * @param count The number of results per page.
     * @param sort The sort order.
     * @param order The column to sort on.
     */
    public ListComments(final CommentStatus status,
                        final int page,
                        final int count,
                        final String sort,
                        final SortOrder order) {
        super(USER_ACTIONS.viewComments());

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
        return GLOBALS.comments().list(_page, _count, _status, _order, _sort);
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

        final CommentCollection comments = new CommentCollection();
        comments.fromJson(new GwtJson(obj));

        execute(comments);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param comments The page of comments returned.
     */
    protected abstract void execute(CommentCollection comments);
}
