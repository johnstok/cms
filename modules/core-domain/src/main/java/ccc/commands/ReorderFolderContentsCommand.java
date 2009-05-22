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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: reorder the entries of a folder.
 *
 * @author Civic Computing Ltd.
 */
public class ReorderFolderContentsCommand {

    private final Dao _dao;
    private final AuditLog    _audit;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ReorderFolderContentsCommand(final Dao dao,
                                        final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Reorder a folder's entries.
     *
     * @param folderId The folder to re-order.
     * @param order The new order for resources.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID folderId,
                        final List<UUID> order)
                               throws UnlockedException, LockMismatchException {
        final Folder f = _dao.find(Folder.class, folderId);
        f.confirmLock(actor);

        final User u = actor;
        final List<Resource> newOrder = new ArrayList<Resource>();
        final List<Resource> currentOrder = f.entries();
        for (final UUID resourceId : order) {
            for (final Resource r : currentOrder) {
                if (r.id().equals(resourceId)) {
                    newOrder.add(r);
                }
            }
        }
        f.reorder(newOrder);

        _audit.recordReorder(f, u, happenedOn);
    }
}
