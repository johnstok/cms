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
import ccc.api.PageDelta;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.domain.WorkingCopyNotSupportedException;
import ccc.domain.WorkingCopySupport;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: updates the working copy for a page.
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
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final PageDelta delta)
                               throws UnlockedException, LockMismatchException {
        final Page r = _dao.find(Page.class, resourceId);
        r.confirmLock(actor);

        r.workingCopy(delta);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.RESOURCE_UPDATE_WC,
                happenedOn,
                resourceId,
                new Snapshot(delta).getDetail()));
    }

    /**
     * Updates the working copy.
     *
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     * @throws WorkingCopyNotSupportedException If the resource is not working
     *  copy aware.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final long revisionNo)
                                       throws UnlockedException,
                                              LockMismatchException,
                                              WorkingCopyNotSupportedException {
        final Resource r =
            _dao.find(Resource.class, resourceId);
        r.confirmLock(actor);

        if (r instanceof WorkingCopySupport<?, ?, ?>) {
            final WorkingCopySupport<?, ?, ?> wcAware =
                (WorkingCopySupport<?, ?, ?>) r;
            wcAware.setWorkingCopyFromRevision((int) revisionNo);

            final Snapshot ss = new Snapshot();
            ss.set(JsonKeys.REVISION, Long.valueOf(revisionNo));
            _audit.record(
                new LogEntry(
                    actor,
                    CommandType.RESOURCE_UPDATE_WC,
                    happenedOn,
                    resourceId,
                    ss.getDetail()));
        } else {
            throw new WorkingCopyNotSupportedException(r);
        }
    }
}
