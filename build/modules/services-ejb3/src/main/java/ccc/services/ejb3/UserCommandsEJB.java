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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.CreateUserCommand;
import ccc.commands.UpdateCurrentUserCommand;
import ccc.commands.UpdatePasswordAction;
import ccc.commands.UpdateUserCommand;
import ccc.domain.CccCheckedException;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
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
public class UserCommandsEJB
    extends
        BaseCommands
    implements
        Users {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private LogEntryRepository _audit;



    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public UserDto createUser(final UserDto delta) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(
                loggedInUser(_context), new Date(), delta));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUser(final UUID userId, final UserDto delta) {
        new UpdateUserCommand(_bdao, _audit).execute(
            loggedInUser(_context), new Date(), userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updateUserPassword(final UUID userId, final UserDto user) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            loggedInUser(_context),
            new Date(),
            userId,
            user.getPassword());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateYourUser(final UUID userId,
                               final UserDto user)
                                                 throws CommandFailedException {
        try {
        new UpdateCurrentUserCommand(_bdao, _audit).execute(
            loggedInUser(_context),
            new Date(),
            userId,
            user.getEmail(),
            user.getPassword());
        } catch (final CccCheckedException e) {
            throw fail(_context, e);
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
        return mapUser(loggedInUser(_context));
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
                                                    final String username) {
        return mapUsers(_users.listUsersWithUsername(username));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public UserDto userDelta(final UUID userId) {
        return
        deltaUser(_users.find(userId));
    }


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
    }
}
