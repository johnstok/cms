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
package ccc.api.dto;

import static ccc.plugins.s11n.JsonKeys.*;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Jsonable;


/**
 * A summary of a file resource.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDto
    extends
        ResourceSnapshot
    implements
        Jsonable {

    private MimeType            _mimeType;
    private String              _path;
    private Map<String, String> _properties;
    private String              _charset;
    private UUID                _dataId;
    private int                 _size;
    private boolean             _isImage;
    private boolean             _isExecutable;
    private boolean             _isText;
    private boolean             _isMajorEdit;
    private String              _comment;
    private InputStream         _inputStream;


    @SuppressWarnings("unused") private FileDto() { super(); }


    /**
     * Constructor.
     *
     * @param type The file's mime type.
     * @param path The file's absolute path.
     * @param id The file's id.
     * @param name The file's name.
     * @param title The file's title.
     * @param properties The file's properties
     */
    public FileDto(final MimeType type,
                   final String path,
                   final UUID id,
                   final ResourceName name,
                   final String title,
                   final Map<String, String> properties) {
        _mimeType = type;
        _path = path;
        setId(id);
        setName(name);
        setTitle(title);
        _properties = properties;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public FileDto(final Json json) {
        this(
            new MimeType(json.getJson(MIME_TYPE)),
            json.getString(PATH),
            json.getId(ID),
            new ResourceName(json.getString(NAME)),
            json.getString(TITLE),
            json.getStringMap(PROPERTIES)
        );
    }


    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType getMimeType() {
        return _mimeType;
    }


    /**
     * Accessor.
     *
     * @return Returns the path.
     */
    public String getPath() {
        return _path;
    }


    /**
     * Accessor.
     *
     * @return Returns the properties.
     */
    public Map<String, String> getProperties() {
        return _properties;
    }


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        json.set(MIME_TYPE, getMimeType());
        json.set(PATH, getPath());
        json.set(ID, getId());
        json.set(NAME, getName().toString());
        json.set(TITLE, getTitle());
        json.set(PROPERTIES, getProperties());
    }


    /**
     * Accessor.
     *
     * @return The character set for the file or NULL if charset is available.
     */
    public String getCharset() {
        return _charset;
    }


    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    public UUID getData() {
        return _dataId;
    }


    /**
     * Accessor.
     *
     * @return Returns the size.
     */
    public int getSize() {
        return _size;
    }


    /**
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return _isImage;
    }


    /**
     * Query if this file is executable.
     *
     * @return True if the file is executable, false otherwise.
     */
    public boolean isExecutable() {
        return _isExecutable;
    }


    /**
     * Query if this file is an text.
     *
     * @return True if the file is a text, false otherwise.
     */
    public boolean isText() {
        return _isText;
    }


    /**
     * Mutator.
     *
     * @param dataId The dataId to set.
     */
    public void setDataId(final UUID dataId) {
        _dataId = dataId;
    }


    /**
     * Mutator.
     *
     * @param charset The charset to set.
     */
    public void setCharset(final String charset) {
        _charset = charset;
    }


    /**
     * Mutator.
     *
     * @param size The size to set.
     */
    public void setSize(final int size) {
        _size = size;
    }


    /**
     * Mutator.
     *
     * @param isImage The isImage to set.
     */
    public void setImage(final boolean isImage) {
        _isImage = isImage;
    }


    /**
     * Mutator.
     *
     * @param isExecutable The isExecutable to set.
     */
    public void setExecutable(final boolean isExecutable) {
        _isExecutable = isExecutable;
    }


    /**
     * Mutator.
     *
     * @param isText The isText to set.
     */
    public void setText(final boolean isText) {
        _isText = isText;
    }


    /**
     * Mutator.
     *
     * @param mimeType The mimeType to set.
     */
    public void setMimeType(final MimeType mimeType) {
        _mimeType = mimeType;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param comment
     */
    public void setComment(final String comment) {
        _comment = comment;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param isMajorEdit
     */
    public void setMajorEdit(final boolean isMajorEdit) {
        _isMajorEdit = isMajorEdit;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public boolean isMajorEdit() {
        return _isMajorEdit;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String getComment() {
        return _comment;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public InputStream getInputStream() {
        return _inputStream;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param inputStream
     */
    public void setInputStream(final InputStream inputStream) {
        _inputStream = inputStream;
    }
}
