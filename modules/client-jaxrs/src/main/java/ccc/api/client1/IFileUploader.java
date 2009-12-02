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

package ccc.api.client1;

import java.io.File;
import java.util.Date;
import java.util.UUID;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
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

}