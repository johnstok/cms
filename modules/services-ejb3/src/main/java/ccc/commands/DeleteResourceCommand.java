/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.Date;
import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: delete a resource.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteResourceCommand extends Command<Void> {

    private final UUID _resourceId;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The ID of the resource to delete.
     */
    public DeleteResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final UUID resourceId) {
        super(repository, audit, null, null);
        _resourceId = resourceId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor,
                             final Date happenedOn) {
        final ResourceEntity resource =
            getRepository().find(ResourceEntity.class, _resourceId);
        resource.confirmLock(actor);

        if (resource.isDeleted()) {
            throw new RuntimeException("Can't delete a deleted resource.");
        }

        final FolderEntity trash =
            getRepository().root(PredefinedResourceNames.TRASH);

        resource.delete();
        resource.setName(new ResourceName(resource.getId().toString()));
        resource.getParent().remove(resource);
        trash.add(resource);
        resource.unlock(actor);

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _resourceId,
                new JsonImpl(resource).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_DELETE; }


    /**
     * Accessor.
     *
     * @return Returns the resourceId.
     */
    public UUID getResourceId() { return _resourceId; }
}
