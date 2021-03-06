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

import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.synchronous.Users;
import ccc.api.types.DBC;
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
    public User retrieveCurrent() {
        try {
            return _delegate.retrieveCurrent();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User retrieve(final UUID userId) {
        try {
            return _delegate.retrieve(userId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        try {
            return _delegate.usernameExists(username);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User create(final User user) {
        try {
            return _delegate.create(user);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId, final User pu) {
        try {
            _delegate.updateUserPassword(userId, pu);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User updateCurrent(final User user) {
        try {
            return _delegate.updateCurrent(user);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User update(final UUID userId, final User delta) {
        try {
            return _delegate.update(userId, delta);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public User userByLegacyId(final String legacyId) {
        try {
            return _delegate.userByLegacyId(legacyId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        try {
            return _delegate.listUserMetadataValuesWithKey(key);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<User> query(final String username,
                                            final String email,
                                            final String groups,
                                            final String metadataKey,
                                            final String metadataValue,
                                            final String sort,
                                            final SortOrder order,
                                            final int pageNo,
                                            final int pageSize) {
        try {
            return _delegate.query(
                username,
                email,
                groups,
                metadataKey,
                metadataValue,
                sort,
                order,
                pageNo,
                pageSize);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    @Override
    public void resetPassword(String password, String token) {
        try {
            _delegate.resetPassword(password, token);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    @Override
    public void sendToken(String username) {
        try {
            _delegate.sendToken(username);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    @Override
    public void delete(UUID userId) {
        try {
            _delegate.delete(userId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }
}
