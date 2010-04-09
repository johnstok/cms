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

import ccc.api.types.CommandType;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


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
     * @param repoFactory The repository factory for this command.
     */
    public UpdateResourceMetadataCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createResourceRepository();
        _audit = repoFactory.createLogEntryRepo();
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
                        final Map<String, String> metadata) {
        final Resource r = _repository.find(Resource.class, id);
        r.confirmLock(actor);

        r.setTitle(title);
        r.setDescription(description);
        r.setTags(tags);

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
