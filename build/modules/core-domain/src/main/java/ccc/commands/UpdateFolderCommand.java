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
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.ResourceOrder;


/**
 * Command: updates a folder.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateFolderCommand(final Repository repository, final LogEntryRepository audit) {
        super(repository, audit);
    }


    /**
     * Update the folder.
     *
     * @param folderId The folder to update.
     * @param order The new sort order.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @param indexPageId The index page.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID folderId,
                        final ResourceOrder order,
                        final UUID indexPageId,
                        final List<UUID> orderList)
                               throws UnlockedException, LockMismatchException {

        final Folder f = getDao().find(Folder.class, folderId);
        Page p = null;
        if (indexPageId != null) {
            p = getDao().find(Page.class, indexPageId);
        }
        f.confirmLock(actor);
        f.indexPage(p);
        f.sortOrder(order);

        if (orderList != null && !orderList.isEmpty()) {
            final List<Resource> newOrder = new ArrayList<Resource>();
            final List<Resource> currentOrder = f.entries();
            for (final UUID resourceId : orderList) {
                for (final Resource r : currentOrder) {
                    if (r.id().equals(resourceId)) {
                        newOrder.add(r);
                    }
                }
            }
            f.reorder(newOrder);
        }

        f.dateChanged(happenedOn);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.FOLDER_UPDATE,
                happenedOn,
                folderId,
                new JsonImpl(f).getDetail());
        getAudit().record(le);
    }
}
