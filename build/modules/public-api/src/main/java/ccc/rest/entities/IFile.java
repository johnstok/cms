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

package ccc.rest.entities;

import java.util.UUID;

import ccc.types.MimeType;


/**
 * API for a file.
 *
 * @author Civic Computing Ltd.
 */
public interface IFile
    extends
        IResource {

    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    UUID getData();

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
     * Query if this file is an text.
     *
     * @return True if the file is a text, false otherwise.
     */
    boolean isText();

    /**
     * Query if this file is executable.
     *
     * @return True if the file is executable, false otherwise.
     */
    boolean isExecutable();

    /**
     * Accessor.
     *
     * @return The character set for the file or NULL if charset is available.
     */
    String getCharset();
}
