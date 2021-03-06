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

import ccc.api.exceptions.ResourceExistsException;
import ccc.api.types.CommandType;
import ccc.domain.FolderEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: create a root folder.
 *
 * @author Civic Computing Ltd.
 */
class CreateRootCommand extends Command<Void> {

    private final FolderEntity _folder;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param folder The root folder to create.
     */
    public CreateRootCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final FolderEntity folder) {
        super(repository, audit, null, null);
        _folder = folder; // TODO: Should create the folder in doExecute().
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {
        final ResourceEntity possibleRoot =
            getRepository().root(_folder.getName().toString());
        if (possibleRoot != null) {
            throw new ResourceExistsException(
            possibleRoot.getId(), possibleRoot.getName());
        }
        _folder.setDateCreated(happenedOn, actor);
        _folder.setDateChanged(happenedOn, actor);
        getRepository().create(_folder);

        auditResourceCommand(actor, happenedOn, _folder);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FOLDER_CREATE; }
}
