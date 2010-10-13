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

import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: unlocks a resource.
 * If the logged in user does not have privileges to unlock this resource a
 * CCCException will be thrown.
 * Unlocking an unlocked resource has no effect.
 *
 * @author Civic Computing Ltd.
 */
class UnlockResourceCommand extends Command<Void> {

    private final UUID _resourceId;
    private final ResourceEntity _r;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to unlock.
     */
    public UnlockResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final UUID resourceId) {
        super(repository, audit, null, null);
        DBC.require().notNull(resourceId);
        _resourceId = resourceId;
        _r = getRepository().find(ResourceEntity.class, _resourceId);
    }

    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor,
                             final Date happenedOn) {
        _r.unlock(actor);

        auditResourceCommand(actor, happenedOn, _r);

        return null;
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_r.isWriteableBy(actor)) {
            throw new UnauthorizedException(_resourceId, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_UNLOCK; }
}
