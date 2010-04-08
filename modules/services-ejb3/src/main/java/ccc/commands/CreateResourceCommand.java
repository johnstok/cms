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

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Abstract superclass for commands that create resources.
 *
 * @param <T> The result type of the command.
 *
 * @author Civic Computing Ltd.
 */
abstract class CreateResourceCommand<T>
    extends
        Command<T> {

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit) {
        super(repository, audit, null, null);
    }

    /**
     * Create a resource in the specified folder.
     *
     * @param actor The user performing the action.
     * @param happenedOn The date the command was executed.
     * @param folderId The folder in which the resource will be created.
     * @param newResource The new resource.
     */
    protected void create(final User actor,
                          final Date happenedOn,
                          final UUID folderId,
                          final Resource newResource) {
        newResource.setDateCreated(happenedOn, actor);
        newResource.setDateChanged(happenedOn, actor);

        final Folder folder = getRepository().find(Folder.class, folderId);
        if (null==folder) {
            throw new RuntimeException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        getRepository().create(newResource);

        audit(newResource, actor, happenedOn);
    }


    /**
     * Audit the creation of a resource.
     *
     * @param resource The newly created resource.
     * @param actor The actor performing the command.
     * @param happenedOn When the command was performed.
     */
    protected void audit(final Resource resource,
                         final User actor,
                         final Date happenedOn) {

        final JsonImpl ss = new JsonImpl(resource);

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
