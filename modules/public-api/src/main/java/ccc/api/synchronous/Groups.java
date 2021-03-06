/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.synchronous;

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

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;




/**
 * Groups API, used to group data in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Groups {

    /** NAME : String. */
    String NAME = "groups";


    /**
     * List available groups.
     *
     * @param name Filter based on group name. NULL disables filter.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     *
     * @return Returns paged list of groups.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.Group.COLLECTION)
    PagedCollection<Group> query(
        @QueryParam("name") String name,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Find the group for a given ID.
     *
     * @param id The group ID.
     *
     * @return Returns the corresponding group.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.Group.ELEMENT)
    Group retrieve(@PathParam("id") UUID id);


    /**
     * Create a new group in the system.
     *
     * @param group The new group details.
     *
     * @return A DTO describing the new group.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.Group.COLLECTION)
    Group create(Group group);


    /**
     * Update an existing group in the system.
     *
     * @param group The updated group details.
     * @param id The ID of the group to update.
     *
     * @return A DTO describing the updated group.
     */
    @PUT @Path(ccc.api.synchronous.ResourceIdentifiers.Group.ELEMENT)
    Group update(@PathParam("id") UUID id, Group group);
}
