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
package ccc.plugins.multipart.apache;

import java.io.InputStream;

import org.apache.commons.fileupload.RequestContext;

import ccc.api.types.DBC;


/**
 * A simple request context for Apache commons file-upload.
 *
 * @author Civic Computing Ltd.
 */
public class JaxrsRequestContext implements RequestContext {

    private final String      _charEncoding;
    private final int         _contentLength;
    private final String      _contentType;
    private final InputStream _inputStream;



    /**
     * Constructor.
     *
     * @param charEncoding The character encoding of the input stream.
     * @param contentLength The number of bytes on the input stream.
     * @param contentType The input stream's media type.
     * @param inputStream The stream to parse as multipart.
     */
    public JaxrsRequestContext(final String charEncoding,
                               final int contentLength,
                               final String contentType,
                               final InputStream inputStream) {
        _charEncoding = charEncoding;
        DBC.require().greaterThan(0, contentLength);
        _contentLength = contentLength;
        _contentType = DBC.require().notEmpty(contentType);
        _inputStream = DBC.require().notNull(inputStream);
    }

    /** {@inheritDoc} */
    @Override
    public String getCharacterEncoding() {
        return _charEncoding;
    }

    /** {@inheritDoc} */
    @Override
    public int getContentLength() {
        return _contentLength;
    }

    /** {@inheritDoc} */
    @Override
    public String getContentType() {
        return _contentType;
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() {
        return _inputStream;
    }

}
