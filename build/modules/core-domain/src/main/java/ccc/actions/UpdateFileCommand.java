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
package ccc.actions;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import ccc.domain.File;
import ccc.domain.LockMismatchException;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.api.FileDelta;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileCommand extends UpdateResourceCommand {

    private final DataManager _data;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateFileCommand(final Dao dao,
                             final AuditLog audit,
                             final DataManager data) {
        super(dao, audit);
        _data = data;
    }


    /**
     * Update a file.
     *
     * @param actor
     * @param happenedOn
     * @param fileId The id of the file to update.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     * @param fileDelta The delta describing changes to the file's metadata.
     * @throws MimeTypeParseException
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID fileId,
                        final FileDelta fileDelta,
                        final InputStream dataStream)
       throws MimeTypeParseException, UnlockedException, LockMismatchException {
        final File f = _dao.find(File.class, fileId);
        f.confirmLock(actor);

         f.title(fileDelta.getTitle());
         f.description(fileDelta.getDescription());
         f.mimeType(new MimeType(fileDelta.getMimeType()));
         f.size(fileDelta.getSize());
         f.data(_data.create(dataStream, fileDelta.getSize()));

        update(f, null, false, actor, happenedOn);
    }
}
