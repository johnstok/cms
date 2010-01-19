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
package ccc.contentcreator.actions.remote;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.actions.RemotingAction;
import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.CommentDto;
import ccc.serialization.JsonKeys;
import ccc.types.CommentStatus;
import ccc.types.DBC;
import ccc.types.SortOrder;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
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
     * @param order The sort order.
     * @param sort The column to sort on.
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
        final String path =
            "/comments"
            + "?page="+_page
            + "&count="+_count
            + ((null==_status) ? "" : "&status="+_status.name())
            + ((null==_order) ? "" : "&order="+_order.name())
            + ((null==_sort) ? "" : "&sort="+_sort);
        return path;
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

        final int totalCount =
            (int) obj.get(JsonKeys.SIZE).isNumber().doubleValue();

        final JSONArray result =obj.get(JsonKeys.ELEMENTS).isArray();
        final Collection<CommentDto> comments = new ArrayList<CommentDto>();
        for (int i=0; i<result.size(); i++) {
            comments.add(new CommentDto(new GwtJson(result.get(i).isObject())));
        }

        execute(comments, totalCount);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param comments The page of comments returned.
     * @param totalCount The total comments available on the server.
     */
    protected abstract void execute(Collection<CommentDto> comments,
                                    int totalCount);
}
