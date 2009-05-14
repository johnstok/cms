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
import java.util.UUID;

import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: unpublish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnpublishResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UnpublishResourceCommand(final Dao dao,
                                              final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param actor The user that unpublished the resource.
     * @param happenedOn The date that the resource was unpublished.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, resourceId);
        r.confirmLock(actor);

        r.unpublish();

        _audit.recordUnpublish(r, actor, happenedOn);
    }
}
