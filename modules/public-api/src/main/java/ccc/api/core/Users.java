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
package ccc.api.core;

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

import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;
import ccc.api.types.Username;


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
     *
     * @return The corresponding delta.
     */
    @GET @Path("/{id}/delta")
    User userDelta(@PathParam("id") UUID userId);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    @GET @Path("/me")
    User loggedInUser();

    /**
     * Query  users.
     *
     * @param username The username criteria.
     * @param email The email criteria.
     * @param groups The groups criteria.
     * @param metadataKey The metadata key criteria.
     * @param metadataValue The metadata value criteria.
     * @param sort The column to sort.
     * @param order The sort order (ASC/DESC).
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     *
     * @return Returns list of users.
     */
   @GET
   PagedCollection<User> listUsers(
        @QueryParam("username") String username,
        @QueryParam("email") String email,
        @QueryParam("groups") String groups,
        @QueryParam("metadataKey") String metadataKey,
        @QueryParam("metadataValue") String metadataValue,
        @QueryParam("sort") String sort,
        @QueryParam("order") @DefaultValue("ASC") SortOrder order,
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
     * @return A user summary describing the new user.
     */
    @POST
    User createUser(User delta);


    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     */
    @POST @Path("/{id}")
    void updateUser(@PathParam("id") UUID userId, User delta);


    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     */
    @POST @Path("/{id}/password")
    void updateUserPassword(@PathParam("id") UUID userId, User user);

    /**
     * Update the email and/or password for the current user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     */
    @POST @Path("/{id}/currentuser")
    void updateYourUser(@PathParam("id") UUID userId, User user);


    /**
     * Look up the user for a specified legacy id.
     *
     * @param legacyId The legacy id of the user.
     *
     * @return A summary of the corresponding user.
     */
    @GET @Path("/by-legacy-id/{id}")
    User userByLegacyId(@PathParam("id") String legacyId);

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
