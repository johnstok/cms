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

import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.UserDto;
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
    public UserDto loggedInUser() {
        return getUsers().loggedInUser();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserDto> listUsers() {
        return getUsers().listUsers();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserDto> listUsersWithEmail(final String email) {
        return getUsers().listUsersWithEmail(email);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserDto> listUsersWithRole(final String role) {
        return getUsers().listUsersWithRole(role);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserDto> listUsersWithUsername(final Username username) {
        return getUsers().listUsersWithUsername(username);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userDelta(final UUID userId) throws RestException {
        return getUsers().userDelta(userId);
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        return getUsers().usernameExists(username);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto createUser(final UserDto user)
    throws RestException {
        return getUsers().createUser(user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId,
                                   final UserDto pu)
    throws RestException {
        getUsers().updateUserPassword(userId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final UUID userId, final UserDto user)
    throws RestException {
        getUsers().updateYourUser(userId, user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final UUID userId, final UserDto delta)
    throws RestException {
        getUsers().updateUser(userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userByLegacyId(final String legacyId) throws RestException {
        return getUsers().userByLegacyId(legacyId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        return getUsers().listUserMetadataValuesWithKey(key);
    }
}
