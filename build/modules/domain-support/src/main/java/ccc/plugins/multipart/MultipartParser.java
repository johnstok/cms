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

import java.io.InputStream;


/**
 * Parser for multipart input streams.
 *
 * @author Civic Computing Ltd.
 */
public interface MultipartParser {

    /**
     * Parse a multipart input stream.
     *
     * @param charEncoding  The character encoding for the content.
     * @param contentLength The length of the multipart content.
     * @param contentType   The mime-type for the content.
     * @param inputStream   The input stream for reading the content.
     *
     * @return The multipart form data plugin.
     */
    MultipartFormData parse(final String charEncoding,
                            final int contentLength,
                            final String contentType,
                            final InputStream inputStream);
}
