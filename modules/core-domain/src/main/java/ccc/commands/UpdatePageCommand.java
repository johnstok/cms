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

import ccc.api.PageDelta;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand extends UpdateResourceCommand{

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdatePageCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }


    /**
     * Update a page.
     *
     * @param actor
     * @param happenedOn
     * @param id
     * @param delta
     * @param comment
     * @param isMajorEdit
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final PageDelta delta,
                        final String comment,
                        final boolean isMajorEdit)
                               throws UnlockedException, LockMismatchException {

        final Page page = _dao.find(Page.class, id);
        page.confirmLock(actor);

        page.workingCopy(delta);
        page.applySnapshot();

        update(page, comment, isMajorEdit, actor, happenedOn);
    }
}
