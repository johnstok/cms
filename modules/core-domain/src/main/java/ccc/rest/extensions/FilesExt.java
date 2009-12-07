/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.extensions;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import ccc.persistence.StreamAction;
import ccc.rest.Files;
import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.ResourceSummary;


/**
 * Additional API methods for operating on files.
 *
 * @author Civic Computing Ltd.
 */
public interface FilesExt
    extends
        Files {

    /**
     * Create a new CCC file.
     *
     * @param parentFolder The folder in which the file should be created.
     * @param file The details of the file.
     * @param resourceName The name of the file.
     * @param dataStream The content of the file.
     * @param title The title of the file.
     * @param description The description of the file.
     * @param lastUpdated The last updated date of the file.
     * @param publish Should the file be published.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     *
     * @return A summary of the newly created file.
     *
     * @throws RestException If an error occurs creating the file.
     */
    ResourceSummary createFile(UUID parentFolder,
                               FileDelta file,
                               String resourceName,
                               InputStream dataStream,
                               String title,
                               String description,
                               Date lastUpdated,
                               boolean publish,
                               String comment,
                               boolean isMajorEdit) throws RestException;


    /**
     * Update an existing CCC file.
     *
     * @param fileId The id of the file to update.
     * @param fileDelta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     * @param dataStream The new content for the file.
     *
     * @throws RestException If an error occurs updating the file.
     */
    void updateFile(UUID fileId,
                    FileDelta fileDelta,
                    String comment,
                    boolean isMajorEdit,
                    InputStream dataStream) throws RestException;


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     *
     * @throws RestException If an error occurs retrieving the file.
     * @throws UnauthorizedException If the file is not accessible to the
     *  current user.
     */
    void retrieve(UUID file, StreamAction action)
    throws RestException, UnauthorizedException;


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param action The action to perform.
     *
     * @throws RestException If an error occurs retrieving the file.
     * @throws UnauthorizedException If the file is not accessible to the
     *  current user.
     */
    void retrieveWorkingCopy(UUID file, StreamAction action)
    throws RestException, UnauthorizedException;


    /**
     * Write the contents of a file to an output stream.
     *
     * @param file The file's ID.
     * @param revision The file revision to retrieve.
     * @param action The action to perform.
     *
     * @throws RestException If an error occurs retrieving the file.
     * @throws UnauthorizedException If the file is not accessible to the
     *  current user.
     */
    void retrieveRevision(UUID file, int revision, StreamAction action)
    throws RestException, UnauthorizedException;

}
