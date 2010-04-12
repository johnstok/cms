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
package ccc.api.jaxrs;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.Users;
import ccc.api.dto.DtoCollection;
import ccc.api.dto.UserDto;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;
import ccc.api.types.Username;


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

    private final Users _delegate;


    /**
     * Constructor.
     *
     * @param users
     */
    public UsersImpl(final Users users) {
        _delegate = DBC.require().notNull(users);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto loggedInUser() {
        return _delegate.loggedInUser();
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userDelta(final UUID userId) {
        return _delegate.userDelta(userId);
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        return _delegate.usernameExists(username);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto createUser(final UserDto user) {
        return _delegate.createUser(user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId, final UserDto pu) {
        _delegate.updateUserPassword(userId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final UUID userId, final UserDto user) {
        _delegate.updateYourUser(userId, user);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final UUID userId, final UserDto delta) {
        _delegate.updateUser(userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userByLegacyId(final String legacyId) {
        return _delegate.userByLegacyId(legacyId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
            return _delegate.listUserMetadataValuesWithKey(key);
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<UserDto> listUsers(final String username,
                                            final String email,
                                            final String groups,
                                            final String metadataKey,
                                            final String metadataValue,
                                            final String sort,
                                            final SortOrder order,
                                            final int pageNo,
                                            final int pageSize) {
        return _delegate.listUsers(username,
            email,
            groups,
            metadataKey,
            metadataValue,
            sort,
            order,
            pageNo,
            pageSize);
    }
}
