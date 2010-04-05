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

import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.exceptions.RestException;


/**
 * API for manipulating Aliases.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Aliases {

    /** NAME : String. */
    String NAME = "Aliases";


    /**
     * Retrieve the target name for a alias.
     *
     * @param aliasId The alias' id.
     *
     * @throws RestException If the method fails.
     *
     * @return The corresponding target name.
     */
    @GET @Path("/{id}/targetname")
    String aliasTargetName(@PathParam("id") UUID aliasId) throws RestException;


    /**
     * Create a new alias in CCC.
     *
     * @param alias The alias to create.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new alias.
     */
    @POST
    ResourceSummary createAlias(AliasDto alias) throws RestException;


    /**
     * Update an alias.
     *
     * @param aliasId The id of the alias to update.
     * @param delta The changes to apply.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}")
    void updateAlias(
        @PathParam("id") UUID aliasId,
        AliasDelta delta) throws RestException;
}
