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
package ccc.api;

import java.io.Serializable;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class FileSummary implements Serializable {

    private String _mimeType;
    private String _path;
    private ID     _id;
    private String _name;
    private String _title;

    @SuppressWarnings("unused") private FileSummary() { super(); }

    /**
     * Constructor.
     *
     * @param type The file's mime type.
     * @param path The file's absolute path.
     * @param id The file's id.
     * @param name The file's name.
     * @param title The file's title.
     */
    public FileSummary(final String type,
                       final String path,
                       final ID id,
                       final String name,
                       final String title) {
        _mimeType = type;
        _path = path;
        _id = id;
        _name = name;
        _title = title;
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
    public ID getId() {
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
}
