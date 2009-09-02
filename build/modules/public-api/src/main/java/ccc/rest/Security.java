/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Security {

    @POST @Path("/sessions")
    public Boolean login(
          @QueryParam("u") final String username,
          @QueryParam("p") final String password);

    @GET @Path("/sessions/current")
    public Boolean isLoggedIn();

    @POST @Path("/sessions/current")
    public void logout();

    @GET @Path("/sessions/allproperties")
    public String readAllProperties();
}
