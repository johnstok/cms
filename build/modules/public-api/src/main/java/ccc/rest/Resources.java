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
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.serialization.Json;
import ccc.types.Duration;


/**
 * Basic API for manipulating resources.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Resources {

    /** NAME : String. */
    String NAME = "PublicCommands";

    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @throws RestException If the method fails
     * @return A summary of the resource.
     */
    @GET
    @Path("/resources/{id}")
    @NoCache
    ResourceSummary resource(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return The absolute path as a string.
     */
    @GET
    @Path("/resources/{id}/path")
    @NoCache
    String getAbsolutePath(@PathParam("id") UUID resourceId)
    throws RestException;

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
     * @throws RestException If the method fails
     * @return The list of resources.
     */
    @GET
    @Path("/resources/{id}/revisions")
    @NoCache
    Collection<RevisionDto> history(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return The metadata in a hashmap.
     */
    @GET
    @Path("/resources/{id}/metadata")
    @NoCache
    Map<String, String> metadata(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * List the roles for a resource.
     *
     * @param resourceId The resource's id.
     * @throws RestException If the method fails
     * @return The roles, as a collection of strings.
     */
    @GET
    @Path("/resources/{id}/roles")
    @NoCache
    Collection<String> roles(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return Duration.
     */
    @GET
    @Path("/resources/{id}/duration")
    @NoCache
    Duration cacheDuration(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @throws RestException If the method fails
     * @return TemplateSummary.
     */
    @GET
    @Path("/resources/{id}/template")
    @NoCache
    TemplateSummary computeTemplate(@PathParam("id") UUID resourceId)
    throws RestException;

    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @throws RestException If the method fails
     * @return A summary of the corresponding resource.
     */
    @GET
    @Path("/resources-by-path{path:.*}")
    @NoCache
    ResourceSummary resourceForPath(@PathParam("path") String path)
    throws RestException;

    /**
     * Look up the resource for a specified legacy id.
     *
     * @param legacyId The legacy id of the resource.
     * @throws RestException If the method fails
     * @return A summary of the corresponding resource.
     */
    @GET
    @Path("/resources-by-legacy-id/{id}")
    @NoCache
    ResourceSummary resourceForLegacyId(@PathParam("id") String legacyId)
    throws RestException;

    @POST @Path("/resources/{id}/duration")
    void updateCacheDuration(
        @PathParam("id") UUID resourceId,
        ResourceDto duration) throws RestException;

    @POST @Path("/resources/{id}/lock")
    void lock(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/wc-apply")
    void applyWorkingCopy(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/template")
    void updateResourceTemplate(
        @PathParam("id") UUID resourceId,
        ResourceDto template) throws RestException;

    @POST @Path("/resources/{id}/unlock")
    void unlock(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/unpublish")
    void unpublish(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/publish")
    void publish(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/parent")
    void move(
        @PathParam("id") UUID resourceId,
        UUID newParentId) throws RestException;

    @POST @Path("/resources/{id}/name")
    void rename(
        @PathParam("id") final UUID resourceId,
        final String name) throws RestException;

    @POST @Path("/resources/{id}/roles")
    void changeRoles(
        @PathParam("id") UUID resourceId,
        Collection<String> roles) throws RestException;

    @POST @Path("/resources/{id}/exclude-mm")
    void excludeFromMainMenu(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/include-mm")
    void includeInMainMenu(
        @PathParam("id") UUID resourceId) throws RestException;

    @POST @Path("/resources/{id}/metadata")
    void updateMetadata(
        @PathParam("id") UUID resourceId,
        Json json) throws RestException;

    @POST @Path("/resources/{id}/wc-clear")
    void clearWorkingCopy(
        @PathParam("id") UUID pageId) throws RestException;

    @POST @Path("/resources/{id}/wc-create")
    void createWorkingCopy(
        @PathParam("id") UUID resourceId,
        ResourceDto pu) throws RestException;

    @DELETE  @Path("/resources/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") UUID id) throws RestException;

    @GET @Path("/fail")
    void fail() throws RestException;
}
