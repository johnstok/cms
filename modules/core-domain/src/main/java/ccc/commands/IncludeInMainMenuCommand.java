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
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.CommandType;


/**
 * Command: include/exclude a resource in the main menu.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public IncludeInMainMenuCommand(final Dao dao,
                                    final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Specify whether this resource should be included in the main menu.
     *
     * @param id The id of the resource to change.
     * @param b True if the resource should be included; false otherwise.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final boolean b)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, id);
        r.confirmLock(actor);

        r.includeInMainMenu(b);

        final CommandType command =
            (b) ? CommandType.RESOURCE_INCLUDE_IN_MM
                : CommandType.RESOURCE_REMOVE_FROM_MM;

        final LogEntry le =
            new LogEntry(
                actor, command, happenedOn, id, new Snapshot(r).getDetail());
        _audit.record(le);
    }
}
