/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.core;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.api.types.CommentStatus;
import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;


/**
 * API for manipulating Comments.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Comments {

    /** NAME : String. */
    String NAME = "Comments";


    /**
     * Create a new comment in CCC.
     *
     * @param comment The comment to create.
     *
     * @return Return the newly created comment.
     */
    @POST
    Comment create(Comment comment);


    /**
     * Retrieve the comment using its ID.
     *
     * @param commentId The comment's ID.
     *
     * @return Return the corresponding comment.
     */
    @GET @Path("/{id}")
    Comment retrieve(@PathParam("id") UUID commentId);


    /**
     * Update the specified comment.
     *
     * @param commentId The comment's ID.
     * @param comment The updated comment.
     */
    @POST @Path("/{id}")
    void update(@PathParam("id") UUID commentId, Comment comment);


    /**
     * Delete an existing comment.
     *
     * @param commentId The comment's ID.
     */
    @DELETE @Path("/{id}")
    void delete(@PathParam("id") UUID commentId);


    /**
     * List existing comments.
     *
     * @param resourceId Filter comments by resource. NULL will return all.
     * @param status Filter comments based on status. NULL will return all.
     * @param sort The field to sort on.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of comments.
     */
    @GET
    PagedCollection<Comment> list(
        @QueryParam("resource") UUID resourceId,
        @QueryParam("status") CommentStatus status,
        @QueryParam("sort") @DefaultValue("status") String sort,
        @QueryParam("order") @DefaultValue("ASC") SortOrder sortOrder,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);
}
