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

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceSummary;


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
    @GET
    @Path("/{id}/targetname")
    @NoCache
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
    @POST @Path("")
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
