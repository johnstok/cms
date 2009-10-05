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
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.FileHelper;
import ccc.domain.RevisionMetadata;
import ccc.persistence.DataRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.FileDelta;
import ccc.types.ResourceName;


/**
 * Command: creates a new file in CCC.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFileCommand extends CreateResourceCommand {

    private final DataRepository _data;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param data The data manager to use for reading / writing the file data.
     */
    public CreateFileCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final DataRepository data) {
        super(repository, audit);
        _data = data;
    }


    /**
     * Create the file.
     *
     * @param file The File to persist.
     * @param parentFolder The unique id of the folder acting as a parent for
     *  file.
     * @param dataStream The input stream from which the bytes for the new file
     *  should be read.
     * @param name The name of the file to create.
     * @param title The file's title.
     * @param description The description of the file.
     * @param rm Metadata describing the revision.
     *
     * @throws CccCheckedException If the command fails.
     *
     * @return The file that was created.
     */
    public File execute(final UUID parentFolder,
                        final FileDelta file,
                        final String title,
                        final String description,
                        final ResourceName name,
                        final RevisionMetadata rm,
                        final InputStream dataStream)
                                                throws CccCheckedException {
        final Data data = _data.create(dataStream, file.getSize());

        if ("image".equals(file.getMimeType().getPrimaryType())) {
            new FileHelper().extractImageMetadata(
                data, file.getProperties(), _data);
        }

        final File f =
            new File(
                name,
                title,
                description,
                data,
                file.getSize(),
                file.getMimeType(),
                file.getProperties(),
                rm);

        create(rm.getActor(), rm.getTimestamp(), parentFolder, f);

        return f;
    }
}
