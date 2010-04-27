/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.core.FileDto;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;


/**
 * A working copy for file deltas.
 *
 * @author Civic Computing Ltd.
 */
public class FileWorkingCopy
    extends
        WorkingCopy<FileDto> {

    private MimeType _mimeType;
    private long _size;
    private Data _data;
    private Map<String, String> _properties;


    /** Constructor: for persistence only. */
    protected FileWorkingCopy() { super(); }


    /**
     * Constructor.
     *
     * @param delta The delta describing this working copy's state.
     */
    public FileWorkingCopy(final FileDto delta) {
        delta(delta);
    }


    /** {@inheritDoc} */
    @Override
    public void delta(final FileDto snapshot) {
        _mimeType = snapshot.getMimeType();
        _size = snapshot.getSize();
        _data = new Data(UUID.fromString(snapshot.getData().toString()));
        _properties = snapshot.getProperties();
    }


    /** {@inheritDoc} */
    @Override
    public FileDto delta() {
        return new FileDto(
            _mimeType,
            _data.getId(),
            _size,
            _properties);
    }


    /**
     * Accessor.
     *
     * @return The working copy's data reference.
     */
    public Data getData() {
        return _data;
    }


    /**
     * Accessor.
     *
     * @return The working copy's mime type.
     */
    public MimeType getMimeType() {
        return _mimeType;
    }


    /**
     * Accessor.
     *
     * @return The working copy's size, in bytes.
     */
    public long getSize() {
        return _size;
    }


    /**
     * Accessor.
     *
     * @return True if the working copy is an image; false otherwise.
     */
    public boolean isImage() {
        // TODO: Factor into superclass.
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }


    /**
     * Accessor.
     *
     * @return The working copy's character set.
     */
    public String getCharset() {
        return _properties.get(FilePropertyNames.CHARSET);
    }


    /** {@inheritDoc} */
    public boolean isText() {
        // TODO: Factor into superclass.
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
