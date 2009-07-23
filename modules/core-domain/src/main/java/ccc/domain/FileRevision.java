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
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileRevision
    extends
        Revision<FileDelta>
    implements
        IFile {

    private Data      _data;
    private int       _size;
    private MimeType  _mimeType;

    /** Constructor: for persistence only. */
    protected FileRevision() { super(); }

    /**
     * Constructor.
     *
     * @param majorChange
     * @param comment
     * @param data
     * @param size
     * @param mimeType
     */
    FileRevision(final Date timestamp,
                 final User actor,
                 final boolean majorChange,
                 final String comment,
                 final Data data,
                 final int size,
                 final MimeType mimeType) {
        super(timestamp, actor, majorChange, comment);
        DBC.require().notNull(data);
        DBC.require().notNull(mimeType);
        _data = data;
        _size = size;
        _mimeType = mimeType; // TODO: Make defensive copy?
    }


    /** {@inheritDoc} */
    public final Data getData() {
        return _data;
    }


    /** {@inheritDoc} */
    public final int getSize() {
        return _size;
    }


    /** {@inheritDoc} */
    public final MimeType getMimeType() {
        return _mimeType;
    }

    /** {@inheritDoc} */
    public boolean isImage() {
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }

    /** {@inheritDoc} */
    public boolean isText() {
        return "text".equalsIgnoreCase(getMimeType().getPrimaryType());
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta delta() {
        return new FileDelta(
            _mimeType,
            new ID(_data.id().toString()),
            _size);
    }
}
