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
package ccc.services.api;

import java.io.Serializable;


/**
 * Delta class representing changes to a file.
 *
 * @author Civic Computing Ltd.
 */
public final class FileDelta implements Serializable {
    private ID     _id;
    private String _title;
    private String _description;
    private String _mimeType;
    private int _size;

    @SuppressWarnings("unused") private FileDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id The file's id.
     * @param title The file's title.
     * @param description The file's description.
     * @param mimeType The file's mime type.
     * @param size The file's size.
     */
    public FileDelta(final ID id,
                     final String title,
                     final String description,
                     final String mimeType,
                     final int size) {
        _id = id;
        _title = title;
        _description = description;
        _mimeType = mimeType;
        _size = size;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final ID id) {
        _id = id;
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
    public String getMimeType() {
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
}
