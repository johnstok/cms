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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

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
    @GET @Path("/users/{id}/delta") @NoCache
    UserDto userDelta(@PathParam("id") UUID userId) throws RestException;

    /**
     * Returns currently logged in user.
     *

     * @return UserDTO
     */
    @GET @Path("/users/me") @NoCache
    UserDto loggedInUser();

    /**
     * Query all users.
     *
     * @return Returns list of users.
     */
    @GET @Path("/users") @NoCache
    Collection<UserDto> listUsers();

    /**
     * Query users with specified role.
     *
     * @param role The role as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/role/{role}") @NoCache
    Collection<UserDto> listUsersWithRole(
        @PathParam("role") String role);

    /**
     * Query users with specified username.
     *
     * @param username The username.
     * @return Returns list of users.
     */
    @GET @Path("/users/username/{uname}") @NoCache
    Collection<UserDto> listUsersWithUsername(
        @PathParam("uname") Username username);

    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    @GET @Path("/users/{uname}/exists") @NoCache
    Boolean usernameExists(@PathParam("uname") Username username);

    /**
     * Query users with specified email.
     *
     * @param email The email as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/email/{email}") @NoCache
    Collection<UserDto> listUsersWithEmail(
        @PathParam("email") String email);


    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     *
     * @throws RestException If the method fails.
     *
     * @return A user summary describing the new user.
     */
    @POST @Path("/users")
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
    @POST @Path("/users/{id}")
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
    @POST @Path("/users/{id}/password")
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
    @POST @Path("/users/{id}/currentuser")
    void updateYourUser(@PathParam("id") UUID userId, UserDto user)
    throws RestException;


    /**
     * Look up the user for a specified legacy id.
     *
     * @param legacyId The legacy id of the user.
     * @throws RestException If the method fails
     * @return A summary of the corresponding user.
     */
    @GET
    @Path("/user-by-legacy-id/{id}")
    @NoCache
    UserDto userByLegacyId(@PathParam("id") String legacyId)
    throws RestException;

    /**
     * Query metadata values with given key.
     *
     * @param key The key as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/metadata/{key}") @NoCache
    Collection<String> listUserMetadataValuesWithKey(
        @PathParam("key") String key);
}
