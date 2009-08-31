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
package ccc.commands;

import java.util.Date;
import java.util.UUID;

import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.JsonImpl;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.types.CommandType;


/**
 * Command: locks a resource.
 *
 * @author Civic Computing Ltd.
 */
public class LockResourceCommand {

    private final Repository      _repository;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public LockResourceCommand(final Repository repository, final AuditLog audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Lock the specified resource.
     * <p>The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId) throws LockMismatchException {
        final Resource r = _repository.find(Resource.class, resourceId);
        r.lock(actor);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_LOCK,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail());
        _audit.record(le);
    }
}
