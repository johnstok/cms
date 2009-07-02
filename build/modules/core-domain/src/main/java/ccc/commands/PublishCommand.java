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

import ccc.api.CommandType;
import ccc.domain.Action;
import ccc.domain.Command;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;


/**
 * Command: publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class PublishCommand
    implements
        Command<Void> {

    private AuditLog _audit;

    /**
     * Constructor.
     *
     * @param audit The audit log to record this command.
     */
    public PublishCommand(final AuditLog audit) {
        _audit = audit;
    }

    /** {@inheritDoc} */
    @Override public Void execute(final Action a, final Date happenedOn)
                                                 throws RemoteExceptionSupport {
        final User publishedBy = a.actor();
        final Resource r = a.subject();

        execute(happenedOn, publishedBy, r);

        return null;
    }

    /**
     * Publishes the resource by specified user.
     *
     * @param r The resource to update.
     * @param publishedBy The id of the publishing user.
     * @param happenedOn The date the resource was published.
     *
     * @throws UnlockedException If the resource is unlocked.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void execute(final Date happenedOn,
                        final User publishedBy,
                        final Resource r)
                               throws UnlockedException, LockMismatchException {

        r.confirmLock(publishedBy);

        r.publish(publishedBy);
        r.dateChanged(happenedOn);

        final Snapshot ss = new Snapshot();
        ss.set("publish", publishedBy.id().toString());
        final LogEntry le =
            new LogEntry(
                publishedBy,
                CommandType.RESOURCE_PUBLISH,
                happenedOn,
                r.id(),
                ss.getDetail());
        _audit.record(le);
    }

}
