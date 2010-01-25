/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateUserCommand;
import ccc.commands.UpdateCurrentUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.User;
import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.UserDto;
import ccc.types.Username;


/**
 * EJB implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Users.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Users.class)
@RolesAllowed({})
public class UsersEJB
    extends
        AbstractEJB
    implements
        Users {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public UserDto createUser(final UserDto delta) throws RestException {
        try {
            return
                new CreateUserCommand(getUsers(), getGroups(), getAuditLog())
                .execute(currentUser(), new Date(), delta)
                .toDto();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUser(final UUID userId, final UserDto delta)
    throws RestException {
        try {
            new UpdateUserCommand(
                getUsers(),
                getAuditLog(),
                getGroups(),
                userId,
                delta)
            .execute(
                currentUser(),
                new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUserPassword(final UUID userId, final UserDto user)
    throws RestException {
        try {
            new UpdatePasswordAction(getUsers(), getAuditLog()).execute(
                currentUser(),
                new Date(),
                userId,
                user.getPassword());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR, API_USER})
    public void updateYourUser(final UUID userId,
                               final UserDto user)
                                                 throws RestException {
        try {
            new UpdateCurrentUserCommand(
                getUsers(),
                getAuditLog(),
                userId,
                user)
            .execute(
                currentUser(),
                new Date());
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Boolean usernameExists(final Username username) {
        return
            Boolean.valueOf(getUsers().usernameExists(username.toString()));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsers() {
        return User.map(getUsers().listUsers());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsersWithEmail(final String email) {
        return User.map(getUsers().listUsersWithEmail(email));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public Collection<UserDto> listUsersWithRole(final String role) {
        return User.map(getUsers().listUsersWithRole(role));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsersWithUsername(
                                                    final Username username) {
        return User.map(getUsers().listUsersWithUsername(username.toString()));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public UserDto userDelta(final UUID userId) throws RestException {
        try {
            return getUsers().find(userId).toDto();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public UserDto userByLegacyId(final String legacyId) throws RestException {
        try {
            return getUsers().userByLegacyId(legacyId).toDto();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER, API_USER})
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        return getUsers().listMetadataValuesWithKey(key);
    }




    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public UserDto loggedInUser() {
        try {
            return currentUser().toDto();
        } catch (final EntityNotFoundException e) {
            return null;
        }
    }
}
