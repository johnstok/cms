/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
     * Execute the next available action.
     */
    @POST @Path("/actions/next")
    void executeAction();


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
