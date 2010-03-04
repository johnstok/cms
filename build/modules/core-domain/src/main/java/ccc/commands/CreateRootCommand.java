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

import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;


/**
 * Command: create a root folder.
 *
 * @author Civic Computing Ltd.
 */
class CreateRootCommand extends CreateResourceCommand<Void> {

    private final Folder _folder;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param folder The root folder to create.
     */
    public CreateRootCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final Folder folder) {
        super(repository, audit);
        _folder = folder; // TODO: Should create the folder in doExecute().
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {
        try {
            final Resource possibleRoot =
                getRepository().root(_folder.getName().toString());
            throw new ResourceExistsException(null, possibleRoot);

        } catch (final EntityNotFoundException e) {
            _folder.setDateCreated(happenedOn, actor);
            _folder.setDateChanged(happenedOn, actor);
            getRepository().create(_folder);

            audit(_folder, actor, happenedOn);

            return null;
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FOLDER_CREATE; }
}
