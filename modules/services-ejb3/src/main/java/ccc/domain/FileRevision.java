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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.api.core.FileDto;
import ccc.api.types.DBC;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;


/**
 * The contents of a file resource at a specified point in time.
 *
 * @author Civic Computing Ltd.
 */
public class FileRevision
    extends
        RevisionEntity<FileDto> {

    private Data      _data;
    private long       _size;
    private MimeType  _mimeType;
    private Map<String, String> _properties;


    /** Constructor: for persistence only. */
    protected FileRevision() { super(); }


    /**
     * Constructor.
     *
     * @param majorChange Is this revision a major change.
     * @param comment Comment describing the revision.
     * @param data The contents of the file.
     * @param size The size of the file in bytes.
     * @param mimeType The file's mime type.
     * @param properties Additional properties for the file.
     * @param timestamp The date the revision was created.
     * @param actor The actor that created this revision.
     */
    FileRevision(final Date timestamp,
                 final UserEntity actor,
                 final boolean majorChange,
                 final String comment,
                 final Data data,
                 final long size,
                 final MimeType mimeType,
                 final Map<String, String> properties) {
        super(timestamp, actor, majorChange, comment);
        DBC.require().notNull(data);
        DBC.require().notNull(mimeType);
        _data = data;
        _size = size;
        _mimeType = mimeType; // TODO: Make defensive copy?
        _properties = properties;
    }


    /**
     * Accessor.
     *
     * @return The file revision's data reference.
     */
    public final Data getData() {
        return _data;
    }


    /**
     * Accessor.
     *
     * @return The file revision's size, in bytes.
     */
    public final long getSize() {
        return _size;
    }


    /**
     * Accessor.
     *
     * @return The file revision's mime type.
     */
    public final MimeType getMimeType() {
        return _mimeType;
    }


    /**
     * Accessor.
     *
     * @return True if the file is an image; false otherwise.
     */
    public boolean isImage() {
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }


    /** {@inheritDoc} */
    public boolean isText() {
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
     * Retrieve the properties for a file.
     *
     * @return The properties as a map.
     */
    public Map<String, String> getProperties() {
        return new HashMap<String, String>(_properties);
    }


    /**
     * Accessor.
     *
     * @return The file revision's character set.
     */
    public String getCharset() {
        return getProperties().get(FilePropertyNames.CHARSET);
    }
}
