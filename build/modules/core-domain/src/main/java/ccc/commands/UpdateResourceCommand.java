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

import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.DataRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;


/**
 * Abstract superclass for commands that update resources.
 *
 * @param <T> The result type of the command.
 *
 * @author Civic Computing Ltd.
 */
abstract class UpdateResourceCommand<T>
    extends
        Command<T> {

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param data The data repository for storing binary data.
     */
    public UpdateResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final DataRepository data) {
        super(repository, audit, null, data);
    }


    /**
     * Record that a resource has been updated (generates a log entry).
     *
     * @param resource The resource that was updated.
     * @param actor The actor who performed the update.
     * @param happenedOn The date the update took place.
     */
    protected void update(final Resource resource,
                          final User actor,
                          final Date happenedOn) {
        resource.setDateChanged(happenedOn, actor);
        audit(resource, happenedOn, actor);
    }


    private void audit(final Resource resource,
                       final Date happenedOn,
                       final User actor) {

        final JsonImpl ss = new JsonImpl(resource);

        // TODO Resolve type for RESOURCE_APPLY_WC commands?

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                resource.getId(),
                ss.getDetail());
        getAudit().record(le);
    }
}
