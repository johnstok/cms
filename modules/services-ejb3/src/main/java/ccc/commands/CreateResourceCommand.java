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

import ccc.domain.FolderEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


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
    protected void create(final UserEntity actor,
                          final Date happenedOn,
                          final UUID folderId,
                          final ResourceEntity newResource) {
        newResource.setDateCreated(happenedOn, actor);
        newResource.setDateChanged(happenedOn, actor);

        final FolderEntity folder =
            getRepository().find(FolderEntity.class, folderId);
        if (null==folder) {
            throw new RuntimeException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        getRepository().create(newResource);

        auditResourceCommand(actor, happenedOn, newResource);
    }
}
