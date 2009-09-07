/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.rest;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.types.Duration;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface QueriesBasic {

    /** NAME : String. */
    String NAME = "PublicQueries";

    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @return A summary of the resource.
     */
    @GET
    @Path("/resources/{id}")
    @NoCache
    ResourceSummary resource(@PathParam("id") UUID resourceId);

    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     * @return The absolute path as a string.
     */
    @GET
    @Path("/resources/{id}/path")
    @NoCache
    String getAbsolutePath(@PathParam("id") UUID resourceId);

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    @GET
    @Path("/resources/locked/me")
    @NoCache
    Collection<ResourceSummary> lockedByCurrentUser();

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    @GET
    @Path("/resources/locked")
    @NoCache
    Collection<ResourceSummary> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @return The list of resources.
     */
    @GET
    @Path("/resources/{id}/revisions")
    @NoCache
    Collection<RevisionDto> history(@PathParam("id") UUID resourceId);

    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @return The metadata in a hashmap.
     */
    @GET
    @Path("/resources/{id}/metadata")
    @NoCache
    Map<String, String> metadata(@PathParam("id") UUID resourceId);

    /**
     * List the roles for a resource.
     *
     * @param resourceId The resource's id.
     *
     * @return The roles, as a collection of strings.
     */
    @GET
    @Path("/resources/{id}/roles")
    @NoCache
    Collection<String> roles(@PathParam("id") UUID resourceId);

    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @return Duration.
     */
    @GET
    @Path("/resources/{id}/duration")
    @NoCache
    Duration cacheDuration(@PathParam("id") UUID resourceId);

    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @return TemplateSummary.
     */
    @GET
    @Path("/resources/{id}/template")
    @NoCache
    TemplateSummary computeTemplate(@PathParam("id") UUID resourceId);

    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @return A summary of the corresponding resource.
     */
    @GET
    @Path("/resources-by-path/{path:.*}")
    @NoCache
    ResourceSummary resourceForPath(@PathParam("path") String path);

    /**
     * Look up the resource for a specified legacy id.
     *
     * @param legacyId The legacy id of the resource.
     * @return A summary of the corresponding resource.
     */
    @GET
    @Path("/resources-by-legacy-id/{id}")
    @NoCache
    ResourceSummary resourceForLegacyId(@PathParam("id") String legacyId);

}