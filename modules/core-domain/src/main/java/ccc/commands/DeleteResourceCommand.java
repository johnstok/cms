/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.Date;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;


/**
 * Command: delete a resource.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteResourceCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public DeleteResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Delete a resource.
     *
     * @param resourceId The id of the resource to delete.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId) throws CccCheckedException {
        final Resource resource = _repository.find(Resource.class, resourceId);
        resource.confirmLock(actor);

        if (resource.isDeleted()) {
            throw new RuntimeException("Can't delete a deleted resource.");
        }

        final Folder trash = _repository.root(PredefinedResourceNames.TRASH);

        resource.delete();
        resource.name(new ResourceName(resource.id().toString()));
        resource.parent().remove(resource);
        trash.add(resource);
        resource.unlock(actor);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_DELETE,
                happenedOn,
                resourceId,
                new JsonImpl(resource).getDetail());
        _audit.record(le);
    }
}
