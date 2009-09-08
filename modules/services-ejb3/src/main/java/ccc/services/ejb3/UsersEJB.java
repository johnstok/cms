/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateUserCommand;
import ccc.commands.UpdateCurrentUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.CccCheckedException;
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
@TransactionAttribute(REQUIRES_NEW)
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
    public UserDto createUser(final UserDto delta) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(
                currentUser(), new Date(), delta));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUser(final UUID userId, final UserDto delta) {
        new UpdateUserCommand(_bdao, _audit).execute(
            currentUser(), new Date(), userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUserPassword(final UUID userId, final UserDto user) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            currentUser(),
            new Date(),
            userId,
            user.getPassword());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateYourUser(final UUID userId,
                               final UserDto user)
                                                 throws RestException {
        try {
        new UpdateCurrentUserCommand(_bdao, _audit).execute(
            currentUser(),
            new Date(),
            userId,
            user.getEmail(),
            user.getPassword());
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public boolean usernameExists(final Username username) {
        return _users.usernameExists(username.toString());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public UserDto loggedInUser() {
        return mapUser(currentUser());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsers() {
        return mapUsers(_users.listUsers());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsersWithEmail(final String email) {
        return mapUsers(_users.listUsersWithEmail(email));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsersWithRole(final String role) {
        return mapUsers(_users.listUsersWithRole(role));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<UserDto> listUsersWithUsername(
                                                    final Username username) {
        return mapUsers(_users.listUsersWithUsername(username.toString()));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public UserDto userDelta(final UUID userId) {
        return
        deltaUser(_users.find(userId));
    }
}
