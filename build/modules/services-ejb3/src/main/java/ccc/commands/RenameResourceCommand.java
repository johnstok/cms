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

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import ccc.api.exceptions.ResourceExistsException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.messaging.Producer;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceCommand
    extends
        Command<Void> {

    private final UUID   _resourceId;
    private final String _name;
    private final ResourceEntity _resource;
    private final Producer       _producer;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to rename.
     * @param name The new name for the resource.
     */
    public RenameResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final Producer producer,
                                 final UUID resourceId,
                                 final String name) {
        super(repository, audit, null, null);
        _producer  = producer;
        _resourceId = resourceId;
        _name = name;
        _resource = getRepository().find(ResourceEntity.class, _resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        _resource.confirmLock(actor);

        final ResourceName newName = new ResourceName(_name);
        final ResourceEntity existingResource =
            _resource.getParent().getEntryWithName(newName);
        if (null!=existingResource) {
            throw new ResourceExistsException(
                existingResource.getId(), existingResource.getName());
        }

        _resource.setName(new ResourceName(_name));

        auditResourceCommand(actor, happenedOn, _resource);

        _producer.broadcastMessage(
            CommandType.SEARCH_INDEX_RESOURCE,
            Collections.singletonMap("resource", _resource.getId().toString()));

        return null;
    }

    @Override
    protected void authorize(final UserEntity actor) {
        if (!_resource.isWriteableBy(actor)) {
            throw new UnauthorizedException(_resourceId, actor.getId());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_RENAME; }
}
