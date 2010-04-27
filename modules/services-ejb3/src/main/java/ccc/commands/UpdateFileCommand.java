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

import ccc.api.core.FileDto;
import ccc.api.types.CommandType;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.FileHelper;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: update an existing file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileCommand
    extends
        UpdateResourceCommand<File> {

    private final UUID        _fileId;
    private final FileDto   _fileDelta;
    private final String      _comment;
    private final boolean     _isMajorEdit;
    private final InputStream _dataStream;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param fileId The id of the file to update.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     * @param fileDelta The delta describing changes to the file's metadata.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     */
    public UpdateFileCommand(final IRepositoryFactory repoFactory,
                             final UUID fileId,
                             final FileDto fileDelta,
                             final String comment,
                             final boolean isMajorEdit,
                             final InputStream dataStream) {
        super(repoFactory);
        _fileId = fileId;
        _fileDelta = fileDelta;
        _comment = comment;
        _isMajorEdit = isMajorEdit;
        _dataStream = dataStream;
    }


    /** {@inheritDoc} */
    @Override
    public File doExecute(final User actor,
                          final Date happenedOn) {

        final File f = getRepository().find(File.class, _fileId);
        f.confirmLock(actor);

        final Data d = getData().create(_dataStream, _fileDelta.getSize());
        _fileDelta.setData(d.getId());

        if ("image".equals(_fileDelta.getMimeType().getPrimaryType())) {
            new FileHelper().extractImageMetadata(
                d, _fileDelta.getProperties(), getData());
        }

        final RevisionMetadata rm = new RevisionMetadata(
            happenedOn,
            actor,
            _isMajorEdit,
            _comment == null || _comment.isEmpty() ? "Updated." : _comment);

        f.setOrUpdateWorkingCopy(_fileDelta);
        f.applyWorkingCopy(rm);

        update(f, actor, happenedOn);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FILE_UPDATE; }
}
