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
package ccc.rendering;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.TextProcessor;
import ccc.types.DBC;
import ccc.types.Duration;
import ccc.types.MimeType;


/**
 * A CCC response.
 * TODO: Never cache secure resources (i.e. with roles).
 * TODO: The Response#write() method IS NOT THE PLACE for thumbnail code.
 * TODO: _headers should be a set not a map.
 *
 * @author Civic Computing Ltd.
 */
public class Response {
    private static final int MILLISECONDS_IN_A_SECOND = 1000;
    private static final Logger LOG = Logger.getLogger(Response.class);

    private final Map<String, Header> _headers =
        new LinkedHashMap<String, Header>();
    private final Body    _body;

    /**
     * Constructor.
     *
     * @param body The response's body.
     */
    public Response(final Body body) {
        DBC.require().notNull(body);
        _body = body;
    }


    /**
     * Mutator.
     *
     * @param description The new description.
     */
    public void setDescription(final String description) {
        _headers.put("Content-Description",
            new StringHeader("Content-Description", description));
    }


    /**
     * Mutator.
     *
     * @param length The size of the response's body, in bytes.
     */
    public void setLength(final int length) {
        _headers.put("Content-Length", new IntHeader("Content-Length", length));
    }


    /**
     * Mutator.
     *
     * @param charset The new character set.
     */
    public void setCharSet(final String charset) {
        try {
            _headers.put("Charset",
                new CharEncodingHeader(Charset.forName(charset)));
        } catch (final RuntimeException e) {
            LOG.warn("Ignoring invalid charset: "+charset);
        }
    }


    /**
     * Mutator.
     *
     * @param mType The mime type for the response.
     */
    public void setMimeType(final MimeType mType) {
        _headers.put("MimeType", new ContentTypeHeader(mType));
    }


    /**
     * Mutator.
     *
     * @param expiry The response's expiry time, as a Duration.
     */
    public void setExpiry(final Duration expiry) {

        /* Pragma needs to be set to NULL because tomcat is adding
         * "pragma:no-cache" otherwise. See
         * https://issues.apache.org/bugzilla/show_bug.cgi?id=27122 and
         * http://www.mail-archive.com/tomcat-user@jakarta.apache.org/msg151294.html
         */
        _headers.put("Pragma", new StringHeader("Pragma", null));

        if (expiry == null || expiry.time() <= 0) {
            _headers.put(
                "Cache-Control",
                new StringHeader(
                    "Cache-Control",
                    "private, must-revalidate, max-age=0"));
            _headers.put("Expires", new DateHeader("Expires", new Date(0)));
        } else {
            final Date now = new Date();
            final Date expiryDate =
                new Date(
                    now.getTime()+(expiry.time()*MILLISECONDS_IN_A_SECOND));
            _headers.put("Expires", new DateHeader("Expires", expiryDate));
            _headers.put("Cache-Control",
                new StringHeader("Cache-Control", "max-age="+expiry.time()));
        }
    }


    /**
     * Mutator.
     *
     * @param disposition The new disposition for this response.
     */
    public void setDisposition(final String disposition) {
        _headers.put("Content-Disposition",
            new StringHeader("Content-Disposition", disposition));
    }


    /**
     * Accessor.
     *
     * @return The response's body.
     */
    public Body getBody() {
        return _body;
    }


    /**
     * Accessor.
     *
     * @return The response's headers.
     */
    public Map<String, Header> getHeaders() {
        return new LinkedHashMap<String, Header>(_headers);
    }


    /**
     * Write the response using the servlet API.
     *
     * @param httpResponse The servlet response.
     * @param processor The text processor used to render the body.
     * @param context The context for the response.
     *
     * @throws IOException Thrown if writing fails.
     */
    public void write(final HttpServletResponse httpResponse,
                      final Context context,
                      final TextProcessor processor) throws IOException {

        httpResponse.reset();

            final HttpServletRequest r =
                 context.get("request", HttpServletRequest.class);
            if (r != null && r.getParameter("thumb") != null) {
                // For thumbnails only
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                writeBody(
                    baos,
                    httpResponse.getCharacterEncoding(),
                    context,
                    processor);

                final byte[] b = baos.toByteArray();
                setLength(b.length);
                setMimeType(MimeType.JPEG);

                writeHeaders(httpResponse);
                httpResponse.getOutputStream().write(b);

        } else {
            writeHeaders(httpResponse);

            writeBody(
                httpResponse.getOutputStream(),
                httpResponse.getCharacterEncoding(),
                context,
                processor);
        }

    }


    /**
     * Write this response's body to an output stream.
     *
     * @param os The output stream.
     * @param charsetName The character set to use.
     * @param context The context for the response.
     * @param processor The text processor used to render the body.
     *
     * @throws IOException If the output stream encounters an error.
     */
    void writeBody(final OutputStream os,
                   final String charsetName,
                   final Context context,
                   final TextProcessor processor) throws IOException {
        Charset charset = Charset.defaultCharset();
        try {
            charset = Charset.forName(charsetName);
        } catch (final RuntimeException e) {
            LOG.warn("Ignoring invalid charset: "+charset);
        }
        _body.write(os, charset, context, processor);
    }


    /**
     * Write this response's headers to the servlet response.
     *
     * @param httpResponse The servlet response.
     */
    void writeHeaders(final HttpServletResponse httpResponse) {
        for (final Header h : _headers.values()) {
            h.writeTo(httpResponse);
        }
    }


    /**
     * Set an ETag for the response.
     * <p>The specified tag will be quoted before it is sent.
     *
     * @param tag The entity tag to set.
     * @param weak True if this is a weak entity tag, false otherwise.
     */
    public void setEtag(final String tag, final boolean weak) {
        _headers.put(
            "ETag",
            new StringHeader("ETag", ((weak) ? "W/\"" : "\"")+tag+"\""));
    }


    /**
     * Set a 'last modified' date for the response.
     *
     * @param dateModified The last time the response body was modified.
     */
    public void setLastModified(final Date dateModified) {
        _headers.put(
            "Last-Modified",
            new DateHeader("Last-Modified", dateModified));
    }


    /**
     * Configure headers to prevent caching of this response.
     */
    public void dontCache() {
        _headers.remove("Last-Modified");
        _headers.remove("ETag");
        setExpiry(null);
    }
}
