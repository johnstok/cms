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

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
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
public class CreateFileCommand extends CreateResourceCommand {

    private final DataManager _data;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateFileCommand(final Dao dao,
                             final AuditLog audit,
                             final DataManager data) {
        super(dao, audit);
        _data = data;
    }

    /**
     * Create the file.
     *
     * @param file The File to persist.
     * @param parentFolder The unique id of the folder acting as a parent for
     *  file.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     * @throws MimeTypeParseException
     * @throws ResourceExistsException
     */
    public File execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final FileDelta file,
                        final ResourceName name,
                        final InputStream dataStream)
                        throws MimeTypeParseException, ResourceExistsException {
        final Data data = _data.create(dataStream, file.getSize());
        final File f =
            new File(
                name,
                file.getTitle(),
                file.getDescription(),
                data,
                file.getSize(),
                new MimeType(file.getMimeType()));

        create(actor, happenedOn, parentFolder, f);

        return f;
    }
}
