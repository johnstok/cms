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

import ccc.api.core.Comment;
import ccc.api.core.PagedCollection;
import ccc.api.types.CommentStatus;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.api.types.SortOrder;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Retrieve a list of comments from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListComments
    extends
        RemotingAction<PagedCollection<Comment>> {

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
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("page",  new String[] {""+_page});
        params.put("count", new String[] {""+_count});
        params.put(
            "status", new String[] {(null==_status) ? null : _status.name()});
        params.put("sort",  new String[] {_sort});
        params.put("order", new String[] {_order.name()});

        return
            new Link(InternalServices.comments.getLink("self"))
            .build(params, InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final PagedCollection<Comment> comments) {
        execute(comments);
    }


    /** {@inheritDoc} */
    @Override
    protected PagedCollection<Comment> parse(final Response response) {
        return readComments(response);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param comments The page of comments returned.
     */
    protected abstract void execute(PagedCollection<Comment> comments);
}
