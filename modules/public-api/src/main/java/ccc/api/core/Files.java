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

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.api.types.StreamAction;



/**
 * API for manipulating files.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Files {

    /** NAME : String. */
    String NAME = "PublicFiles";


    /**
     * List all images of the given folder id.
     *
     * @param folderId The id of the folder.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     *
     * @return The list of images.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.File.IMAGES)
    PagedCollection<File> getPagedImages(
        @PathParam("id") UUID folderId,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Update an existing CCC file.
     *
     * @param id The ID of the file to update.
     * @param file The new file representation.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.File.ELEMENT)
    void update(@PathParam("id") UUID id, File file);


    /**
     * Get the representation of an existing CCC file.
     *
     * @param fileId The ID of the file to get.
     *
     * @return The file for the specified ID.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.File.ELEMENT)
    File retrieve(@PathParam("id") UUID fileId);


    /**
     * Create a new text file in CCC.
     *
     * @param textFile The textFile details.
     *
     * @return A resource summary describing the new text file.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.File.COLLECTION)
    @Deprecated
    ResourceSummary createTextFile(File textFile);


    /**
     * Create a new CCC file.
     *
     * @param file The file to create.
     *
     * @return A summary of the newly created file.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.File.BINARY_COLLECTION)
    @Consumes("multipart/form-data")
    @Produces({"text/html", "application/json"})
    ResourceSummary create(File file);


    /**
     * Update an existing CCC file.
     *
     * @param fileId The id of the file to update.
     * @param file   The changes to apply.
     *
     * @return A summary of the updated file.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.File.BINARY_ELEMENT)
    @Consumes("multipart/form-data")
    @Produces({"text/html", "application/json"})
    ResourceSummary updateFile(@PathParam("id") UUID fileId, File file);


    /**
     * Process the contents of a file with an action.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.File.BINARY_ELEMENT)
    void retrieve(@PathParam("id") UUID file,
                  StreamAction action);


    /**
     * Process the contents of a file's working copy with an action.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.File.BINARY_WC)
    void retrieveWorkingCopy(@PathParam("id") UUID file,
                             StreamAction action);


    /**
     * Process the contents of a file revision with an action.
     *
     * @param file The file's ID.
     * @param revision The file revision to retrieve.
     * @param action The action to perform.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.File.BINARY_REVISION)
    void retrieveRevision(@PathParam("id") UUID file,
                          @PathParam("rev") int revision,
                          StreamAction action);
}
