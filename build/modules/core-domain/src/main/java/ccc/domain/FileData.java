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

import ccc.commons.DBC;



/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class FileData extends Entity {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 4924963689426238574L;

    private byte[] _data = null;

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
    public FileData(final byte[] data) {
        super();
        DBC.require().notNull(data);
        _data = data;
    }

    /**
     * Accessor for the file's data.
     *
     * @return The description as a byte array.
     */
    public byte[] data() {
        return _data;
    }
}
