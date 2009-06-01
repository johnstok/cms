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
package ccc.api;

import java.io.Serializable;


/**
 * Delta class representing changes to a file.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDelta implements Serializable, Jsonable {
    private String _title;
    private String _description;
    private MimeType _mimeType;
    private int _size;
    private ID _data;

    @SuppressWarnings("unused") private FileDelta() { super(); }

    /**
     * Constructor.
     *
     * @param title The file's title.
     * @param description The file's description.
     * @param mimeType The file's mime type.
     * @param size The file's size.
     * @param data A reference to the files data.
     */
    public FileDelta(final String title,
                     final String description,
                     final MimeType mimeType,
                     final ID data,
                     final int size) {
        _title = title;
        _description = description;
        _mimeType = mimeType;
        _data = data;
        _size = size;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this delta.
     */
    public FileDelta(final Json json) {
        this(
            json.getString(JsonKeys.TITLE),
            json.getString(JsonKeys.DESCRIPTION),
            new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
            json.getId(JsonKeys.DATA),
            json.getInt(JsonKeys.SIZE));
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
     * Mutator.
     *
     * @param title The title to set.
     */
    public void setTitle(final String title) {
        _title = title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return _description;
    }


    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _description = description;
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
    public ID getData() {
        return _data;
    }


    /**
     * Mutator.
     *
     * @param data The data to set.
     */
    public void setData(final ID data) {
        _data = data;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.TITLE, getTitle());
        json.set(JsonKeys.MIME_TYPE, getMimeType());
        json.set(JsonKeys.SIZE, (long) getSize());
        json.set(JsonKeys.DESCRIPTION, getDescription());
        json.set(JsonKeys.DATA, getData());
    }
}
