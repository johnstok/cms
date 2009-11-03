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

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.Jsonable;


/**
 * A summary of a file resource.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDto implements Serializable, Jsonable {

    private String _mimeType;
    private String _path;
    private UUID   _id;
    private String _name;
    private String _title;
    private Map<String, String> _properties;

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
    public FileDto(final String type,
                   final String path,
                   final UUID id,
                   final String name,
                   final String title,
                   final Map<String, String> properties) {
        _mimeType = type;
        _path = path;
        _id = id;
        _name = name;
        _title = title;
        _properties = properties;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public FileDto(final Json json) {
        this(
            json.getString(MIME_TYPE),
            json.getString(PATH),
            json.getId(ID),
            json.getString(NAME),
            json.getString(TITLE),
            json.getStringMap(PROPERTIES)
        );
    }

    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public String getMimeType() {
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
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
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
        json.set(NAME, getName());
        json.set(TITLE, getTitle());
        json.set(PROPERTIES, getProperties());
    }
}
