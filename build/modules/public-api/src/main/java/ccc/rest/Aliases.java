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


    /**
     * Retrieve the target name for a alias.
     *
     * @param aliasId The alias' id.
     * @return The corresponding target name.
     */
    @GET
    @Path("/{id}/targetname")
    @NoCache
    String aliasTargetName(@PathParam("id") UUID aliasId);


    @POST @Path("")
    ResourceSummary createAlias(AliasDto alias) throws CommandFailedException;


    @POST @Path("/{id}")
    void updateAlias(
        @PathParam("id") UUID aliasId,
        AliasDelta delta) throws CommandFailedException;
}
