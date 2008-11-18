/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ccc.domain.LogEntry;
import ccc.domain.Resource;


/**
 * {@link Resource} specific business methods.
 *
 * @author Civic Computing Ltd.
 */
@Path("/resources")
public interface ResourceDAOLocal {

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    @GET // TODO: Should be POST
    @Path("/lock/{id}")
    @Produces("text/plain")
    Resource lock(@PathParam("id") String resourceId);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    @GET // TODO: Should be POST
    @Path("/unlock/{id}")
    @Produces("text/plain")
    Resource unlock(@PathParam("id") String resourceId);

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    List<Resource> lockedByCurrentUser();

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    @GET
    @Path("/locked")
    @Produces("text/plain")
    List<Resource> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @return The list of resources.
     */
    @GET
    @Path("/history/{id}")
    @Produces("text/plain")
    List<LogEntry> history(@PathParam("id") String resourceId);

}
