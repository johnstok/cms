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
package ccc.services;

import ccc.api.UserSummary;
import ccc.domain.CommandFailedException;
import ccc.types.ID;


/**
 * User Commands API, used to update user data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface UserCommands {
    /** NAME : String. */
    String NAME = "PublicUserCommands";


    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A user summary describing the new user.
     */
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
    void updateUser(ID userId, UserSummary delta) throws CommandFailedException;


    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param password The new password to set.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateUserPassword(ID userId, String password)
    throws CommandFailedException;

    /**
     * Update the email and/or password for the current user.
     *
     * @param userId The user's id.
     * @param email The new email to set.
     * @param password The new password to set.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateYourUser(ID userId, String email, String password)
    throws CommandFailedException;
}
