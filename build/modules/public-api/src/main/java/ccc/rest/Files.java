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
package ccc.rest;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.TextFileDto;


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
     * @param folderId The id of the folder.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @return The list of images.
     * @throws RestException If an error occurs updating the file.
     */
    @GET
    @Path("/images/{id}")
    @NoCache
    DtoCollection<FileDto> getPagedImages(
        @PathParam("id") UUID folderId,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize)
        throws RestException;


    /**
     * Update an existing CCC file.
     *
     * @param id The ID of the file to update.
     * @param file The new file representation.
     *
     * @throws RestException If an error occurs updating the file.
     */
    @POST @Path("/{id}")
    void update(@PathParam("id") UUID id, TextFileDelta file)
        throws RestException;


    /**
     * Get the representation of an existing CCC file.
     *
     * @param fileId The ID of the file to get.
     *
     * @throws RestException If an error occurs getting the file.
     *
     * @return The file for the specified ID.
     */
    @GET @Path("/{id}")
    TextFileDelta get(@PathParam("id") UUID fileId) throws RestException;

    /**
     * Create a new text file in CCC.
     *
     * @param textFile The textFile details.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new text file.
     */
    @POST
    @Path("")
    ResourceSummary createTextFile(TextFileDto textFile)
    throws RestException;

}
