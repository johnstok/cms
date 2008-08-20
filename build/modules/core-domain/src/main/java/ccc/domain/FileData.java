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

import java.sql.Blob;
import java.sql.SQLException;

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
     */
    @SuppressWarnings("unused")
    private FileData() {
        super();
    }

    /**
     * Constructor.
     *
     * @param data Data of the file.
     */
    public FileData(final Blob data) {
        super();
        DBC.require().notNull(data);
        try {
            if (data.length() > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException(
                        "Data size must be under "+MAX_FILE_SIZE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        _data = data;
    }

    /**
     * Accessor for the file's data.
     *
     * @return The description as a byte array.
     */
    public Blob data() {
        return _data;
    }
}
