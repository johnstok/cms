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

import ccc.rest.dto.FileDelta;
import ccc.types.FilePropertyNames;
import ccc.types.MimeType;


/**
 * A working copy for file deltas.
 *
 * @author Civic Computing Ltd.
 */
public class FileWorkingCopy
    extends
        WorkingCopy<FileDelta> {

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
            _data.id(),
            _size,
            _properties);
    }

    public Data getData() {
        return _data;
    }

    public MimeType getMimeType() {
        return _mimeType;
    }

    public int getSize() {
        return _size;
    }

    public boolean isImage() {
        // TODO: Factor into superclass?
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }

    public String getCharset() {
        return _properties.get(FilePropertyNames.CHARSET);
    }

    /** {@inheritDoc} */
    public boolean isText() {
        // TODO: Factor into superclass?
        final String primary = getMimeType().getPrimaryType();
        final String sub = getMimeType().getSubType();
        if ("text".equalsIgnoreCase(primary)) {
            return true;
        } else if ("application".equalsIgnoreCase(primary)) {
            if ("xml".equalsIgnoreCase(sub)
                || "plain".equalsIgnoreCase(sub)
                || "javascript".equalsIgnoreCase(sub)
                || "x-javascript".equalsIgnoreCase(sub)) {
                return true;
            }
        }
        return false;
    }
}
