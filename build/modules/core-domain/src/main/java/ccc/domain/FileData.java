/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.lob.BlobImpl;

import ccc.commons.DBC;



/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class FileData extends Entity {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 4924963689426238574L;
    private Blob _data = null;

    /** MAX_FILE_SIZE : int. */
    public static final int MAX_FILE_SIZE = 32*1024*1024;
    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     * @param dataSize
     * @param dataStream
     */
    @SuppressWarnings("unused")
    private FileData() {
        super();
    }

    /**
     * Constructor.
     *
     * @param dataStream The data as an InputStream.
     * @param dataSize The size of the data.
     */
    public FileData(final InputStream dataStream, final int dataSize) {
        super();
        DBC.require().notNull(dataStream);

        _data = new BlobImpl(dataStream, dataSize);
        try {
            if (_data.length() > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException(
                        "Data size must be under "+MAX_FILE_SIZE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Accessor for the file's data.
     *
     * @return The data as a blob.
     */
    public Blob data() {
        return _data;
    }
}
