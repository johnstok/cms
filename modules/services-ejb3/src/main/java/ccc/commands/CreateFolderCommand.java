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
import ccc.domain.FolderEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: create a new folder.
 *
 * @author Civic Computing Ltd.
 */
class CreateFolderCommand extends CreateResourceCommand<FolderEntity> {

    private final UUID _parentFolder;
    private final FolderEntity _folder;
    private final String _name;
    private final String _title;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the new folder will be created.
     * @param name The name of the new folder.
     * @param title The title of the new folder.
     */
    public CreateFolderCommand(final ResourceRepository repository,
                               final LogEntryRepository audit,
                               final UUID parentFolder,
                               final String name,
                               final String title) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _folder = getRepository().find(FolderEntity.class, parentFolder);
        _name = name;
        _title = title;
    }


    /** {@inheritDoc} */
    @Override
    public FolderEntity doExecute(final UserEntity actor,
                            final Date happenedOn) {
        final FolderEntity f = new FolderEntity(_name);
        f.setTitle((null==_title)?_name:_title);

        create(actor, happenedOn, _parentFolder, f);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    protected void authorize(final UserEntity actor) {
        if (!_folder.isWriteableBy(actor)) {
            throw new UnauthorizedException(_parentFolder, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FOLDER_CREATE; }
}
