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
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.api.Duration;


/**
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCachingCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateCachingCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Change the cache duration for a resource.
     *
     * @param actor
     * @param happenedOn
     * @param resourceId
     * @param duration
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final Duration duration)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, resourceId);
        r.confirmLock(actor);

        r.cache(duration);
        _audit.recordUpdateCache(r, actor, happenedOn);
    }

}
