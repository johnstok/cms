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
package ccc.api.core;

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

import ccc.api.types.ACL;
import ccc.api.types.Duration;
import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;


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
     * List existing resources. Leave field to null if not applicable.
     *
     * @param parent Filter resources by parent.
     * @param tag Filter resources by tag.
     * @param before Include only resources created before this date.
     * @param after Include only resources created after this date.
     * @param mainMenu Filter resources by 'included in main menu'.
     * @param type Filter resources by type.
     * @param locked Filter resources by locked (true or null).
     * @param published Filter resources by published (true or null).
     * @param sort The field results be sorted on.
     * @param order The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of resources.
     */
    @GET
    @Path(ccc.api.core.ResourceIdentifiers.Resource.LIST)
    PagedCollection<ResourceSummary> list(
        @QueryParam("parent") UUID parent,
        @QueryParam("tag") String tag,
        @QueryParam("before") Long before,
        @QueryParam("after") Long after,
        @QueryParam("mainmenu") String mainMenu,
        @QueryParam("type") String type,
        @QueryParam("locked") String locked,
        @QueryParam("published") String published,
        @QueryParam("sort") String sort,
        @QueryParam("order") @DefaultValue("ASC") SortOrder order,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     *
     * @return A summary of the resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.ELEMENT)
    ResourceSummary resource(@PathParam("id") UUID resourceId);


    /**
     * Delete the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     */
    @POST // Should be DELETE but hard to support from the browser.
    @Path(ccc.api.core.ResourceIdentifiers.Resource.DELETE)
    void deleteResource(@PathParam("id") UUID resourceId);


    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     *
     * @return The absolute path as a string.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.PATH)
    String getAbsolutePath(@PathParam("id") UUID resourceId);


    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     *
     * @return The list of resources.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.REVISIONS)
    PagedCollection<Revision> history(@PathParam("id") UUID resourceId);


    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     *
     * @return The metadata in a hashmap.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.METADATA)
    Map<String, String> metadata(@PathParam("id") UUID resourceId);


    /**
     * Get the access control list for a resource.
     *
     * @param resourceId The resource's id.
     *
     * @return The access control list for the specified resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.ACL)
    ACL acl(@PathParam("id") UUID resourceId);


    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     *
     * @return Duration.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.DURATION)
    Duration cacheDuration(@PathParam("id") UUID resourceId);


    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     *
     * @return TemplateSummary.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.TEMPLATE)
    Template computeTemplate(@PathParam("id") UUID resourceId);


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.SEARCH_PATH)
    ResourceSummary resourceForPath(@PathParam("path") String path);


    /**
     * Look up the resource for a specified legacy id.
     *
     * @param legacyId The legacy id of the resource.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.SEARCH_LEGACY)
    ResourceSummary resourceForLegacyId(@PathParam("id") String legacyId);

    /**
     * Look up the resource for a specified metadata key.
     *
     * @param key The legacy id of the resource.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.SEARCH_METADATA)
    PagedCollection<ResourceSummary> resourceForMetadataKey(
        @PathParam("id") String key);


    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration DTO specifying the cache duration.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.DURATION)
    void updateCacheDuration(
        @PathParam("id") UUID resourceId,
        Resource duration);


    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.LOCK)
    void lock(@PathParam("id") UUID resourceId);


    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.WC_APPLY)
    void applyWorkingCopy(@PathParam("id") UUID resourceId);


    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param template DTO specifying the new template to set for the resource.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.TEMPLATE)
    void updateResourceTemplate(
        @PathParam("id") UUID resourceId,
        Resource template);


    /**
     * Unlock the specified Resource.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.UNLOCK)
    void unlock(@PathParam("id") UUID resourceId);


    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.UNPUBLISH)
    void unpublish(@PathParam("id") UUID resourceId);


    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.PUBLISH)
    void publish(@PathParam("id") UUID resourceId);


    /**
     * Changes a resource's parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the folder to which the resource should be
     *  moved.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.PARENT)
    void move(@PathParam("id") UUID resourceId, UUID newParentId);


    /**
     * Rename resource.
     *
     * @param resourceId The id of the resource to rename.
     * @param name The new name.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.NAME)
    void rename(@PathParam("id") final UUID resourceId, final String name);


    /**
     * Change the access control list for a resource.
     *
     * @param resourceId The resource to update.
     * @param acl The access control list for the specified resource.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.ACL)
    void changeAcl(@PathParam("id") UUID resourceId, ACL acl);


    /**
     * Specify whether a resource should not be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.EXCLUDE_MM)
    void excludeFromMainMenu(@PathParam("id") UUID resourceId);


    /**
     * Specify that a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.INCLUDE_MM)
    void includeInMainMenu(@PathParam("id") UUID resourceId);


    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param resource The resource containing new metadata.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.METADATA)
    void updateMetadata(@PathParam("id") UUID resourceId,
                        Resource resource);


    /**
     * Delete the working copy for a page.
     *
     * @param pageId The id of the page with a working copy.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.WC_CLEAR)
    void clearWorkingCopy(@PathParam("id") UUID pageId);


    /**
     * Create a working copy for the specified resource, using the specified
     * revision.
     *
     * @param resourceId The id of the resource.
     * @param dto The DTO specifying the number of the revision to use.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.WC_CREATE)
    void createWorkingCopy(@PathParam("id") UUID resourceId,
                           Resource dto);


    /**
     * Clear the cache duration for the specified resource.
     *
     * @param id The id of the resource to update.
     */
    @DELETE @Path(ccc.api.core.ResourceIdentifiers.Resource.DURATION)
    void deleteCacheDuration(@PathParam("id") UUID id);


    /**
     * Create a new log entry for the resource.
     *
     * @param resourceId The id of the resource to create log entry for.
     * @param action The action for the log entry.
     * @param detail The details for the log entry.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.LOG_ENTRY)
    void createLogEntry(
        @PathParam("id") UUID resourceId, String action, String detail);


    /**
     * Look up the contents of a file as a String.
     * FIXME: Move to files.
     *
     * @param absolutePath The absolute path to the resource.
     * @param charset The character set for the file.
     *
     * @return The contents as a string.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.TEXT)
    String fileContentsFromPath(@PathParam("path") String absolutePath,
                                @DefaultValue("UTF-8") String charset);


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.PATH_SECURE)
    @Deprecated() // FIXME Use resourceForPath() or lookup by ID. - used in ContentServlet.getSnapshot
    Resource resourceForPathSecure(@PathParam("path") String path);


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @param version The version number of the resource to retrieve.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.PATH_SECURE)
        // FIXME: Path is duplicate of resourceForPathSecure()
    @Deprecated // FIXME: Lookup by ID. - used in ContentServlet.getSnapshot
    Resource revisionForPath(
                 @PathParam("path") final String path,
                 @QueryParam("version") @DefaultValue("0") final int version);


    /**
     * Look up the working copy for a specified path.
     *
     * @param path The absolute path.
     *
     * @return A summary of the corresponding resource.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Resource.PATH_WC)
    @Deprecated // FIXME: Lookup by ID. - used in ContentServlet.getSnapshot
    Resource workingCopyForPath(@PathParam("path") final String path);


    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     *
     * @return A summary of the newly created search.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Resource.SEARCH)
    // FIXME Should post a 'search' object.
    // FIXME Should be part of the 'search' API.
    ResourceSummary createSearch(@PathParam("id")    UUID parentId,
                                 @PathParam("title") String title);
}
