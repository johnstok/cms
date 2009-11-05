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

package ccc.persistence;

import java.io.InputStream;
import java.io.OutputStream;

import ccc.domain.Data;
import ccc.entities.IData;


/**
 * Repository for file entities.
 *
 * @author Civic Computing Ltd.
 */
public interface DataRepository {

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
    void retrieve(IData data, OutputStream dataStream);

    /**
     * Retrieve an existing item of binary data and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param action An action to perform with the retrieved data.
     */
    void retrieve(Data data, StreamAction action);

    /**
     * Retrieve an existing image, create a thumbnail and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param os The output stream to which the data should be written.
     * @param maxDimension The maximum dimension of the thumbnail.
     */
    void retrieveThumb(IData data, OutputStream os, int maxDimension);
}
