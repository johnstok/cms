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
 * Security API for CCC.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Security {

    /**
     * Log in to CCC.
     *
     * @param username The user's username.
     * @param password The user's password.
     *
     * @return True if the login fails, false otherwise.
     */
    @POST @Path("/sessions")
    Boolean login(
          @QueryParam("u") final String username,
          @QueryParam("p") final String password);


    /**
     * Determine if a user is associated with the current session.
     *
     * @return True if a user is associated, false otherwise.
     */
    @GET @Path("/sessions/current")
    Boolean isLoggedIn();


    /**
     * Log out from the current session.
     *
     */
    @POST @Path("/sessions/current")
    void logout();


    /**
     * Read the properties for the session.
     *
     * @return The properties as a string.
     */
    @GET @Path("/sessions/allproperties")
    String readAllProperties();
}
