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

package ccc.remoting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.MimeTypeParseException;
import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.rest.Files;
import ccc.rest.RestException;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FilesExt;
import ccc.serialization.JsonImpl;
import ccc.types.MimeType;


/**
 * Abstract servlet class that provides helper methods when handling multipart
 * mime requests.
 *
 * @author Civic Computing Ltd.
 */
public abstract class MultipartServlet
    extends
        HttpServlet {
    private static final Logger LOG = Logger.getLogger(MultipartServlet.class);

    @EJB(name = Files.NAME) private FilesExt _files;


    /**
     * Extract the mime type from the content-type HTTP header.
     * <p>If the header value is un-parsable then "application/octet-stream"
     * will be returned.
     *
     * @param contentType The HTTP content type.
     *
     * @return The mime type as a value object.
     */
    protected MimeType toMimeType(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return new MimeType(mt.getPrimaryType(), mt.getSubType());
        } catch (final MimeTypeParseException e) {
            LOG.warn("Ignored invalid mime type: "+contentType);
            return MimeType.BINARY_DATA;
        }
    }


    /**
     * Extract the charset parameter from the content-type HTTP header.
     *
     * @param contentType The HTTP content type.
     *
     * @return The charset param as a string.
     */
    protected String toCharset(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return mt.getParameter("charset");
        } catch (final MimeTypeParseException e) {
            return null;
        }
    }


    /**
     * Handle a failure in processing a multipart request.
     *
     * @param response The HTTP response.
     * @param e The exception to handle.
     *
     * @throws IOException If writing to the response fails.
     */
    protected void handleException(final HttpServletResponse response,
                                   final RestException e)
    throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(new JsonImpl(e.getFailure()).getDetail());
    }


    /**
     * Convert a {@link ResourceSummary} to JSON.
     *
     * @param rs The {@link ResourceSummary} to convert.
     *
     * @return The JSON representation,as a string.
     */
    protected String toJSON(final ResourceSummary rs) {
        final JsonImpl s = new JsonImpl();
        rs.toJson(s);
        return s.getDetail();
    }


    /**
     * Returns maximum upload file size.
     *
     * @return The size in bytes.
     */
    protected long maxFileSize() {
        long maxSize = 32 * 1024 * 1024; // Default value 32MB.
        try {
            InputStream inStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("uploadConfig.properties");
            Properties uploadProps = new Properties();
            uploadProps.load(inStream);
            maxSize = Long.decode(uploadProps.getProperty("maxFileSize"));
            
        } catch(Exception e) {
            LOG.warn("Setting maximum file size for upload failed, using default 32MB.");
        }
        return maxSize;
    }
    
    /**
     * Accessor.
     *
     * @return Returns the files implementation.
     */
    public final FilesExt getFiles() {
        return _files;
    }
}
