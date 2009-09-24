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
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.domain.WorkingCopyNotSupportedException;
import ccc.domain.WorkingCopySupport;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: updates the working copy for a page.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyCommand {

    private final ResourceRepository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateWorkingCopyCommand(final ResourceRepository repository,
                                    final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Updates the working copy.
     *
     * @param delta The page delta to store in the page.
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final PageDelta delta)
                               throws CccCheckedException {
        final Page r = _repository.find(Page.class, resourceId);
        r.confirmLock(actor);

        r.setOrUpdateWorkingCopy(delta);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.RESOURCE_UPDATE_WC,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail()));
    }

    /**
     * Updates the working copy.
     *
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @param revisionNo The revision that the working copy will be created
     *  from.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final long revisionNo) throws CccCheckedException {
        final Resource r =
            _repository.find(Resource.class, resourceId);
        r.confirmLock(actor);

        if (r instanceof WorkingCopySupport<?, ?, ?>) {
            final WorkingCopySupport<?, ?, ?> wcAware =
                (WorkingCopySupport<?, ?, ?>) r;
            wcAware.setWorkingCopyFromRevision((int) revisionNo);

            _audit.record(
                new LogEntry(
                    actor,
                    CommandType.RESOURCE_UPDATE_WC,
                    happenedOn,
                    resourceId,
                    new JsonImpl(r).getDetail()));
        } else {
            throw new WorkingCopyNotSupportedException(r);
        }
    }
}
