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

import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;

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
import ccc.rest.UserCommands;
import ccc.rest.dto.UserSummary;
import ccc.types.ID;
import ccc.types.Username;


/**
 * EJB implementation of the {@link UserCommands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=UserCommands.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(UserCommands.class)
@RolesAllowed({}) // "ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"
public class UserCommandsEJB
    extends
        BaseCommands
    implements
        UserCommands {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private LogEntryRepository           _audit;



    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public UserSummary createUser(final UserSummary delta) {
        return mapUser(
            new CreateUserCommand(_bdao, _audit).execute(
                loggedInUser(_context), new Date(), delta));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updateUser(final ID userId, final UserSummary delta) {
        new UpdateUserCommand(_bdao, _audit).execute(
            loggedInUser(_context), new Date(), toUUID(userId), delta);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updateUserPassword(final ID userId, final UserSummary user) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            loggedInUser(_context),
            new Date(),
            toUUID(userId),
            user.getPassword());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateYourUser(final ID userId,
                               final UserSummary user)
                                                 throws CommandFailedException {
        try {
        new UpdateCurrentUserCommand(_bdao, _audit).execute(
            loggedInUser(_context),
            new Date(),
            toUUID(userId),
            user.getEmail(),
            user.getPassword());
        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }
    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public boolean usernameExists(final Username username) {
        return _users.usernameExists(username.toString());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public UserSummary loggedInUser() {
        return mapUser(loggedInUser(_context));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public Collection<UserSummary> listUsers() {
        return mapUsers(_users.listUsers());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return mapUsers(_users.listUsersWithEmail(email));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return mapUsers(_users.listUsersWithRole(role));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        return mapUsers(_users.listUsersWithUsername(username));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
    public UserSummary userDelta(final ID userId) {
        return
        deltaUser(_users.find(toUUID(userId)));
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
