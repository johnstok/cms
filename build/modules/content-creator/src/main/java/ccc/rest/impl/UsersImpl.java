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

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Users;
import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.UserDto;
import ccc.types.SortOrder;
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

    private Users _delegate;


    public Users getUsers() {
        return (null==_delegate) ? defaultUsers() : _delegate;
    }


    public void setUsers(final Users users) {
        _delegate = users;
    }


    /**
     * Decorate an exiting users implementation with a new {@link UsersImpl}.
     *
     * @param users The implementation to decorate.
     *
     * @return The decorated implementation.
     */
    public static UsersImpl decorate(final Users users) {
        final UsersImpl ui = new UsersImpl();
        ui.setUsers(users);
        return ui;
    }


    /** {@inheritDoc} */
    @Override
    public UserDto loggedInUser() {
        try {
            return getUsers().loggedInUser();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userDelta(final UUID userId) {
        try {
            return getUsers().userDelta(userId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        try {
            return getUsers().usernameExists(username);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public UserDto createUser(final UserDto user) {
        try {
            return getUsers().createUser(user);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId, final UserDto pu) {
        try {
            getUsers().updateUserPassword(userId, pu);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final UUID userId, final UserDto user) {
        try {
            getUsers().updateYourUser(userId, user);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final UUID userId, final UserDto delta) {
        try {
            getUsers().updateUser(userId, delta);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public UserDto userByLegacyId(final String legacyId) {
        try {
            return getUsers().userByLegacyId(legacyId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        try {
            return getUsers().listUserMetadataValuesWithKey(key);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
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
        try {
            return getUsers().listUsers(username,
                email,
                groups,
                metadataKey,
                metadataValue,
                sort,
                order,
                pageNo,
                pageSize);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

}
