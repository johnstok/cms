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
package ccc.rest;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.UserDto;
import ccc.types.Username;


/**
 * User Commands API, used to update user data in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Users {

    /** NAME : String. */
    String NAME = "PublicUserCommands";


    /**
     * Retrieve the delta for a user.
     *
     * @param userId The user's id.
     * @throws RestException If the method fails
     * @return The corresponding delta.
     */
    @GET @Path("/{id}/delta")
    UserDto userDelta(@PathParam("id") UUID userId) throws RestException;

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    @GET @Path("/me")
    UserDto loggedInUser();

    /**
     * Query  users.
     *
     * @param username The username criteria.
     * @param email The email criteria.
     * @param groups The groups criteria.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @return Returns list of users.
     */
    @GET
   DtoCollection<UserDto> listUsers(
        @QueryParam("username") String username,
        @QueryParam("email") String email,
        @QueryParam("groups") String groups,
        @QueryParam("page") @DefaultValue("1") int pageNo,
        @QueryParam("count") @DefaultValue("20") int pageSize);


    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    @GET @Path("/{uname}/exists")
    Boolean usernameExists(@PathParam("uname") Username username);


    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     *
     * @throws RestException If the method fails.
     *
     * @return A user summary describing the new user.
     */
    @POST
    UserDto createUser(UserDto delta)
    throws RestException;


    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}")
    void updateUser(@PathParam("id") UUID userId, UserDto delta)
    throws RestException;


    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/password")
    void updateUserPassword(@PathParam("id") UUID userId, UserDto user)
    throws RestException;

    /**
     * Update the email and/or password for the current user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     *
     * @throws RestException If the method fails.
     */
    @POST @Path("/{id}/currentuser")
    void updateYourUser(@PathParam("id") UUID userId, UserDto user)
    throws RestException;


    /**
     * Look up the user for a specified legacy id.
     *
     * @param legacyId The legacy id of the user.
     * @throws RestException If the method fails
     * @return A summary of the corresponding user.
     */
    @GET @Path("/by-legacy-id/{id}")
    UserDto userByLegacyId(@PathParam("id") String legacyId)
    throws RestException;

    /**
     * Query metadata values with given key.
     *
     * @param key The key as a string.
     * @return Returns list of users.
     */
    @GET @Path("/metadata/{key}")
    Collection<String> listUserMetadataValuesWithKey(
        @PathParam("key") String key);
}
