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

import ccc.api.CommandType;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: locks a resource.
 *
 * @author Civic Computing Ltd.
 */
public class LockResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public LockResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
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
        final Resource r = _dao.find(Resource.class, resourceId);
        r.lock(actor);

        final Snapshot ss = new Snapshot();
        ss.set("lock", actor.id().toString());
        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_LOCK,
                happenedOn,
                resourceId,
                ss.getDetail());
        _audit.record(le);
    }
}
