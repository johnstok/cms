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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderNew;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ID;


/**
 * Basic API for manipulating folders.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface FoldersBasic {

    /**
     * List all of the folders that are children of the specified parent folder.
     *
     * @param folderId The id of the folder.
     * @return The list of child folders.
     */
    @GET
    @Path("/folders/{id}/folder-children")
    @NoCache
    Collection<ResourceSummary> getFolderChildren(@PathParam("id") ID folderId);

    /**
     * List all of the children of the specified folder.
     *
     * @param folderId The folder.
     * @return The folder's of children.
     */
    @GET
    @Path("/folders/{id}/children")
    @NoCache
    Collection<ResourceSummary> getChildren(@PathParam("id") ID folderId);

    /**
     * List all of the children of the specified folder in manual order.
     *
     * @param folderId The folder.
     * @return The folder's of children.
     */
    @GET
    @Path("/folders/{id}/children-manual-order")
    @NoCache
    Collection<ResourceSummary> getChildrenManualOrder(@PathParam("id") ID folderId);

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folderId The id of the folder to check.
     * @param name The name of the resource.
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    @GET
    @Path("/folders/{id}/{name}/exists")
    @NoCache
    boolean nameExistsInFolder(@PathParam("id") final ID folderId,
                               @PathParam("name") final String name);

    /**
     * List the root folders available.
     *
     * @return A collection of resource summaries - one for each root folder.
     */
    @GET
    @Path("/roots")
    @NoCache
    Collection<ResourceSummary> roots();

    /**
     * Create a folder with the specified name.
     *
     * @param folder Details of the new folder.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    @POST
    @Path("/folders")
    ResourceSummary createFolder(FolderNew folder) throws CommandFailedException;

    /**
     * Update the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param delta The updated details of the folder.
     *
     * @throws CommandFailedException If the method fails.
     */
    @POST
    @Path("/folders/{id}")
    void updateFolder(@PathParam("id") ID folderId, FolderDelta delta) throws CommandFailedException;

}