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
package ccc.rest;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ccc.rest.dto.CommentDto;


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
     * @throws RestException If the method fails.
     *
     * @return Return the newly created comment.
     */
    @POST @Path("/comments")
    CommentDto create(CommentDto comment) throws RestException;


    /**
     * Retrieve the comment using its ID.
     *
     * @param commentId The comment's ID.
     *
     * @throws RestException If the method fails.
     *
     * @return Return the corresponding comment.
     */
    @GET @Path("/comment/{id}")
    CommentDto retrieve(@PathParam("id") UUID commentId) throws RestException;


    /**
     * Update the specified comment.
     *
     * @param commentId The comment's ID.
     * @param comment The updated comment.
     *
     * @throws RestException If the method fails.
     */
    @PUT @Path("/comment/{id}")
    void update(@PathParam("id") UUID commentId,
                CommentDto comment) throws RestException;


    /**
     * Delete an existing comment.
     *
     * @param commentId The comment's ID.
     *
     * @throws RestException If the method fails.
     */
    @DELETE @Path("/comment/{id}")
    void delete(@PathParam("id") UUID commentId) throws RestException;
}
