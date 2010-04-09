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

package ccc.api;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.api.dto.DtoCollection;
import ccc.api.dto.FolderDelta;
import ccc.api.dto.FolderDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.exceptions.RestException;
import ccc.api.types.SortOrder;


/**
 * Basic API for manipulating folders.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Folders {

    /** NAME : String. */
    String NAME = "PublicFolderCommands";

    /**
     * List all of the folders that are children of the specified parent folder.
     *
     * @param folderId The id of the folder.
     * @throws RestException If the method fails
     * @return The list of child folders.
     */
    @GET @Path("/{id}/folder-children")
    Collection<ResourceSummary> getFolderChildren(
        @PathParam("id") UUID folderId) throws RestException;

    /**
     * List paged set of the children of the specified folder.
     *
     * @param folderId The id of the folder.
     * @param sort The column to sort.
     * @param sortOrder The sort order (ASC/DESC).
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @return The list of child resource for paging.
     * @throws RestException If the method fails
     */
    @GET @Path("/{id}/children-paged")
    DtoCollection<ResourceSummary> getChildrenPaged(
        @PathParam("id") UUID folderId,
        @QueryParam("sort") String sort,
        @QueryParam("order") @DefaultValue("ASC") SortOrder sortOrder,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize)
         throws RestException;


    /**
     * List all of the children of the specified folder.
     *
     * @param folderId The folder.
     * @throws RestException If the method fails
     * @return The folder's of children.
     */
    @GET @Path("/{id}/children")
    Collection<ResourceSummary> getChildren(
        @PathParam("id") UUID folderId) throws RestException;

    /**
     * List all of the children of the specified folder which are  accessible
     * to current user.
     *
     * @param folderId The folder.
     * @throws RestException If the method fails
     * @return The folder's of children.
     */
    @GET @Path("/{id}/accessible-children")
    Collection<ResourceSummary> getAccessibleChildren(
        @PathParam("id") UUID folderId) throws RestException;

    /**
     * List all of the children of the specified folder in manual order.
     *
     * @param folderId The folder.
     * @throws RestException If the method fails
     * @return The folder's of children.
     */
    @GET @Path("/{id}/children-manual-order")
    Collection<ResourceSummary> getChildrenManualOrder(
        @PathParam("id") UUID folderId)
    throws RestException;

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folderId The id of the folder to check.
     * @param name The name of the resource.
     * @throws RestException If the method fails
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    @GET @Path("/{id}/{name}/exists")
    Boolean nameExistsInFolder(@PathParam("id") final UUID folderId,
                               @PathParam("name") final String name)
    throws RestException;

    /**
     * List the root folders available.
     *
     * @return A collection of resource summaries - one for each root folder.
     */
    @GET @Path("/roots")
    Collection<ResourceSummary> roots();

    /**
     * Create a folder with the specified name.
     *
     * @param folder Details of the new folder.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    @POST
    ResourceSummary createFolder(FolderDto folder) throws RestException;

    /**
     * Update the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param delta The updated details of the folder.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}")
    void updateFolder(@PathParam("id") UUID folderId, FolderDelta delta)
        throws RestException;


    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     *
     * @return A resource summary describing the new folder.
     */
    @POST @Path("/deprecated")
    @Deprecated
    ResourceSummary createFolder(UUID parentId,
                                 String name,
                                 String title,
                                 boolean publish);


    /**
     * Create a root folder with the specified name.
     *
     * @param name The name of the root folder.
     *
     * @return A resource summary describing the new root.
     */
    @POST @Path("/roots/{name}")
    // FIXME Post a 'folder' DTO.
    ResourceSummary createRoot(@PathParam("name") String name);
}
