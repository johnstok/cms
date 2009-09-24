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

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.ResourceName;


/**
 * Command: rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public RenameResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Rename a resource.
     *
     * @param actor The actor performing the rename.
     * @param happenedOn The date rename took place.
     * @param resourceId The resource to rename.
     * @param name The new name for the resource.
     * @throws CccCheckedException If the command fails.
     */
    public void rename(final User actor,
                       final Date happenedOn,
                       final UUID resourceId,
                       final String name) throws CccCheckedException {
        final Resource resource = _repository.find(Resource.class, resourceId);
        resource.confirmLock(actor);

        final ResourceName newName = new ResourceName(name);
        final Resource existingResource =
            resource.parent().entryWithName(newName);
        if (null!=existingResource) {
            throw new ResourceExistsException(
                resource.parent(), existingResource);
        }

        resource.name(new ResourceName(name));

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_RENAME,
                happenedOn,
                resource.id(),
                new JsonImpl(resource).getDetail());
        _audit.record(le);
    }
}
