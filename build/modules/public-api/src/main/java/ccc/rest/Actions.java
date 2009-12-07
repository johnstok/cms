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
package ccc.rest;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;


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
    @POST @Path("/actions/{id}/cancel")
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
    @POST @Path("/actions")
    ActionSummary createAction(ActionDto action) throws RestException;


    /**
     * List all CCC actions that haven't yet been executed.
     *
     * @return A collection of action summaries, one per outstanding action.
     */
    @GET @Path("/actions/pending") @NoCache
    Collection<ActionSummary> listPendingActions();


    /**
     * List all CCC actions that have been executed.
     *
     * @return A collection of action summaries, one per completed action.
     */
    @GET @Path("/actions/completed") @NoCache
    Collection<ActionSummary> listCompletedActions();


    /**
     * Executes all available actions whose 'execute after' date is in the past.
     */
    @POST @Path("/actions/all")
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
    @GET @Path("/actions/{id}")
    ActionSummary findAction(
         @PathParam("id") UUID actionId) throws RestException;
}
