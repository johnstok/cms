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
import ccc.persistence.AuditLog;
import ccc.persistence.AuditLogImpl;
import ccc.persistence.UserCommands;
import ccc.persistence.UserLookup;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.UserSummary;
import ccc.types.ID;


/**
 * EJB implementation of the {@link UserCommands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=UserCommands.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(UserCommands.class)
@RolesAllowed({}) // "ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"
public class UserCommandsEJB  extends
BaseCommands
implements
UserCommands {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private AuditLog           _audit;



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
    public void updateUserPassword(final ID userId, final String password) {
        new UpdatePasswordAction(_bdao, _audit).execute(
            loggedInUser(_context), new Date(), toUUID(userId), password);
    }


    /** {@inheritDoc}
     * @throws CommandFailedException */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateYourUser(final ID userId, final String email, final String password) throws CommandFailedException {
        try {
        new UpdateCurrentUserCommand(_bdao, _audit).execute(
        loggedInUser(_context), new Date(), toUUID(userId), email, password);
        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new AuditLogImpl(_bdao);
        _userLookup = new UserLookup(_bdao);
    }
}
