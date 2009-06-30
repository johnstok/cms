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

import java.util.UUID;

import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileWorkingCopy
    extends
        WorkingCopy implements IFile {

    private MimeType _mimeType;
    private int _size;
    private Data _data;

    /** Constructor: for persistence only. */
    protected FileWorkingCopy() { super(); }

    /**
     * Constructor.
     *
     * @param snapshot
     */
    public FileWorkingCopy(final FileDelta snapshot) {
        delta(snapshot);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param snapshot
     */
    public void delta(final FileDelta snapshot) {
        _mimeType = snapshot.getMimeType();
        _size = snapshot.getSize();
        _data = new Data(UUID.fromString(snapshot.getData().toString()));
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public FileDelta delta() {
        return new FileDelta(
            _mimeType,
            new ID(_data.id().toString()),
            _size);
    }

    /** {@inheritDoc} */
    @Override
    public Data getData() {
        return _data;
    }

    /** {@inheritDoc} */
    @Override
    public MimeType getMimeType() {
        return _mimeType;
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return _size;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isImage() {
        // TODO: Factor into superclass.
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }
}
