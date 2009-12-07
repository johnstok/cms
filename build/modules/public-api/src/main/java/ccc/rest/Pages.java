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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageDto;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;


/**
 * Basic API for manipulating pages.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Pages {

    /** NAME : String. */
    String NAME = "PublicPageCommands";

    /**
     * Validate a set of paragraphs against a given definition.
     *
     * @param json The paragraphs to validate.
     *
     * @return A list of errors, as strings.
     */
    @POST
    @Path("/page-validator")
    String validateFields(Json json);


    /**
     * Retrieve the delta for a page.
     *
     * @param pageId The page's id.
     * @throws RestException If the method fails
     * @return The corresponding delta.
     */
    @GET
    @Path("/pages/{id}/delta")
    @NoCache
    PageDelta pageDelta(@PathParam("id") UUID pageId) throws RestException;


    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     *
     * @throws RestException If the method fails.
     */
    @POST
    @Path("/pages/{id}")
    void updatePage(@PathParam("id") UUID pageId, Json delta)
    throws RestException;


    /**
     * Update the working copy of the specified page.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     *
     * @throws RestException If the method fails.
     */
    @POST
    @Path("/pages/{id}/wc")
    void updateWorkingCopy(@PathParam("id") UUID pageId, PageDelta delta)
    throws RestException;


    /**
     * Creates a new page.
     *
     * @param page Details of the new page to create.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new page.
     */
    @POST
    @Path("/pages")
    ResourceSummary createPage(PageDto page) throws RestException;

}
