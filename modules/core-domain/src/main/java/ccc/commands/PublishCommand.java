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
import ccc.types.DBC;


/**
 * Command: publish a resource.
 *
 * @author Civic Computing Ltd.
 */
class PublishCommand extends Command<Void> {

    private final UUID _resourceId;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to publish.
     */
    public PublishCommand(final ResourceRepository repository,
                          final LogEntryRepository audit,
                          final UUID resourceId) {
        super(repository, audit);
        DBC.require().notNull(resourceId);
        _resourceId = resourceId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final User publishedBy,
                             final Date happenedOn) throws CccCheckedException {

        final Resource r = getRepository().find(Resource.class, _resourceId);
        r.confirmLock(publishedBy);

        r.publish(publishedBy);
        r.dateChanged(happenedOn);

        final LogEntry le =
            new LogEntry(
                publishedBy,
                getType(),
                happenedOn,
                r.id(),
                new JsonImpl(r).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_PUBLISH; }
}
