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
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.domain.WorkingCopyAware;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: clears the working copy for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ClearWorkingCopyCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Clear a resource's working copy.
     *
     * @param resourceId The resource's id.
     * @param actor The user that executed the command.
     * @param happenedOn The date the command was executed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId)
                               throws UnlockedException, LockMismatchException {
        final WorkingCopyAware<?> r =
            _dao.find(WorkingCopyAware.class, resourceId);
        r.confirmLock(actor);

        r.clearWorkingCopy();

        _audit.record(
            new LogEntry(
                actor,
                CommandType.RESOURCE_CLEAR_WC,
                happenedOn,
                resourceId,
                null,
                null,
                false));
    }
}
