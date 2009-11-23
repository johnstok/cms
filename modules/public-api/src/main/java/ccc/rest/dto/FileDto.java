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
package ccc.rest.dto;

import static ccc.serialization.JsonKeys.*;

import java.util.Map;
import java.util.UUID;

import ccc.rest.entities.IFile;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.Jsonable;
import ccc.types.MimeType;
import ccc.types.ResourceName;


/**
 * A summary of a file resource.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDto
    extends
        ResourceSnapshot
    implements
        Jsonable,
        IFile {

    private MimeType            _mimeType;
    private String              _path;
    private Map<String, String> _properties;
    private String              _charset;
    private UUID                _dataId;
    private int                 _size;
    private boolean             _isImage;
    private boolean             _isExecutable;
    private boolean             _isText;


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


    /** {@inheritDoc} */
    @Override
    public String getCharset() {
        return _charset;
    }


    /** {@inheritDoc} */
    @Override
    public UUID getData() {
        return _dataId;
    }


    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return _size;
    }


    /** {@inheritDoc} */
    @Override
    public boolean isImage() {
        return _isImage;
    }


    /** {@inheritDoc} */
    @Override
    public boolean isExecutable() {
        return _isExecutable;
    }


    /** {@inheritDoc} */
    @Override
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
}
