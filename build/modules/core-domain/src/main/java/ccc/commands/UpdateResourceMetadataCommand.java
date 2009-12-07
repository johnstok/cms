/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: update resource metadata.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceMetadataCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateResourceMetadataCommand(final ResourceRepository repository,
                                         final LogEntryRepository audit) {
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
     * @param title The new title for the resource.
     * @param description The new description for the resource.
     * @param tags The new tags for the resource.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final String title,
                        final String description,
                        final String tags,
                        final Map<String, String> metadata)
                               throws CccCheckedException {
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
