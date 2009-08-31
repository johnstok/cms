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
import java.util.Map;
import java.util.UUID;

import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.JsonImpl;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Repository;
import ccc.types.CommandType;


/**
 * Command: update resource metadata.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceMetadataCommand {

    private final Repository      _repository;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateResourceMetadataCommand(final Repository repository,
                                              final AuditLog audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Update metadata of the resource.
     *
     * @param id The resource to update.
     * @param metadata The new metadata to set.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final String title,
                        final String description,
                        final String tags,
                        final Map<String, String> metadata)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _repository.find(Resource.class, id);
        r.confirmLock(actor);

        r.title(title);
        r.description(description);
        r.tags(tags);

        r.clearMetadata();
        for (final Map.Entry<String, String> metadatum: metadata.entrySet()) {
            r.addMetadatum(metadatum.getKey(), metadatum.getValue());
        }

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_UPDATE_METADATA,
                happenedOn,
                id,
                new JsonImpl(r).getDetail());
        _audit.record(le);
    }

}
