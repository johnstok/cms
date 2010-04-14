/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.api.http;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import ccc.api.dto.ResourceSummary;


/**
 * API for uploading files.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated // FIXME: Use the standard API.
public interface IFileUploader {


    /**
     * Upload a file.
     *
     * @param parentId The folder in which the file should be uploaded.
     * @param fileName The name of the file.
     * @param originalTitle The title of the file.
     * @param originalDescription The file's description.
     * @param originalLastUpdate The file's last update date.
     * @param file The local file reference.
     * @param publish Should the file be published.
     */
    void uploadFile(final UUID parentId,
                    final String fileName,
                    final String originalTitle,
                    final String originalDescription,
                    final Date originalLastUpdate,
                    final File file,
                    final boolean publish);


    /**
     * Upload a file.
     *
     * @param parentId The folder in which the file should be uploaded.
     * @param fileName The name of the file.
     * @param title The title of the file.
     * @param lastUpdate The last update of the file.
     * @param description The file's description.
     * @param directory The directory that the local file is stored.
     */
    void uploadFile(final UUID parentId,
                    final String fileName,
                    final String title,
                    final String description,
                    final Date lastUpdate,
                    final String directory);


    /**
     * Update an existing file to be a text file with the specified text.
     *
     * @param fText The text for the file.
     * @param rs The summary for the file to update.
     *
     * @throws IOException If the operation fails sending the data.
     *
     * @return A summary of the updated resource.
     */
    ResourceSummary updateTextFile(final String fText,
                                   final ResourceSummary rs) throws IOException;


    /**
     * Create a text file on the server.
     *
     * @param fName The file's name.
     * @param fText The file's contents.
     * @param filesFolder The parent folder for the file.
     *
     * @return The summary of the newly created file.
     *
     * @throws IOException If creation fails on the client.
     */
    ResourceSummary createFile(final String fName,
                               final String fText,
                               final ResourceSummary filesFolder)
                                                            throws IOException;
}