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

import ccc.domain.Folder;
import ccc.domain.LockMismatchException;
import ccc.domain.ResourceOrder;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateFolderCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
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
                        final ResourceOrder order)
                               throws UnlockedException, LockMismatchException {

        final Folder f = _dao.find(Folder.class, folderId);
        f.confirmLock(actor);

        f.sortOrder(order);

        // Set folder.dateChanged()?
        _audit.recordUpdateSortOrder(f, actor, happenedOn); // FIXME: Should this just be 'update folder'?
    }
}
