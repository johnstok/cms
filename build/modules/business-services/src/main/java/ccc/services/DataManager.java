/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.activation.MimeType;

import ccc.domain.Data;
import ccc.domain.File;


/**
 * API definition for data management.
 *
 * @author Civic Computing Ltd.
 */
public interface DataManager {

    /** NAME : String. */
    String NAME = "DataManager";

    /**
     * Create a new item of binary data.
     *
     * @param dataStream The input stream from which the bytes for the new data
     *        item should be read.
     * @param length The length of the input, in bytes.
     * @return An instance of {@link Data} that represents the contents of the
     *      stream.
     */
    Data create(InputStream dataStream, final int length);

    /**
     * Retrieve an existing item of binary data and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param dataStream The output stream to which the data should be written.
     */
    void retrieve(Data data, OutputStream dataStream);

    /**
     * Retrieve an existing item of binary data and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param action An action to perform with the retrieved data.
     */
    void retrieve(Data data, StreamAction action);

    /**
     * Create a file.
     *
     * @param file The File to persist.
     * @param parentId The unique id of the folder acting as a parent for file.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     */
    void createFile(File file, final UUID parentId, InputStream dataStream);

    /**
     * Update a file.
     *
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     * @param fileId The uuid of the file to update.
     * @param title The new title for the file.
     * @param description The new description for the file.
     * @param mimeType The mime type of the new file.
     * @param size The size of the new file.
     */
    void updateFile(UUID fileId,
                    String title,
                    String description,
                    MimeType mimeType,
                    long size,
                    InputStream dataStream);

    /**
     * Returns a list of all images.
     *
     * @return The list of resources.
     */
    List<File> findImages();


    /**
     * An action to perform on an {@link InputStream}.
     *
     * @author Civic Computing Ltd.
     */
    public interface StreamAction {
        /**
         * Execute the action.
         *
         * @param is The input stream to operate on.
         * @throws Exception If the action fails.
         */
        void execute(InputStream is) throws Exception;
    }
}
