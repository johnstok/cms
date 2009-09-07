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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;


/**
 * API for manipulating files.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Files {


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
     * Retrieve the delta for a file.
     *
     * @param fileId The file's id.
     * @return The corresponding delta.
     */
    @GET
    @Path("/{id}/delta")
    @NoCache
    FileDelta fileDelta(@PathParam("id") UUID fileId);
}
