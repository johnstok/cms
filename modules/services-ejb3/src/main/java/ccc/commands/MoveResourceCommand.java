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
import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: move a resource to another folder.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public MoveResourceCommand(final ResourceRepository repository,
                               final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    public void execute(final UserEntity actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final UUID newParentId) {
        final ResourceEntity resource = _repository.find(ResourceEntity.class, resourceId);
        resource.confirmLock(actor);

        final FolderEntity newParent = _repository.find(FolderEntity.class, newParentId);
        resource.getParent().remove(resource);
        newParent.add(resource);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_MOVE,
                happenedOn,
                resourceId,
                new JsonImpl(resource).getDetail());
        _audit.record(le);
    }

}
