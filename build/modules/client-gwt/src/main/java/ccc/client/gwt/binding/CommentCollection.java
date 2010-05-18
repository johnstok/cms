/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.binding;

import ccc.api.core.Comment;
import ccc.api.core.PagedCollection;
import ccc.api.types.CommentStatus;
import ccc.api.types.SortOrder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CommentCollection
    extends
        PagedCollection<Comment> {


    /**
     * Constructor.
     */
    public CommentCollection() {
        super(Comment.class);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param page
     * @param count
     * @param status
     * @param order
     * @param sort
     * @return
     */
    public String list(final int page,
                       final int count,
                       final CommentStatus status,
                       final SortOrder order,
                       final String sort) {
        return
            getLink("self")
                + "?page="+page
                + "&count="+count
                + ((null==status) ? "" : "&status="+status.name())
                + ((null==order) ? "" : "&order="+order.name())
                + ((null==sort) ? "" : "&sort="+sort);
    }
}
