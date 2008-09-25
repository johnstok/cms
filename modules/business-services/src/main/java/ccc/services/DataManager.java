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
import java.util.UUID;

import ccc.domain.Data;
import ccc.domain.File;


/**
 * API definition for data management.
 * TODO: Should have package scope.
 *
 * @author Civic Computing Ltd.
 */
interface DataManager {

    /**
     * Create a new item of binary data.
     *
     * @param data The identifier for the new data.
     * @param dataStream The input stream from which the bytes for the new data
     *        item should be read.
     */
    void create(Data data, InputStream dataStream);

    /**
     * Retrieve an existing item of binary data and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param dataStream The output stream to which the data should be written.
     */
    void retrieve(Data data, OutputStream dataStream);


    /**
     * Create a file.
     *
     * @param file The File to persists.
     * @param path The unique id of the folder acting as a parent for file.
     * @param dataStream The input stream from which the bytes for the new file
     *        should be read.
     */
    void createFile(File file, final UUID path, InputStream dataStream);
}
