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
package ccc.persistence;

import java.util.Collection;
import java.util.UUID;

import ccc.domain.User;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface UserManager {

    /** NAME : String. */
    String NAME = "UserManager";


    /**
     * List all users.
     *
     * @return List of users.
     */
    Collection<User> listUsers();


    /**
     * List all users with the specified role.
     *
     * @param role The role to filter on.
     * @return The users with the specified role.
     */
    Collection<User> listUsersWithRole(String role);


    /**
     * List all users with matching username.
     *
     * @param username The username to filter on.
     * @return The users with the specified username.
     */
    Collection<User> listUsersWithUsername(String username);


    /**
     * Query whether a user exists with the specified username.
     *
     * @param username The username.
     * @return True if such a user exists, false otherwise.
     */
    boolean usernameExists(String username);


    /**
     * List all users with matching email.
     *
     * @param email The email to filter on.
     * @return The users with the specified email.
     */
    Collection<User> listUsersWithEmail(String email);


    /**
     * Look up a user from their Id.
     *
     * @param userId The UUID for the user.
     * @return The user corresponding to 'userId'.
     */
    User find(UUID userId);
}
