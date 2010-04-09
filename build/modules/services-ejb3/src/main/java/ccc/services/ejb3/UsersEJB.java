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

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.Users;
import ccc.api.dto.DtoCollection;
import ccc.api.dto.UserCriteria;
import ccc.api.dto.UserDto;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.commands.CreateUserCommand;
import ccc.commands.UpdateCurrentUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.User;
import ccc.persistence.UserRepository;
import ccc.types.SortOrder;
import ccc.types.Username;


/**
 * EJB implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Users.NAME)
@TransactionAttribute(REQUIRED)
@Local(Users.class)
@RolesAllowed({})
public class UsersEJB
    extends
        AbstractEJB
    implements
        Users {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_CREATE)
    public UserDto createUser(final UserDto delta) {
        return
            new CreateUserCommand(getRepoFactory())
            .execute(currentUser(), new Date(), delta)
            .toDto();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_UPDATE)
    public void updateUser(final UUID userId, final UserDto delta) {
        new UpdateUserCommand(
            getRepoFactory(),
            userId,
            delta)
        .execute(
            currentUser(),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_UPDATE)
    public void updateUserPassword(final UUID userId, final UserDto user) {
        new UpdatePasswordAction(getRepoFactory()).execute(
            currentUser(),
            new Date(),
            userId,
            user.getPassword());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(SELF_UPDATE)
    public void updateYourUser(final UUID userId, final UserDto user) {
        execute(
            new UpdateCurrentUserCommand(
                getRepoFactory(),
                userId,
                user));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_READ)
    public Boolean usernameExists(final Username username) {
        return
            Boolean.valueOf(
                getRepoFactory()
                .createUserRepo()
                .usernameExists(username.toString()));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_READ)
    public DtoCollection<UserDto> listUsers(
        final String username,
        final String email,
        final String groups,
        final String metadataKey,
        final String metadataValue,
        final String sort,
        final SortOrder order,
        final int pageNo,
        final int pageSize) {
        final UserRepository userrepo = getRepoFactory().createUserRepo();
        final UserCriteria uc = new UserCriteria(
            username,
            email,
            groups,
            metadataKey,
            metadataValue);
        return new DtoCollection<UserDto>(userrepo.countUsers(uc), User.map(
                userrepo.listUsers(uc, sort, order, pageNo, pageSize)));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_READ)
    public UserDto userDelta(final UUID userId) {
        return
            getRepoFactory()
                .createUserRepo()
                .find(userId)
                .toDto();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_READ)
    public UserDto userByLegacyId(final String legacyId) {
        return
            getRepoFactory()
                .createUserRepo()
                .userByLegacyId(legacyId).toDto();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(USER_READ)
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        return
            getRepoFactory()
                .createUserRepo()
                .listMetadataValuesWithKey(key);
    }


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
