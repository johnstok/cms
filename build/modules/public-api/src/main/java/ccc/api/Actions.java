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
package ccc.api;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.api.dto.ActionDto;
import ccc.api.dto.ActionSummary;
import ccc.api.dto.DtoCollection;
import ccc.api.exceptions.RestException;
import ccc.types.SortOrder;


/**
 * API for managing actions.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Actions {

    /** NAME : String. */
    String NAME = "ActionDao";


    /**
     * Cancel a scheduled action.
     *
     * @param actionId The id of the action to cancel.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/cancel")
    void cancelAction(
        @PathParam("id") UUID actionId) throws RestException;

    /**
     * Create a new scheduled action.
     *
     * @param action The action to create.
     *
     * @throws RestException If the method fails.
     *
     * @return A summary of the new action.
     */
    @POST
    ActionSummary createAction(ActionDto action) throws RestException;


    /**
     * List all CCC actions that haven't yet been executed.
     *
     * @param sort The field to sort on.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     * @return A collection of action summaries, one per outstanding action.
     */
    @GET @Path("/pending")
    DtoCollection<ActionSummary> listPendingActions(
        @QueryParam("sort") @DefaultValue("status") String sort,
        @QueryParam("order") @DefaultValue("DESC") SortOrder sortOrder,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * List all CCC actions that have been executed.
     *
     * @param sort The field to sort on.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     * @return A collection of action summaries, one per completed action.
     */
    @GET @Path("/completed")
    DtoCollection<ActionSummary> listCompletedActions(
        @QueryParam("sort") @DefaultValue("status") String sort,
        @QueryParam("order") @DefaultValue("DESC") SortOrder sortOrder,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Executes all available actions whose 'execute after' date is in the past.
     */
    @POST @Path("/all")
    void executeAll();


    /**
     * Find the action with the specified ID.
     *
     * @param actionId The action's ID.
     *
     * @throws RestException If the method fails.
     *
     * @return A summary of the action.
     */
    @GET @Path("/{id}")
    ActionSummary findAction(
         @PathParam("id") UUID actionId) throws RestException;
}
