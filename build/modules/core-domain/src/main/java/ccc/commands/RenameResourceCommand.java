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
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public RenameResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Rename a resource.
     *
     * @param actor The actor performing the rename.
     * @param happenedOn The date rename took place.
     * @param resourceId The resource to rename.
     * @param name The new name for the resource.
     * @throws UnlockedException If the resource is unlocked.
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws ResourceExistsException If a resource already exists in the
     *  parent folder with the specified name.
     */
    public void rename(final User actor,
                       final Date happenedOn,
                       final UUID resourceId,
                       final String name)
      throws UnlockedException, LockMismatchException, ResourceExistsException {
        final Resource resource = _dao.find(Resource.class, resourceId);
        resource.confirmLock(actor);

        final ResourceName newName = new ResourceName(name);
        if (resource.parent().hasEntryWithName(newName)) {
            throw new ResourceExistsException(resource.parent(), newName);
        }

        resource.name(new ResourceName(name));

        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.NAME, resource.name().toString());
        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_RENAME,
                happenedOn,
                resource.id(),
                ss.getDetail());
        _audit.record(le);
    }
}
