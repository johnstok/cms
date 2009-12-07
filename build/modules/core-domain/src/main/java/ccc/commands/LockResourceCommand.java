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
 * Command: locks a resource.
 * <p>The resource will be locked by the currently logged in user.
 * If the resource is already locked an will be thrown.
 *
 * @author Civic Computing Ltd.
 */
class LockResourceCommand extends Command<Void> {

    private final UUID _resourceId;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to lock.
     */
    public LockResourceCommand(final ResourceRepository repository,
                               final LogEntryRepository audit,
                               final UUID resourceId) {
        super(repository, audit, null, null);
        DBC.require().notNull(resourceId);
        _resourceId = resourceId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final User actor,
                             final Date happenedOn) throws CccCheckedException {
        final Resource r = getRepository().find(Resource.class, _resourceId);
        r.lock(actor);

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _resourceId,
                new JsonImpl(r).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_LOCK; }
}
