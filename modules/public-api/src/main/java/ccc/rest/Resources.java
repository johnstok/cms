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
import java.util.Map;
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

import ccc.plugins.s11n.Json;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSnapshot;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.exceptions.RestException;
import ccc.rest.exceptions.UnauthorizedException;
import ccc.types.Duration;
import ccc.types.SortOrder;


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
     * List existing resources.
     *
     * @param tag Filter resources by tag. NULL will return all.
     * @param sort The field results be sorted on.
     * @param order The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of resources.
     *
     * @throws RestException If the method fails.
     */
    @GET
    Collection<ResourceSummary> list(
        @QueryParam("tag") String tag,
        @QueryParam("before") Long before,
        @QueryParam("after") Long after,
        @QueryParam("sort") @DefaultValue("name") String sort,
        @QueryParam("order") @DefaultValue("ASC") SortOrder order,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize)
    throws RestException;


    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @throws RestException If the method fails
     * @return A summary of the resource.
     * @throws UnauthorizedException If the user does not have right to access.
     */
    @GET @Path("/{id}")
    ResourceSummary resource(@PathParam("id") UUID resourceId)
    throws RestException, UnauthorizedException;


    /**
     * Delete the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @throws RestException If the method fails.
     */
    @POST // Should be DELETE but hard to support from the browser.
    @Path("/{id}/delete")
    void deleteResource(@PathParam("id") UUID resourceId) throws RestException;


    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return The absolute path as a string.
     */
    @GET @Path("/{id}/path")
    String getAbsolutePath(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    @GET @Path("/locked")
    Collection<ResourceSummary> locked();


    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @throws RestException If the method fails
     * @return The list of resources.
     */
    @GET @Path("/{id}/revisions")
    Collection<RevisionDto> history(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return The metadata in a hashmap.
     */
    @GET @Path("/{id}/metadata")
    Map<String, String> metadata(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * List the roles for a resource.
     *
     * @param resourceId The resource's id.
     * @throws RestException If the method fails
     * @return The access control list for the specified resource.
     */
    @GET @Path("/{id}/roles")
    AclDto roles(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @throws RestException If the method fails
     * @return Duration.
     */
    @GET @Path("/{id}/duration")
    Duration cacheDuration(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @throws RestException If the method fails
     * @return TemplateSummary.
     */
    @GET @Path("/{id}/template")
    TemplateSummary computeTemplate(@PathParam("id") UUID resourceId)
    throws RestException;


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @throws RestException If the method fails
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-path{path:.*}")
    ResourceSummary resourceForPath(@PathParam("path") String path)
    throws RestException;


    /**
     * Look up the resource for a specified legacy id.
     *
     * @param legacyId The legacy id of the resource.
     * @throws RestException If the method fails
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-legacy-id/{id}")
    ResourceSummary resourceForLegacyId(@PathParam("id") String legacyId)
    throws RestException;

    /**
     * Look up the resource for a specified metadata key.
     *
     * @param key The legacy id of the resource.
     * @throws RestException If the method fails
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-metadata-key/{id}")
    Collection<ResourceSummary> resourceForMetadataKey(
        @PathParam("id") String key)
    throws RestException;


    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration DTO specifying the cache duration.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/duration")
    void updateCacheDuration(
        @PathParam("id") UUID resourceId,
        ResourceDto duration) throws RestException;


    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/lock")
    void lock(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/wc-apply")
    void applyWorkingCopy(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param template DTO specifying the new template to set for the resource.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/template")
    void updateResourceTemplate(
        @PathParam("id") UUID resourceId,
        ResourceDto template) throws RestException;


    /**
     * Unlock the specified Resource.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/unlock")
    void unlock(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/unpublish")
    void unpublish(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/publish")
    void publish(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Changes a resource's parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the folder to which the resource should be
     *  moved.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/parent")
    void move(
        @PathParam("id") UUID resourceId,
        UUID newParentId) throws RestException;


    /**
     * Rename resource.
     *
     * @param resourceId The id of the resource to rename.
     * @param name The new name.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/name")
    void rename(
        @PathParam("id") final UUID resourceId,
        final String name) throws RestException;


    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param acl The access control list for the specified resource.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/roles")
    void changeRoles(
        @PathParam("id") UUID resourceId,
        AclDto acl) throws RestException;


    /**
     * Specify whether a resource should not be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/exclude-mm")
    void excludeFromMainMenu(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Specify that a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/include-mm")
    void includeInMainMenu(
        @PathParam("id") UUID resourceId) throws RestException;


    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param json JSON representation of the metadata.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/metadata")
    void updateMetadata(
        @PathParam("id") UUID resourceId,
        Json json) throws RestException;


    /**
     * Delete the working copy for a page.
     *
     * @param pageId The id of the page with a working copy.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/wc-clear")
    void clearWorkingCopy(
        @PathParam("id") UUID pageId) throws RestException;


    /**
     * Create a working copy for the specified resource, using the specified
     * revision.
     *
     * @param resourceId The id of the resource.
     * @param dto The DTO specifying the number of the revision to use.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/wc-create")
    void createWorkingCopy(
        @PathParam("id") UUID resourceId,
        ResourceDto dto) throws RestException;


    /**
     * Clear the cache duration for the specified resource.
     *
     * @param id The id of the resource to update.
     *
     * @throws RestException If the update fails.
     */
    @DELETE @Path("/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") UUID id) throws RestException;


    /**
     * Create a new log entry for the resource.
     *
     * @param resourceId The id of the resource to create log entry for.
     * @param action The action for the log entry.
     * @param detail The details for the log entry.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/logentry-create")
    void createLogEntry(
        @PathParam("id") UUID resourceId,
        String action,
        String detail) throws RestException;

    /**
     * Returns siblings of the resource, the resource included.
     *
     * @param resourceId The id of the resource to create log entry for.
     *
     * @return The list of siblings.
     */
    @GET @Path("/{id}/siblings")
    Collection<ResourceSummary> getSiblings(@PathParam("id") UUID resourceId);


    /**
     * Look up the contents of a file as a String.
     * FIXME: Move to files.
     *
     * @param absolutePath The absolute path to the resource.
     * @param charset The character set for the file.
     *
     * @return The contents as a string.
     */
    @GET @Path("/text-content{path:.*}")
    String fileContentsFromPath(@PathParam("path") String absolutePath,
                                @DefaultValue("UTF-8") String charset);


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-path-secure{path:.*}")
    @Deprecated() // FIXME Use resourceForPath() or lookup by ID.
    ResourceSnapshot resourceForPathSecure(@PathParam("path") String path);


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @param version The version number of the resource to retrieve.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-path-secure{path:.*}")
    @Deprecated // FIXME: Lookup by ID.
    ResourceSnapshot revisionForPath(
                 @PathParam("path") final String path,
                 @QueryParam("version") @DefaultValue("0") final int version);


    /**
     * Look up the working copy for a specified path.
     *
     * @param path The absolute path.
     *
     * @throws RestException If the method fails
     * @throws UnauthorizedException If the resource is not accessible to the
     *  current user.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/by-path-wc{path:.*}")
    @Deprecated // FIXME: Lookup by ID.
    ResourceSnapshot workingCopyForPath(@PathParam("path") final String path)
    throws RestException, UnauthorizedException;


    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     *
     * @return A summary of the newly created search.
     */
    @POST @Path("/search/{id}/{title}")
    // FIXME Should post a 'search' object.
    // FIXME Should be part of the 'search' API.
    ResourceSummary createSearch(@PathParam("id")    UUID parentId,
                                 @PathParam("title") String title);
}
