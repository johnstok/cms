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

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.api.dto.FileDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.types.PagedCollection;


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
    @GET @Path("/images/{id}")
    PagedCollection<FileDto> getPagedImages(
        @PathParam("id") UUID folderId,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Update an existing CCC file.
     *
     * @param id The ID of the file to update.
     * @param file The new file representation.
     */
    @POST @Path("/{id}")
    void update(@PathParam("id") UUID id, FileDto file);


    /**
     * Get the representation of an existing CCC file.
     *
     * @param fileId The ID of the file to get.
     *
     * @return The file for the specified ID.
     */
    @GET @Path("/{id}")
    FileDto get(@PathParam("id") UUID fileId);


    /**
     * Create a new text file in CCC.
     *
     * @param textFile The textFile details.
     *
     * @return A resource summary describing the new text file.
     */
    @POST
    ResourceSummary createTextFile(FileDto textFile);


    /**
     * Create a new CCC file.
     *
     * @param file The file to create.
     *
     * @return A summary of the newly created file.
     */
    @POST @Path("/bin")
    @Consumes("multipart/form-data")
    @Produces({"application/json", "text/html"})
    ResourceSummary createFile(FileDto file);


    /**
     * Update an existing CCC file.
     *
     * @param fileId The id of the file to update.
     * @param file   The changes to apply.
     *
     * @return A summary of the updated file.
     */
    @POST @Path("/bin/{id}")
    @Consumes("multipart/form-data")
    @Produces({"application/json", "text/html"})
    ResourceSummary updateFile(@PathParam("id") UUID fileId, FileDto file);


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     */
    @GET @Path("/bin/{id}")
    void retrieve(@PathParam("id") UUID file,
                  StreamAction action);


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     */
    @GET @Path("/bin/{id}/wc")
    void retrieveWorkingCopy(@PathParam("id") UUID file,
                             StreamAction action);


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param revision The file revision to retrieve.
     * @param action The action to perform.
     */
    @GET @Path("/bin/{id}/wc")
    void retrieveRevision(@PathParam("id") UUID file,
                          int revision,
                          StreamAction action);
}
