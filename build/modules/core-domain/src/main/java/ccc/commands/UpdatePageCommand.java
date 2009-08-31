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
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.rest.PageDelta;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand extends UpdateResourceCommand{

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdatePageCommand(final Repository repository, final AuditLog audit) {
        super(repository, audit);
    }


    /**
     * Update a page.
     *
     * @param id The id of the page to update.
     * @param delta The changes to the page.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final PageDelta delta,
                        final String comment,
                        final boolean isMajorEdit)
                               throws UnlockedException, LockMismatchException {

        final Page page = getDao().find(Page.class, id);
        page.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, isMajorEdit, comment);

        page.workingCopy(delta);
        page.applySnapshot(rm);

        update(page, comment, isMajorEdit, actor, happenedOn);
    }
}
