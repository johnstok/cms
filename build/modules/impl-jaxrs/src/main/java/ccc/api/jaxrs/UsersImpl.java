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
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.User;
import ccc.api.core.Users;
import ccc.api.types.DBC;
import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;
import ccc.api.types.Username;


/**
 * Implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
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
    public User loggedInUser() {
        try {
            return _delegate.loggedInUser();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User userDelta(final UUID userId) {
        try {
            return _delegate.userDelta(userId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        try {
            return _delegate.usernameExists(username);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User createUser(final User user) {
        try {
            return _delegate.createUser(user);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId, final User pu) {
        try {
            _delegate.updateUserPassword(userId, pu);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final UUID userId, final User user) {
        try {
            _delegate.updateYourUser(userId, user);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final UUID userId, final User delta) {
        try {
            _delegate.updateUser(userId, delta);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User userByLegacyId(final String legacyId) {
        try {
            return _delegate.userByLegacyId(legacyId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        try {
            return _delegate.listUserMetadataValuesWithKey(key);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<User> listUsers(final String username,
                                            final String email,
                                            final String groups,
                                            final String metadataKey,
                                            final String metadataValue,
                                            final String sort,
                                            final SortOrder order,
                                            final int pageNo,
                                            final int pageSize) {
        try {
            return _delegate.listUsers(
                username,
                email,
                groups,
                metadataKey,
                metadataValue,
                sort,
                order,
                pageNo,
                pageSize);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
