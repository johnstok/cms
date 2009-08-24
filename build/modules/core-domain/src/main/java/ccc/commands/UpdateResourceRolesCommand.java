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

import java.util.Collection;
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
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateResourceRolesCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Update the security roles for the specified resource.
     *
     * @param id The id of the resource to update.
     * @param roles The new roles.
     * @param actor The actor that made the change.
     * @param happenedOn When the update took place.
     *
     * @throws UnlockedException If the resource is unlocked.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final Collection<String> roles)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, id);
        r.confirmLock(actor);

        r.roles(roles);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.RESOURCE_CHANGE_ROLES,
            happenedOn,
            id,
            new Snapshot(r).getDetail());
        _audit.record(le);
    }

}
