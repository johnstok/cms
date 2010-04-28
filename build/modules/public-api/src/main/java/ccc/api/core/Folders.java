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

package ccc.api.core;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;



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
     *
     * @return The list of child folders.
     */
    @GET @Path("/{id}/folder-children")
    Collection<ResourceSummary> getFolderChildren(
        @PathParam("id") UUID folderId);


    /**
     * List all of the children of the specified folder which are  accessible
     * to current user.
     *
     * @param folderId The folder.
     *
     * @return The folder's of children.
     */
    @GET @Path("/{id}/accessible-children")
    Collection<ResourceSummary> getAccessibleChildren(
        @PathParam("id") UUID folderId);

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folderId The id of the folder to check.
     * @param name The name of the resource.
     *
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    @GET @Path("/{id}/{name}/exists")
    Boolean nameExistsInFolder(@PathParam("id") final UUID folderId,
                               @PathParam("name") final String name);

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
     * @return A resource summary describing the new folder.
     */
    @POST
    ResourceSummary createFolder(Folder folder);

    /**
     * Update the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param delta The updated details of the folder.
     */
    @POST @Path("/{id}")
    void updateFolder(@PathParam("id") UUID folderId, Folder delta);


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
