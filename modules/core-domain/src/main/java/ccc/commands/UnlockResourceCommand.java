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

import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.JsonImpl;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.CommandType;


/**
 * Command: unlocks a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UnlockResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws UnlockedException If the resource is unlocked.
     * @throws InsufficientPrivilegesException If the user does not have enough
     *  privileges to perform this command.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId)
      throws UnlockedException, InsufficientPrivilegesException {
        final Resource r = _dao.find(Resource.class, resourceId);
        r.unlock(actor);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_UNLOCK,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail());
        _audit.record(le);
    }
}
