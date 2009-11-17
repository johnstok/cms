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
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.Duration;


/**
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCachingCommand
    extends
        Command<Void> {

    private final UUID     _resourceId;
    private final Duration _duration;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to update.
     * @param duration The new caching duration.
     */
    public UpdateCachingCommand(final ResourceRepository repository,
                                final LogEntryRepository audit,
                                final UUID resourceId,
                                final Duration duration) {
        super(repository, audit, null, null);
        _resourceId = resourceId;
        _duration = duration;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {
        final Resource r = getRepository().find(Resource.class, _resourceId);
        r.confirmLock(actor);

        r.cache(_duration);

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                r.id(),
                new JsonImpl(r).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_UPDATE_CACHE;
    }
}
