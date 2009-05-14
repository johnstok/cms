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
import ccc.domain.ResourceExistsException;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: clears the working copy for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ClearWorkingCopyCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Clear a resource's working copy.
     *
     * @param id The resource's id.
     * @param actor The user that unpublished the resource.
     * @param happenedOn The date that the resource was unpublished.
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws ResourceExistsException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId)
      throws UnlockedException, LockMismatchException, ResourceExistsException {
        final Resource r = _dao.find(Resource.class, resourceId);
        r.confirmLock(actor);

        r.clearWorkingCopy();

        // FIXME: Audit this action.
    }
}
