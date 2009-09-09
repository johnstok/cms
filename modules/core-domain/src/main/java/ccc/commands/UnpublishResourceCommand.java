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
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: unpublish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnpublishResourceCommand {

    private final Repository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UnpublishResourceCommand(final Repository repository,
                                    final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId) throws CccCheckedException {
        final Resource r = _repository.find(Resource.class, resourceId);
        r.confirmLock(actor);

        r.unpublish();

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_UNPUBLISH,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail());
        _audit.record(le);
    }
}
