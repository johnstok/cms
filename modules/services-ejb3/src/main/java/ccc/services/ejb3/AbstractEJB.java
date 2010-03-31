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

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJBContext;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.commands.Command;
import ccc.commands.CommandFactory;
import ccc.commons.Exceptions;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.DataRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;
import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.types.DBC;


/**
 * Abstract base class for EJBs.
 *
 * @author Civic Computing Ltd.
 */
abstract class AbstractEJB {

    private static Logger log = Logger.getLogger(AbstractEJB.class);

    @javax.annotation.Resource private EJBContext    _context;
    @PersistenceContext        private EntityManager   _em;

    private UserRepository     _users;
    private ResourceRepository _resources;
    private LogEntryRepository _audit;
    private DataRepository     _dm;

    private CommandFactory     _cFactory;
    private IRepositoryFactory _rf;


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _rf = IRepositoryFactory.DEFAULT.create(_em);
        _audit = _rf.createLogEntryRepo();
        _users = _rf.createUserRepo();
        _resources = _rf.createResourceRepository();
        _dm = _rf.createDataRepository();
        _cFactory = new CommandFactory(_resources, _audit, _dm);
    }


    /**
     * Accessor.
     *
     * @return Returns the repository factory.
     */
    protected IRepositoryFactory getRepoFactory() {
        return _rf;
    }


    /**
     * Accessor.
     *
     * @return Returns the command factory.
     */
    public CommandFactory commands() {
        return _cFactory;
    }


    /**
     * Accessor.
     *
     * @return The timer service for this EJB.
     */
    protected final TimerService getTimerService() {
        return _context.getTimerService();
    }


    /**
     * Fail a method throwing an application exception.
     *
     * @param e The checked exception to convert.
     *
     * @return The corresponding application exception.
     */
    protected RestException fail(final CccCheckedException e) {
        guaranteeRollback(); // CRITICAL
        final RestException cfe = e.toRemoteException();
        log.debug(
            "Handled local exception: "+cfe.getFailure().getExceptionId(), e);
        return cfe;
    }


    /**
     * Ensure the transaction is rolled back.
     */
    protected void guaranteeRollback() { _context.setRollbackOnly(); }


    /**
     * Execute a command on behalf of another user.
     *
     * @param <T> The command's return type.
     * @param command The command to execute.
     * @param actorId The actor executing the command.
     * @param happenedOn When the command was executed.
     *
     * @return The command's return value.
     *
     * @throws RestException If execution fails.
     */
    protected final <T> T sudoExecute(final Command<T> command,
                                      final UUID actorId,
                                      final Date happenedOn)
                                   throws RestException, UnauthorizedException {
        try {
            return command.execute(_users.find(actorId), happenedOn);
        } catch (final CccCheckedException e) {
            throw fail(e);
        } catch (final UnauthorizedException e) {
            guaranteeRollback();
            throw e;
        }
    }


    /**
     * Execute a command on behalf of the current user.
     *
     * @param <T> The command's return type.
     * @param command The command to execute.
     *
     * @return The command's return value.
     *
     * @throws RestException If execution fails.
     */
    protected final <T> T execute(final Command<T> command)
                                   throws RestException, UnauthorizedException {
        try {
            return command.execute(currentUser(), new Date());
        } catch (final CccCheckedException e) {
            throw fail(e);
        } catch (final UnauthorizedException e) {
            guaranteeRollback();
            throw e;
        }
    }


    /**
     * Look up the user for the specified ID.
     *
     * @param userId The user's ID.
     *
     * @return The corresponding user.
     *
     * @throws EntityNotFoundException If a corresponding user doesn't exist.
     */
    protected User userForId(final UUID userId) throws EntityNotFoundException {
        final User u = _users.find(userId);
        return u;
    }


    /**
     * Accessor.
     * TODO: Throw 'invalid session exception instead.
     *
     * @throws EntityNotFoundException If no user corresponds to the current
     *  principal.
     *
     * @return The currently logged in user.
     */
    protected User currentUser() throws EntityNotFoundException {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }


    /**
     * Accessor.
     *
     * @throws EntityNotFoundException If no user corresponds to the current
     *  principal.
     *
     * @return The currently logged in user's ID.
     */
    protected UUID currentUserId() throws EntityNotFoundException {
        return currentUser().getId();
    }


    /**
     * Check that a resource is readable by a user.
     *
     * @param r The resource to check.
     * @throws UnauthorizedException If the resource cannot be read.
     */
    protected void checkRead(final Resource r) throws UnauthorizedException {

        DBC.require().notNull(r);

        User u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        if (!r.isReadableBy(u)) {
            guaranteeRollback(); // CRITICAL
            throw new UnauthorizedException(
                r.getId(), (null==u) ? null : u.getId());
        }
    }


    /**
     * Check that a resource is write-able by a user.
     *
     * @param r The resource to check.
     * @throws UnauthorizedException If the resource cannot be written.
     */
    protected void checkWrite(final Resource r) throws UnauthorizedException {

        DBC.require().notNull(r);

        User u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        if (!r.isWriteableBy(u)) {
            guaranteeRollback();
            throw new UnauthorizedException(
                r.getId(), (null==u) ? null : u.getId());
        }
    }


    /**
     * Check that the current user has ONE OF the specified permissions.
     *
     * @param permissions The permissions to check.
     */
    protected void checkPermission(final String... permissions) {
        try {
            final User u = currentUser();
            for (final String permission : permissions) {
                if (u.hasPermission(permission)) { return; }
            }
            throw new RuntimeException("Caller unauthorized.");

        } catch (final EntityNotFoundException e) {
            throw new RuntimeException("No user available.", e);
        }
    }

}
