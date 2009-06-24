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
package ccc.domain;

import java.util.Date;

import ccc.api.DBC;
import ccc.api.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileRevision
    extends
        Revision {

    private Data      _data;
    private int       _size;
    private MimeType  _mimeType;

    /** Constructor: for persistence only. */
    protected FileRevision() { super(); }

    /**
     * Constructor.
     *
     * @param index
     * @param majorChange
     * @param comment
     * @param data
     * @param size
     * @param mimeType
     */
    FileRevision(final int index,
                 final Date timestamp,
                 final User actor,
                 final boolean majorChange,
                 final String comment,
                 final Data data,
                 final int size,
                 final MimeType mimeType) {
        super(index, timestamp, actor, majorChange, comment);
        DBC.require().notNull(data);
        DBC.require().notNull(mimeType);
        _data = data;
        _size = size;
        _mimeType = mimeType; // TODO: Make defensive copy?
    }


    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    public final Data getData() {
        return _data;
    }


    /**
     * Accessor.
     *
     * @return Returns the size.
     */
    public final int getSize() {
        return _size;
    }


    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public final MimeType getMimeType() {
        return _mimeType;
    }

    /**
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }
}
