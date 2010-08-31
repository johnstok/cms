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

import static ccc.api.types.Permission.SELF_UPDATE;
import static ccc.api.types.Permission.USER_CREATE;
import static ccc.api.types.Permission.USER_READ;
import static ccc.api.types.Permission.USER_UPDATE;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.Collection;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.core.UserCriteria;
import ccc.api.core.Users;
import ccc.api.types.SortOrder;
import ccc.api.types.Username;
import ccc.commands.CreateUserCommand;
import ccc.commands.UpdateCurrentUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.UserEntity;
import ccc.persistence.UserRepository;


/**
 * EJB implementation of the {@link Users} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Users.NAME)
@TransactionAttribute(REQUIRED)
@Local(Users.class)
public class UsersEJB
    extends
        AbstractEJB
    implements
        Users {


    /** {@inheritDoc} */
    @Override
    public User create(final User delta) {
        checkPermission(USER_CREATE);

        return
            execute(new CreateUserCommand(getRepoFactory(), delta))
            .toDto();
    }


    /** {@inheritDoc} */
    @Override
    public void update(final UUID userId, final User delta) {
        checkPermission(USER_UPDATE);

        execute(
            new UpdateUserCommand(getRepoFactory(), userId, delta));
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final UUID userId, final User user) {
        checkPermission(USER_UPDATE);

        execute(
            new UpdatePasswordAction(
                getRepoFactory(), userId, user.getPassword()));
    }


    /** {@inheritDoc} */
    @Override
    public void updateCurrent(final User user) {
        checkPermission(SELF_UPDATE);

        execute(
            new UpdateCurrentUserCommand(
                getRepoFactory(),
                currentUserId(),
                user));
    }


    /** {@inheritDoc} */
    @Override
    public Boolean usernameExists(final Username username) {
        checkPermission(USER_READ);

        return
            Boolean.valueOf(
                getRepoFactory()
                .createUserRepo()
                .usernameExists(username.toString()));
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
        checkPermission(USER_READ);

        final UserRepository userrepo = getRepoFactory().createUserRepo();
        final UserCriteria uc = new UserCriteria(
            username,
            email,
            groups,
            metadataKey,
            metadataValue);
        final PagedCollection<User> users =
            new PagedCollection<User>(
                userrepo.countUsers(uc),
                User.class,
                UserEntity.map(
                    userrepo.listUsers(uc, sort, order, pageNo, pageSize)));
        users.addLink(
            "me",
            ccc.api.core.ResourceIdentifiers.User.ME);
        users.addLink(
            "self",
            ccc.api.core.ResourceIdentifiers.User.COLLECTION
            + "?{-join|&|count,page,sort,order,email,username,groups}");
        users.addLink(
            "exists",
            ccc.api.core.ResourceIdentifiers.User.EXISTS);
        return users;
    }


    /** {@inheritDoc} */
    @Override
    public User retrieve(final UUID userId) {
        checkPermission(USER_READ);

        return
            getRepoFactory()
                .createUserRepo()
                .find(userId)
                .toDto();
    }


    /** {@inheritDoc} */
    @Override
    public User userByLegacyId(final String legacyId) {
        checkPermission(USER_READ);

        return
            getRepoFactory()
                .createUserRepo()
                .userByLegacyId(legacyId).toDto();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> listUserMetadataValuesWithKey(final String key) {
        checkPermission(USER_READ);

        return
            getRepoFactory()
                .createUserRepo()
                .listMetadataValuesWithKey(key);
    }


    /** {@inheritDoc} */
    @Override
    public User retrieveCurrent() {
        final UserEntity current = currentUser();
        if (current == null) {
            return null;
        }
        return current.toDto();
    }

}
