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

import ccc.api.FileDelta;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;


/**
 * Command: creates a new file in CCC.
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
     * @param data The data manager to use for reading / writing the file data.
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
     * @param actor The user creating the file.
     * @param happenedOn The date that the file was created.
     * @param file The File to persist.
     * @param parentFolder The unique id of the folder acting as a parent for
     *  file.
     * @param dataStream The input stream from which the bytes for the new file
     *  should be read.
     * @param name The name of the file to create.
     *
     * @throws ResourceExistsException If a resource with the specified name
     *  already exists.
     *
     * @return The file that was created.
     */
    public File execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final FileDelta file,
                        final ResourceName name,
                        final InputStream dataStream)
                                                throws ResourceExistsException {
        final Data data = _data.create(dataStream, file.getSize());
        final File f =
            new File(
                name,
                file.getTitle(),
                file.getDescription(),
                data,
                file.getSize(),
                file.getMimeType());

        if (f.isImage()) {
            new FileHelper().extractImageMetadata(f, _data);
        }

        create(actor, happenedOn, parentFolder, f);

        return f;
    }
}