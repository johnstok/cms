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
import ccc.api.JsonKeys;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: unpublish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnpublishResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UnpublishResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, resourceId);
        r.confirmLock(actor);

        r.unpublish();

        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.UNPUBLISH, actor.id().toString());
        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_UNPUBLISH,
                happenedOn,
                resourceId,
                ss.getDetail());
        _audit.record(le);
    }
}
