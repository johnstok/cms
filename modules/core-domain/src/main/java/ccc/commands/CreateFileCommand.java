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
import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.FileRepository;
import ccc.persistence.Repository;
import ccc.rest.dto.FileDelta;
import ccc.types.ResourceName;


/**
 * Command: creates a new file in CCC.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFileCommand extends CreateResourceCommand {

    private final FileRepository _data;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param data The data manager to use for reading / writing the file data.
     */
    public CreateFileCommand(final Repository repository,
                             final AuditLog audit,
                             final FileRepository data) {
        super(repository, audit);
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
     * @throws CccCheckedException If the command fails.
     *
     * @return The file that was created.
     */
    public File execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final FileDelta file,
                        final String title,
                        final String description,
                        final ResourceName name,
                        final String comment,
                        final boolean isMajorEdit,
                        final InputStream dataStream)
                                                throws CccCheckedException {
        final Data data = _data.create(dataStream, file.getSize());

        if ("image".equals(file.getMimeType().getPrimaryType())) {
            new FileHelper().extractImageMetadata(
                data, file.getProperties(), _data);
        }

        final RevisionMetadata rm =
            new RevisionMetadata(
                happenedOn,
                actor,
                isMajorEdit,
                comment == null || comment.isEmpty() ? "Created." : comment);

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

        create(actor, happenedOn, parentFolder, f);

        return f;
    }
}
