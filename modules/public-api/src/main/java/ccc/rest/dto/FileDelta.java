/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.MimeType;


/**
 * Delta class representing changes to a file.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDelta implements Serializable, Jsonable {
    private MimeType _mimeType;
    private int _size;
    private UUID _data;
    private Map<String, String> _properties;

    @SuppressWarnings("unused") private FileDelta() { super(); }

    /**
     * Constructor.
     *
     * @param mimeType The file's mime type.
     * @param size The file's size.
     * @param data A reference to the files data.
     * @param properties The file's properties.
     */
    public FileDelta(final MimeType mimeType,
                     final UUID data,
                     final int size,
                     final Map<String, String> properties) {
        _mimeType = mimeType;
        _data = data;
        _size = size;
        _properties = new HashMap<String, String>(properties);
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this delta.
     */
    public FileDelta(final Json json) {
        this(
            new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
            json.getId(JsonKeys.DATA),
            json.getInt(JsonKeys.SIZE).intValue(),
            json.getStringMap("properties"));
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
     * @return Returns the size.
     */
    public int getSize() {
        return _size;
    }


    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    public UUID getData() {
        return _data;
    }


    /**
     * Mutator.
     *
     * @param data The data to set.
     */
    public void setData(final UUID data) {
        _data = data;
    }


    /**
     * Accessor.
     *
     * @return Returns the properties.
     */
    public final Map<String, String> getProperties() {
        return _properties;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.MIME_TYPE, getMimeType());
        json.set(JsonKeys.SIZE, (long) getSize());
        json.set(JsonKeys.DATA, getData());
        json.set("properties", getProperties());
    }
}
