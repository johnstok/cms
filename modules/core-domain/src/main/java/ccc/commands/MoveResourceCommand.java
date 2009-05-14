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

import ccc.domain.Folder;
import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: move a resource to another folder.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public MoveResourceCommand(final Dao dao,
                                              final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     * @param actor
     * @param happenedOn
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws ResourceExistsException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final UUID newParentId)
      throws UnlockedException, LockMismatchException, ResourceExistsException {
        final Resource resource = _dao.find(Resource.class, resourceId);
        resource.confirmLock(actor);

        final Folder newParent = _dao.find(Folder.class, newParentId);
        resource.parent().remove(resource);
        newParent.add(resource);

        _audit.recordMove(resource, actor, happenedOn);
    }

}
