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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;



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
     * @param page The page to test.
     *
     * @return A list of errors, as strings.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Page.VALIDATOR)
    String validate(Page page);


    /**
     * Retrieve the page.
     *
     * @param pageId The page's id.
     *
     * @return The corresponding page.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Page.ELEMENT)
    Page retrieve(@PathParam("id") UUID pageId);


    /**
     * Retrieve the working copy for a page.
     *
     * @param pageId The page's id.
     *
     * @return The corresponding working copy.
     */
    @GET @Path(ccc.api.core.ResourceIdentifiers.Page.WC)
    Page retrieveWorkingCopy(@PathParam("id") UUID pageId);


    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     */
    @PUT @Path(ccc.api.core.ResourceIdentifiers.Page.ELEMENT)
    void update(@PathParam("id") UUID pageId, Page delta);


    /**
     * Update the working copy of the specified page.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     */
    @PUT @Path(ccc.api.core.ResourceIdentifiers.Page.WC)
    void updateWorkingCopy(@PathParam("id") UUID pageId, Page delta);


    /**
     * Creates a new page.
     *
     * @param page Details of the new page to create.
     *
     * @return A resource summary describing the new page.
     */
    @POST @Path(ccc.api.core.ResourceIdentifiers.Page.COLLECTION)
    ResourceSummary create(Page page);



    /**
     * List existing pages.
     *
     * @param criteria The criteria by which to filter pages.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of pages.
     */
    @POST
    @Path(ccc.api.core.ResourceIdentifiers.Page.SEARCH)
    PagedCollection<ResourceSummary> list(
        PageCriteria criteria,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);
}
