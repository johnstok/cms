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

import java.util.Map;
import java.util.UUID;

import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.MimeType;
import ccc.entities.IFile;


/**
 * A working copy for file deltas.
 *
 * @author Civic Computing Ltd.
 */
public class FileWorkingCopy
    extends
        WorkingCopy<FileDelta> implements IFile {

    private MimeType _mimeType;
    private int _size;
    private Data _data;
    private Map<String, String> _properties;

    /** Constructor: for persistence only. */
    protected FileWorkingCopy() { super(); }

    /**
     * Constructor.
     *
     * @param delta The delta describing this working copy's state.
     */
    public FileWorkingCopy(final FileDelta delta) {
        delta(delta);
    }

    /** {@inheritDoc} */
    @Override
    public void delta(final FileDelta snapshot) {
        _mimeType = snapshot.getMimeType();
        _size = snapshot.getSize();
        _data = new Data(UUID.fromString(snapshot.getData().toString()));
        _properties = snapshot.getProperties();
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta delta() {
        return new FileDelta(
            _mimeType,
            new ID(_data.id().toString()),
            _size,
            _properties);
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
        // TODO: Factor into superclass?
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }
}
