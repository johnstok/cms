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
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: move a resource to another folder.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceCommand
    extends
        Command<Void> {

    private final UUID _resourceId;
    private final UUID _newParentId;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     */
    public MoveResourceCommand(final IRepositoryFactory repoFactory,
                               final UUID resourceId,
                               final UUID newParentId) {
        super(repoFactory);
        _resourceId = resourceId;
        _newParentId = newParentId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        final ResourceEntity resource =
            getRepository().find(ResourceEntity.class, _resourceId);
        resource.confirmLock(actor);

        final FolderEntity newParent =
            getRepository().find(FolderEntity.class, _newParentId);
        resource.getParent().remove(resource);
        newParent.add(resource);

        auditResourceCommand(actor, happenedOn, resource);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_MOVE; }
}
