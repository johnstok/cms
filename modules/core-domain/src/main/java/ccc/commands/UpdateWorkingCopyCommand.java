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
import ccc.domain.ResourceExistsException;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: updates the working copy for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateWorkingCopyCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Updates the working copy.
     *
     * @param delta The page delta to store in the page.
     * @param resourceId The resource's id.
     * @param actor The user that updated the working copy.
     * @param happenedOn The date that the w.c. was updated.
     *
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws ResourceExistsException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final PageDelta delta)
                               throws UnlockedException, LockMismatchException {
        final Page r = _dao.find(Page.class, resourceId);
        r.confirmLock(actor);

        r.workingCopy(delta);

        // FIXME: Audit this action?
    }
}
