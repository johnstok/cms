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

import ccc.api.MimeType;


/**
 * TODO: Add a description for this type.
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

}