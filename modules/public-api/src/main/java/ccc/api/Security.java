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
package ccc.api;

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
