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

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.UserSummary;
import ccc.types.ID;
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
     * @return The corresponding delta.
     */
    @GET @Path("/users/{id}/delta") @NoCache
    UserSummary userDelta(@PathParam("id") ID userId);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    @GET @Path("/users/me") @NoCache
    UserSummary loggedInUser();

    /**
     * Query all users.
     *
     * @return Returns list of users.
     */
    @GET @Path("/users") @NoCache
    Collection<UserSummary> listUsers();

    /**
     * Query users with specified role.
     *
     * @param role The role as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/role/{role}") @NoCache
    Collection<UserSummary> listUsersWithRole(
        @PathParam("role") String role);

    /**
     * Query users with specified username.
     *
     * @param username The username as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/username/{uname}") @NoCache
    Collection<UserSummary> listUsersWithUsername(
        @PathParam("uname") String username);

    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    @GET @Path("/users/{uname}/exists") @NoCache
    boolean usernameExists(@PathParam("uname") Username username);

    /**
     * Query users with specified email.
     *
     * @param email The email as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/email/{email}") @NoCache
    Collection<UserSummary> listUsersWithEmail(
        @PathParam("email") String email);


    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A user summary describing the new user.
     */
    @POST @Path("/users")
    UserSummary createUser(UserSummary delta)
    throws CommandFailedException;


    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    @POST @Path("/users/{id}")
    void updateUser(@PathParam("id") ID userId, UserSummary delta) throws CommandFailedException;


    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     *
     * @throws CommandFailedException If the method fails.
     */
    @POST @Path("/users/{id}/password")
    void updateUserPassword(@PathParam("id") ID userId, UserSummary user)
    throws CommandFailedException;

    /**
     * Update the email and/or password for the current user.
     *
     * @param userId The user's id.
     * @param user New details for the user.
     *
     * @throws CommandFailedException If the method fails.
     */
    @POST @Path("/users/{id}/currentuser")
    void updateYourUser(@PathParam("id") ID userId, UserSummary user)
    throws CommandFailedException;
}
