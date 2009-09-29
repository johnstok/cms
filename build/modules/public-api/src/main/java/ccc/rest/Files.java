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
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.FileDto;
import ccc.rest.dto.TextFileDelta;


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
     * List all content images.
     *
     * @return The list of images.
     */
    @GET
    @Path("/images")
    @NoCache
    Collection<FileDto> getAllContentImages();


    /**
     * Update an existing CCC file.
     *
     * @param id The ID of the file to update.
     * @param file The new file representation.
     *
     * @throws RestException If an error occurs updating the file.
     */
    @POST @Path("/{id}")
    void update(@PathParam("id") UUID id, TextFileDelta file) throws RestException;


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
}
