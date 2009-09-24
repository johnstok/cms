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
import ccc.domain.WCAware;
import ccc.domain.WorkingCopyNotSupportedException;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: clears the working copy for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ClearWorkingCopyCommand(final ResourceRepository repository,
                                   final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Clear a resource's working copy.
     *
     * @param resourceId The resource's id.
     * @param actor The user that executed the command.
     * @param happenedOn The date the command was executed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId) throws CccCheckedException {
        final Resource r = _repository.find(Resource.class, resourceId);
        r.confirmLock(actor);

        if (r instanceof WCAware<?>) {
            final WCAware<?> wcAware = (WCAware<?>) r;
            wcAware.clearWorkingCopy();
        } else {
            throw new WorkingCopyNotSupportedException(r);
        }

        _audit.record(
            new LogEntry(
                actor,
                CommandType.RESOURCE_CLEAR_WC,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail()));
    }
}
