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

import ccc.domain.Resource;
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
        final Void result =  execute(happenedOn, publishedBy, r);

        return result;
    }

    public Void execute(final Date happenedOn,
                         final User publishedBy,
                         final Resource r) {

        r.confirmLock(publishedBy);

        r.publish(publishedBy);
        r.dateChanged(happenedOn);

        _audit.recordPublish(r, publishedBy, happenedOn);
        return null;
    }

}
