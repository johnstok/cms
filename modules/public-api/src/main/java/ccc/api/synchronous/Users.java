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
package ccc.api.synchronous;

import java.util.Collection;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
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
     * Retrieve a user.
     *
     * @param userId The user's id.
     *
     * @return The corresponding delta.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.ELEMENT)
    User retrieve(@PathParam("id") UUID userId);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.ME)
    User retrieveCurrent();

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
   @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.COLLECTION)
   PagedCollection<User> query(
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
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.EXISTS)
    Boolean usernameExists(@QueryParam("uname") Username username);


    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     *
     * @return A user summary describing the new user.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.User.COLLECTION)
    User create(User delta);


    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     *
     * @return The updated user.
     */
    @PUT @Path(ccc.api.synchronous.ResourceIdentifiers.User.ELEMENT)
    User update(@PathParam("id") UUID userId, User delta);


    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     */
    @PUT @Path(ccc.api.synchronous.ResourceIdentifiers.User.PASSWORD)
    @Deprecated // Just use update()
    void updateUserPassword(@PathParam("id") UUID userId, User user);

    /**
     * Update the email and/or password for the current user.
     *
     * @param user New details for the user.
     *
     * @return The updated user.
     */
    @PUT @Path(ccc.api.synchronous.ResourceIdentifiers.User.ME)
    User updateCurrent(User user);


    /**
     * Look up the user for a specified legacy id.
     *
     * @param legacyId The legacy id of the user.
     *
     * @return A summary of the corresponding user.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.LEGACY)
    User userByLegacyId(@PathParam("id") String legacyId);

    /**
     * Query metadata values with given key.
     *
     * @param key The key as a string.
     * @return Returns list of users.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.User.METADATA)
    Collection<String> listUserMetadataValuesWithKey(
        @PathParam("key") String key);

    /**
     * Sends an email to user. Email contains link with the correct password
     * reset token.
     *
     * @param username The user's username.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.User.TOKEN)
    void sendToken(@QueryParam("username") String username);

    /**
     * Reset the password with given new password. The token must match the
     * right token sent in email.
     *
     * @param password The new password.
     * @param token The reset token.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.User.RESET_PASSWORD)
    void resetPassword(@QueryParam("password") String password,
                       @QueryParam("token") String token);

    /**
     * Delete the user with the specified id.
     *
     * @param resourceId The id of the existing resource.
     */
    @DELETE @Path(ccc.api.synchronous.ResourceIdentifiers.User.ELEMENT)
    void delete(@PathParam("id") UUID resourceId);
}
