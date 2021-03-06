/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
 * Revision      $Rev: 3405 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2011-01-26 16:46:29 +0000 (Wed, 26 Jan 2011) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.synchronous;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.FailureCode;
import ccc.api.types.SortOrder;


/**
 * Actions API, minus scheduling.
 *
 * @author Civic Computing Ltd.
 */
public interface Actions2 {

    /**
     * Cancel a scheduled action.
     *
     * @param actionId The id of the action to cancel.
     */
    @DELETE @Path(ccc.api.synchronous.ResourceIdentifiers.Action.ELEMENT)
    void cancel(@PathParam("id") UUID actionId);

    /**
     * Create a new scheduled action.
     *
     * @param action The action to create.
     *
     * @return A summary of the new action.
     */
    @POST
    Action create(Action action);


    /**
     * List of CCC actions.
     *
     * @param username The username to match.
     * @param commandType The command type to match.
     * @param failureCode The failure code to match.
     * @param status The action status code to match.
     * @param executeAfter The execute after date to match.
     * @param subject The subject to match.
     * @param sort The field to sort on.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     * @return A collection of action summaries, one per outstanding action.
     */
    @GET
    PagedCollection<ActionSummary> listActions(
        @QueryParam("username") String username,
        @QueryParam("commandType") CommandType commandType,
        @QueryParam("failureCode") FailureCode failureCode,
        @QueryParam("status") ActionStatus status,
        @QueryParam("executeAfter") Date executeAfter,
        @QueryParam("subject") UUID subject,
        @QueryParam("sort") @DefaultValue("status") String sort,
        @QueryParam("order") @DefaultValue("DESC") SortOrder sortOrder,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Executes all available actions whose 'execute after' date is in the past.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.Action.EXECUTE)
    void executeAll();


    /**
     * Find the action with the specified ID.
     *
     * @param actionId The action's ID.
     *
     * @return A summary of the action.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.Action.ELEMENT)
    Action retrieve(@PathParam("id") UUID actionId);
}
