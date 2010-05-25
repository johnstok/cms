/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.plugins.multipart;

import java.io.IOException;
import java.io.InputStream;


/**
 * API for accessing multipart form data.
 *
 * @author Civic Computing Ltd.
 */
public interface MultipartFormData {

    /**
     * Retrieve an input stream for a form item.
     *
     * @param key The key to the item.
     *
     * @throws IOException If a stream cannot be opened.
     *
     * @return An input stream to read the item.
     */
    InputStream getInputStream(final String key) throws IOException;

    /**
     * Get the content type for a form item.
     *
     * @param key The key to the item.
     *
     * @return The content type as a string.
     */
    String getContentType(final String key);

    /**
     * Get the size in bytes of a form item.
     *
     * @param key The key to the item.
     *
     * @return The size, as a long.
     */
    long getSize(final String key);

    /**
     * Get the string representation of a form item.
     *
     * @param key The key to the item.
     *
     * @return The item value, as a string.
     */
    String getString(final String key);

}
