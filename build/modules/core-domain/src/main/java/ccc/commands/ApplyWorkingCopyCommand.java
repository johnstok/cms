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
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.domain.WorkingCopyAware;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: apply the current working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand extends UpdateResourceCommand {


    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public ApplyWorkingCopyCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Applies the current working copy to update a resource.
     *
     * @param id The resource's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final String comment,
                        final boolean isMajorEdit)
                               throws UnlockedException, LockMismatchException {
        final WorkingCopyAware<?> r = getDao().find(WorkingCopyAware.class, id);
        r.confirmLock(actor);

        r.applySnapshot();

        update(r, comment, isMajorEdit, actor, happenedOn);
    }
}
