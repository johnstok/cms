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
package ccc.actions;

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
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ReorderFolderContentsCommand {

    private final ResourceDao _dao;
    private final AuditLog    _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ReorderFolderContentsCommand(final ResourceDao dao,
                                        final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param actor
     * @param happenedOn
     * @param folderId
     * @param order
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID folderId,
                        final List<UUID> order)
                               throws UnlockedException, LockMismatchException {
        final Folder f = _dao.findLocked(Folder.class, folderId, actor);
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
