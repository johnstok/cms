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
package ccc.services;

import java.io.InputStream;
import java.util.Date;

import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.ResourceSummary;
import ccc.commands.CommandFailedException;


/**
 * Local CCC commands.
 *
 * @author Civic Computing Ltd.
 */
public interface LocalCommands extends Commands {

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
     * @throws CommandFailedException If an error occurs creating the file.
     */
    ResourceSummary createFile(ID parentFolder,
                               FileDelta file,
                               String resourceName,
                               InputStream dataStream,
                               String title,
                               String description,
                               Date lastUpdated,
                               boolean publish,
                               String comment,
                               boolean isMajorEdit) throws CommandFailedException;

    /**
     * Update an existing CCC file.
     *
     * @param fileId The id of the file to update.
     * @param fileDelta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     * @param dataStream The new content for the file.
     *
     * @throws CommandFailedException If an error occurs updating the file.
     */
    void updateFile(ID fileId,
                    FileDelta fileDelta,
                    String comment,
                    boolean isMajorEdit,
                    InputStream dataStream) throws CommandFailedException;
}
