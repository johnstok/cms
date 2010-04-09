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

import java.io.InputStream;
import java.util.Date;
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
import ccc.api.dto.FileDelta;
import ccc.api.dto.FileDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TextFileDelta;
import ccc.api.dto.TextFileDto;


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
    DtoCollection<FileDto> getPagedImages(
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
    void update(@PathParam("id") UUID id, TextFileDelta file);


    /**
     * Get the representation of an existing CCC file.
     *
     * @param fileId The ID of the file to get.
     *
     * @return The file for the specified ID.
     */
    @GET @Path("/{id}")
    TextFileDelta get(@PathParam("id") UUID fileId);


    /**
     * Create a new text file in CCC.
     *
     * @param textFile The textFile details.
     *
     * @return A resource summary describing the new text file.
     */
    @POST
    ResourceSummary createTextFile(TextFileDto textFile);


    /**
     * Create a new CCC file.
     *
     * @param parentFolder The folder in which the file should be created.
     * @param file The details of the file.
     * @param resourceName The name of the file.
     * @param dataStream The content of the file.
     * @param title The title of the file.
     * @param description The description of the file.
     * @param lastUpdated The last updated date of the file.
     * @param publish Should the file be published.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     *
     * @return A summary of the newly created file.
     */
    @POST @Path("/bin")
    ResourceSummary createFile(UUID parentFolder,
                               FileDelta file,
                               String resourceName,
                               InputStream dataStream,
                               String title,
                               String description,
                               Date lastUpdated,
                               boolean publish,
                               String comment,
                               boolean isMajorEdit);


    /**
     * Update an existing CCC file.
     *
     * @param fileId The id of the file to update.
     * @param fileDelta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     * @param dataStream The new content for the file.
     */
    @POST @Path("/bin/{id}")
    void updateFile(@PathParam("id") UUID fileId,
                    FileDelta fileDelta,
                    String comment,
                    boolean isMajorEdit,
                    InputStream dataStream);


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
