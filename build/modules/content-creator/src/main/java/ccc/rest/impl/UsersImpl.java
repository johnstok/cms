/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.CommandFailedException;
import ccc.rest.Users;
import ccc.rest.dto.UserSummary;
import ccc.types.Username;


/**
 * Implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class UsersImpl
    extends
        JaxrsCollection
    implements
        Users {


    /** {@inheritDoc} */
    @Override
    public UserSummary loggedInUser() {
        return getUserCommands().loggedInUser();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsers() {
        return getUserCommands().listUsers();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return getUserCommands().listUsersWithEmail(email);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return getUserCommands().listUsersWithRole(role);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithUsername(
                                                        final String username) {
        return getUserCommands().listUsersWithUsername(username);
    }


    /** {@inheritDoc} */
    @Override
    public UserSummary userDelta(final UUID userId) {
        return getUserCommands().userDelta(userId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final Username username) {
        return getUserCommands().usernameExists(username);
    }


    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserSummary user)
    throws CommandFailedException {
        return getUserCommands().createUser(user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId,
                                   final UserSummary pu)
    throws CommandFailedException {
        getUserCommands().updateUserPassword(userId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final UUID userId, final UserSummary user)
    throws CommandFailedException {
        getUserCommands().updateYourUser(userId, user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final UUID userId, final UserSummary delta)
    throws CommandFailedException {
        getUserCommands().updateUser(userId, delta);
    }
}
