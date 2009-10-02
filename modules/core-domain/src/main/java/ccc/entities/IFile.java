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

package ccc.entities;

import ccc.types.MimeType;


/**
 * API for a file.
 *
 * @author Civic Computing Ltd.
 */
public interface IFile {

    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    IData getData();

    /**
     * Accessor.
     *
     * @return Returns the size.
     */
    int getSize();

    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    MimeType getMimeType();

    /**
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    boolean isImage();

    /**
     * Accessor.
     *
     * @return The character set for the file or NULL if charset is available.
     */
    String getCharset();
}
