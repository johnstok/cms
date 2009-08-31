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
import ccc.domain.LogEntry;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.JsonImpl;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.types.CommandType;


/**
 * Command: move a resource to another folder.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceCommand {

    private final Repository      _repository;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public MoveResourceCommand(final Repository repository, final AuditLog audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final UUID newParentId) throws CccCheckedException {
        final Resource resource = _repository.find(Resource.class, resourceId);
        resource.confirmLock(actor);

        final Folder newParent = _repository.find(Folder.class, newParentId);
        resource.parent().remove(resource);
        newParent.add(resource);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_MOVE,
                happenedOn,
                resourceId,
                new JsonImpl(resource).getDetail());
        _audit.record(le);
    }

}
