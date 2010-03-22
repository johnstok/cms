/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.UserDto;
import ccc.types.Username;


/**
 * Implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/users")
@Consumes("application/json")
@Produces("application/json")
@NoCache
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


    /** {@inheritDoc} */
    @Override
    public DtoCollection<UserDto> listUsers(final String username,
                                            final String email,
                                            final String groups,
                                            final int pageNo,
                                            final int pageSize) {

        return getUsers().listUsers(username, email, groups, pageNo, pageSize);
    }

}
