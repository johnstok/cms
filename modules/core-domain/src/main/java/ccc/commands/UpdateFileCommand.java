/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.FileHelper;
import ccc.domain.LockMismatchException;
import ccc.domain.RevisionMetadata;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.FileRepository;
import ccc.persistence.Repository;
import ccc.rest.dto.FileDelta;
import ccc.types.ID;


/**
 * Command: update an existing file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileCommand extends UpdateResourceCommand {

    private final FileRepository _data;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param data The data manager to use for reading / writing the file data.
     */
    public UpdateFileCommand(final Repository repository,
                             final AuditLog audit,
                             final FileRepository data) {
        super(repository, audit);
        _data = data;
    }


    /**
     * Update a file.
     *
     * @param actor The user updating the file.
     * @param happenedOn The date that the file was updated.
     * @param fileId The id of the file to update.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     * @param fileDelta The delta describing changes to the file's metadata.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     *
     * @throws LockMismatchException If the file is locked by another user.
     * @throws UnlockedException If the file is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID fileId,
                        final FileDelta fileDelta,
                        final String comment,
                        final boolean isMajorEdit,
                        final InputStream dataStream)
       throws UnlockedException, LockMismatchException {
        final File f = getDao().find(File.class, fileId);
        f.confirmLock(actor);

        final Data d = _data.create(dataStream, fileDelta.getSize());
        fileDelta.setData(new ID(d.id().toString()));

        if ("image".equals(fileDelta.getMimeType().getPrimaryType())) {
            new FileHelper().extractImageMetadata(
                d, fileDelta.getProperties(), _data);
        }

        final RevisionMetadata rm = new RevisionMetadata(
            happenedOn,
            actor,
            isMajorEdit,
            comment == null || comment.isEmpty() ? "Updated." : comment);

        f.workingCopy(fileDelta);
        f.applySnapshot(rm);

        update(f, null, false, actor, happenedOn);
    }
}
