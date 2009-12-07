/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.persistence;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
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
     * @param legacyId The legacy Id.
     *
     * @throws EntityNotFoundException If no user exists for the specified ID.
     *
     * @return The user corresponding to 'legacyId'.
     */
    User userByLegacyId(String legacyId) throws EntityNotFoundException;


    /**
     * Return list of unique values in user metadata with given key.
     *
     * @param key The key for the metadata values.
     * @return The list of values.
     */
    List<String> listMetadataValuesWithKey(final String key);
}
