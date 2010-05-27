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

import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.CommandType;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.domain.WCAware;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


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
     */
    public void execute(final UserEntity actor,
                        final Date happenedOn,
                        final UUID resourceId) {
        final ResourceEntity r =
            _repository.find(ResourceEntity.class, resourceId);
        r.confirmLock(actor);

        if (r instanceof WCAware<?>) {
            final WCAware<?> wcAware = (WCAware<?>) r;
            wcAware.clearWorkingCopy();
        } else {
            throw new WorkingCopyNotSupportedException(r.getId());
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
