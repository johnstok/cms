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

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.domain.Data;
import ccc.domain.FileEntity;
import ccc.domain.FileHelper;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;
import ccc.persistence.DataRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: creates a new file in CCC.
 *
 * @author Civic Computing Ltd.
 */
class CreateFileCommand extends CreateResourceCommand<FileEntity> {

    private final DataRepository _data;
    private final UUID _parentFolder;
    private final File _file;
    private final String _title;
    private final String _description;
    private final ResourceName _name;
    private final RevisionMetadata _rm;
    private final InputStream _dataStream;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param data The data manager to use for reading / writing the file data.
     * @param file The File to persist.
     * @param parentFolder The unique id of the folder acting as a parent for
     *  file.
     * @param dataStream The input stream from which the bytes for the new file
     *  should be read.
     * @param name The name of the file to create.
     * @param title The file's title.
     * @param description The description of the file.
     * @param rm Metadata describing the revision.
     */
    public CreateFileCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final DataRepository data,
                             final UUID parentFolder,
                             final File file,
                             final String title,
                             final String description,
                             final ResourceName name,
                             final RevisionMetadata rm,
                             final InputStream dataStream) {
        super(repository, audit);
        _data = data;
        _parentFolder = parentFolder;
        _file = file;
        _title = title;
        _description = description;
        _name = name;
        _rm = rm;
        _dataStream = dataStream;
    }


    /** {@inheritDoc} */
    @Override
    public FileEntity doExecute(final UserEntity actor,
                          final Date happenedOn) {
        final Data data = _data.create(_dataStream, _file.getSize());

        if ("image".equals(_file.getMimeType().getPrimaryType())) {
            new FileHelper().extractImageMetadata(
                data, _file.getProperties(), _data);
        }

        final FileEntity f =
            new FileEntity(
                _name,
                _title,
                _description,
                data,
                _file.getSize(),
                _file.getMimeType(),
                _file.getProperties(),
                _rm);

        create(_rm.getActor(), _rm.getTimestamp(), _parentFolder, f);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FILE_CREATE; }
}
