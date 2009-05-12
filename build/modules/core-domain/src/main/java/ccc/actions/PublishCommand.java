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

import java.util.Date;

import ccc.domain.CCCException;
import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;


/**
 * TODO: Add Description for this type.
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
    @Override public Void execute(final Action a, final Date happenedOn) {
        final User publishedBy = a.actor();
        final Resource r = a.subject();
        Void result;
        try {
            result = execute(happenedOn, publishedBy, r);
        } catch (final UnlockedException e) {
            throw new CCCException(e);
        } catch (final LockMismatchException e) {
            throw new CCCException(e);
        }

        return result;
    }

    public Void execute(final Date happenedOn,
                        final User publishedBy,
                        final Resource r)
                               throws UnlockedException, LockMismatchException {

        r.confirmLock(publishedBy);

        r.publish(publishedBy);
        r.dateChanged(happenedOn);

        _audit.recordPublish(r, publishedBy, happenedOn);
        return null;
    }

}
