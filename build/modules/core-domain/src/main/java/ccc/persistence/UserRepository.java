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

import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

import ccc.domain.EntityNotFoundException;
import ccc.domain.User;

/**
 * API for user repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface UserRepository {

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
     *
     * @throws EntityNotFoundException If no user exists for the specified ID.
     *
     * @return The user corresponding to 'userId'.
     */
    User find(UUID userId) throws EntityNotFoundException;

    /**
     * Look up a user from a JAAS principal.
     *
     * @param p The principal.
     *
     * @throws EntityNotFoundException If no user exists for the corresponding
     *  principal.
     *
     * @return The corresponding CCC user.
     */
    User loggedInUser(final Principal p) throws EntityNotFoundException;


    /**
     * Create a new user.
     *
     * @param user The new user to add.
     */
    void create(User user);

    /**
     * Look up a user using the legacy id.
     *
     * @param legacyId
     *
     * @throws EntityNotFoundException If no user exists for the specified ID.
     *
     * @return The user corresponding to 'legacyId'.
     */
    User userByLegacyId(String legacyId) throws EntityNotFoundException;
}
