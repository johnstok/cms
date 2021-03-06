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
package ccc.api.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * A summary of a file resource.
 *
 * @author Civic Computing Ltd.
 */
public final class File
    extends
        Resource {

    private MimeType              _mimeType;
    private Map<String, String>   _properties = new HashMap<String, String>();
    private long                  _size;
    private boolean               _isImage;
    private boolean               _isExecutable;
    private boolean               _isText;
    private boolean               _isMajorEdit;
    private String                _comment;

    private UUID                  _dataId;
    private transient InputStream _inputStream;
    private String                _content;

    // TODO: Remove.
    private String                _path; // Duplicate of ResSumm.getAbsPath()?


    /**
     * Constructor.
     */
    public File() { super(); }


    /**
     * Constructor.
     *
     * @param parentId The parent ID.
     * @param name The file's name.
     * @param mimeType The file's mime type.
     * @param isMajorRevision Is this a major revision.
     * @param revisionComment Comment describing the revision.
     * @param content The file's content.
     */
    public File(final UUID parentId,
                   final String name,
                   final MimeType mimeType,
                   final boolean isMajorRevision,
                   final String revisionComment,
                   final String content) {
        setParent(parentId);
        setName(new ResourceName(name));
        _mimeType = mimeType;
        setMajorEdit(isMajorRevision);
        setComment(revisionComment);
        _content = content;
    }


    /**
     * Constructor.
     *
     * @param mimeType The file's mime type.
     * @param size The file's size.
     * @param data A reference to the files data.
     * @param properties The file's properties.
     */
    public File(final MimeType mimeType,
                   final UUID data,
                   final long size,
                   final Map<String, String> properties) {
        _mimeType = mimeType;
        _dataId = data;
        _size = size;
        _properties = new HashMap<String, String>(properties);
    }


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
    public File(final MimeType type,
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
     * @param id The file's ID.
     * @param content The file's content.
     * @param mimeType The file's mime-type.
     * @param isMajorRevision Is this a major revision.
     * @param revisionComment Comment describing the revision.
     */
    public File(final UUID id,
                   final String content,
                   final MimeType mimeType,
                   final boolean isMajorRevision,
                   final String revisionComment) {
        setId(id);
        _content = content;
        _mimeType = mimeType;
        setMajorEdit(isMajorRevision);
        setComment(revisionComment);
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


    /**
     * Accessor.
     *
     * @return The character set for the file or NULL if charset is available.
     */
    public String getCharset() {
        return _properties.get(FilePropertyNames.CHARSET);
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
     * Mutator.
     *
     * @param data The data to set.
     */
    public void setData(final UUID data) {
        _dataId = data;
    }


    /**
     * Accessor.
     *
     * @return Returns the size.
     */
    public long getSize() {
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
        _properties.put(FilePropertyNames.CHARSET, charset);
    }


    /**
     * Mutator.
     *
     * @param size The size to set.
     */
    public void setSize(final long size) {
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
     * Mutator.
     *
     * @param comment to set.
     */
    public void setComment(final String comment) {
        _comment = comment;
    }


    /**
     * Mutator.
     *
     * @param isMajorEdit to set.
     */
    public void setMajorEdit(final boolean isMajorEdit) {
        _isMajorEdit = isMajorEdit;
    }


    /**
     * Accessor.
     *
     * @return Returns the isMajorEdit.
     */
    public boolean isMajorEdit() {
        return _isMajorEdit;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the input stream.
     */
    public InputStream getInputStream() {
        return _inputStream;
    }


    /**
     * Mutator.
     *
     * @param inputStream The input stream to set.
     */
    public void setInputStream(final InputStream inputStream) {
        _inputStream = inputStream;
    }


    /**
     * Accessor.
     *
     * @return Returns the content.
     */
    public String getContent() {
        return _content;
    }


    /**
     * Mutator.
     *
     * @param path The path to set.
     */
    public void setPath(final String path) {
        _path = path;
    }


    /**
     * Mutator.
     *
     * @param properties The properties to set.
     */
    public void setProperties(final Map<String, String> properties) {
        _properties = new HashMap<String, String>(properties);
    }


    /**
     * Mutator.
     *
     * @param content The content to set.
     */
    public void setContent(final String content) {
        _content = content;
    }


    /**
     * Accessor.
     *
     * @return The width of the image resource.
     */
    public String getWidth() {
        return getProperties().get(FilePropertyNames.WIDTH);
    }


    /**
     * Accessor.
     *
     * @return The height of the image resource.
     */
    public String getHeight() {
        return getProperties().get(FilePropertyNames.HEIGHT);
    }


    /**
     * Rel names for links on this class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Links {
        private Links() { super(); }

        /** LIST_BINARY : String. */
        public static final String LIST_BINARY = "list_binary";
        /** SELF_BINARY : String. */
        public static final String SELF_BINARY = "self_binary";
        /** RETRIVE_WITH_ID : String. */
        public static final String RETRIVE_WITH_ID = "retrive_with_id";
    }


    /**
     * Property names for this class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Properties {
        private Properties() { super(); }

        /** PATH : String. */
        public static final String PATH = "path";
        /** WIDTH : String. */
        public static final String WIDTH = "width";
        /** HEIGHT : String. */
        public static final String HEIGHT = "height";
    }
}
