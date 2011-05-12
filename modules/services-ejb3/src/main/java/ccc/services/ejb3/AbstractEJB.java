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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.EJBContext;
import javax.ejb.TimerService;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.api.exceptions.UnauthorizedException;
import ccc.api.exceptions.UsernameNotFoundException;
import ccc.api.types.DBC;
import ccc.commands.Command;
import ccc.commands.CommandFactory;
import ccc.commons.Exceptions;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.messaging.Producer;
import ccc.persistence.DataRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.RepositoryFactory;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;


/**
 * Abstract base class for EJBs.
 *
 * @author Civic Computing Ltd.
 */
abstract class AbstractEJB {

    private static final Logger LOG = Logger.getLogger(AbstractEJB.class);

    @javax.annotation.Resource private EJBContext    _context;
    @PersistenceContext        private EntityManager _em;
    @Resource(
        name="topic_conn_factory",
        authenticationType=AuthenticationType.CONTAINER,
        type=TopicConnectionFactory.class)
    private TopicConnectionFactory _connectionFactory;

    @Resource(name="topic_broadcast")
    private Topic _broadcast;

    private UserRepository     _users;
    private ResourceRepository _resources;
    private LogEntryRepository _audit;
    private DataRepository     _dm;

    private CommandFactory     _cFactory;
    private IRepositoryFactory _rf;
    private Producer           _producer;


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _rf = new RepositoryFactory(new JpaRepository(_em));
        _audit = _rf.createLogEntryRepo();
        _users = _rf.createUserRepo();
        _resources = _rf.createResourceRepository();
        _dm = _rf.createDataRepository();
        _producer = new JmsProducer(_connectionFactory, _broadcast);
        _cFactory = new CommandFactory(_resources, _audit, _dm, _producer);
    }


    /**
     * Accessor.
     *
     * @return Returns the broadcast producer.
     */
    protected Producer getProducer() {
        return _producer;
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
     * Execute a command on behalf of another user.
     *
     * @param <T> The command's return type.
     * @param command The command to execute.
     * @param actorId The actor executing the command.
     * @param happenedOn When the command was executed.
     *
     * @return The command's return value.
     */
    protected final <T> T sudoExecute(final Command<T> command,
                                      final UUID actorId,
                                      final Date happenedOn) {
        return command.execute(_users.find(actorId), happenedOn);
    }


    /**
     * Execute a command on behalf of the current user.
     *
     * @param <T> The command's return type.
     * @param command The command to execute.
     *
     * @return The command's return value.
     */
    protected final <T> T execute(final Command<T> command) {
        return command.execute(currentUser(), new Date());
    }


    /**
     * Look up the user for the specified ID.
     *
     * @param userId The user's ID.
     *
     * @return The corresponding user.
     */
    protected UserEntity userForId(final UUID userId) {
        final UserEntity u = _users.find(userId);
        return u;
    }


    /**
     * Accessor.
     * TODO: Throw 'invalid session exception instead.
     *
     * @return The currently logged in user.
     */
    protected UserEntity currentUser() {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }


    /**
     * Accessor.
     *
     * @return The currently logged in user's ID.
     */
    protected UUID currentUserId() {
        return currentUser().getId();
    }


    /**
     * Check that a resource is readable by a user.
     *
     * @param r The resource to check.
     */
    protected void checkRead(final ResourceEntity r) {

        DBC.require().notNull(r);

        final UserEntity u = currentUser();

        if (!r.isReadableBy(u)) {
            throw new UnauthorizedException(
                r.getId(), (null==u) ? null : u.getId());
        }
    }


    /**
     * Check that a resource is write-able by a user.
     *
     * @param r The resource to check.
     */
    protected void checkWrite(final ResourceEntity r) {

        DBC.require().notNull(r);

        UserEntity u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        if (!r.isWriteableBy(u)) {
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
            final UserEntity u = currentUser();
            for (final String permission : permissions) {
                if (u.hasPermission(permission)) { return; }
            }
            throw new RuntimeException("Caller unauthorized.");

        } catch (final EntityNotFoundException e) {
            throw new UsernameNotFoundException();
        }
    }


    /**
     * Filter a collection of resources based on accessibility.
     *
     * @param u The user reading the collection.
     * @param resources The un-filtered collection.
     *
     * @return Subset of the input collection, accessible to the specified user.
     */
    protected Collection<? extends ResourceEntity> filterAccessibleTo(
                            final UserEntity u,
                            final List<? extends ResourceEntity> resources) {
        final List<ResourceEntity> accessible = new ArrayList<ResourceEntity>();
        for (final ResourceEntity r : resources) {
            if (r.isReadableBy(u)) { accessible.add(r); }
        }
        return accessible;
    }
}
