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
package ccc.services;

import java.util.List;

import ccc.domain.CreatorRoles;
import ccc.domain.User;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
interface UserManager {

    /**
     * Create new user.
     *
     * @param user The user to be created.
     */
    void createUser(User user);

    /**
     * List all users.
     *
     * @return List of users.
     */
    List<User> listUsers();

    /**
     * List all users with the specified role.
     *
     * @param role The role to filter on.
     * @return The users with the specified role.
     */
    List<User> listUsersWithRole(CreatorRoles role);
}
