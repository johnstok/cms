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
package ccc.rendering;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.api.DBC;
import ccc.api.Duration;
import ccc.api.MimeType;
import ccc.commons.TextProcessor;


/**
 * A CCC response.
 * TODO: Never cache secure resources (i.e. with roles).
 *
 * @author Civic Computing Ltd.
 */
public class Response {
    private static final int MILLISECONDS_IN_A_SECOND = 1000;
    private static final Logger LOG = Logger.getLogger(Response.class);

    private final List<Header> _headers = new ArrayList<Header>();
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
        _headers.add(new StringHeader("Content-Description", description));
    }


    /**
     * Mutator.
     *
     * @param length The size of the response's body, in bytes.
     */
    public void setLength(final int length) {
        _headers.add(new IntHeader("Content-Length", length));
    }


    /**
     * Mutator.
     *
     * @param charset The new character set.
     */
    public void setCharSet(final String charset) {
        try {
            _headers.add(new CharEncodingHeader(Charset.forName(charset)));
        } catch (final RuntimeException e) {
            LOG.warn("Ignoring invalid charset: "+charset);
        }
    }


    /**
     * Mutator.
     *
     * @param primary The primary part of the mime type.
     * @param secondary The secondary part of the mime type.
     */
    public void setMimeType(final String primary, final String secondary) {
        _headers.add(new ContentTypeHeader(new MimeType(primary, secondary)));
    }


    /**
     * Mutator.
     *
     * @param expiry The response's expiry time, as a Duration.
     */
    public void setExpiry(final Duration expiry) {
        if (expiry == null || expiry.time() <= 0) {
            _headers.add(new StringHeader("Pragma", "no-cache"));
            _headers.add(new StringHeader(
                "Cache-Control", "no-store, must-revalidate, max-age=0"));
            _headers.add(new DateHeader("Expires", new Date(0)));
        } else {
    /* Pragma needs to be set to NULL because tomcat is adding
     * "pragma:no-cache" otherwise. See
     * https://issues.apache.org/bugzilla/show_bug.cgi?id=27122 and
     * http://www.mail-archive.com/tomcat-user@jakarta.apache.org/msg151294.html
     */
            _headers.add(new StringHeader("Pragma", null));
            final Date now = new Date();
            final Date expiryDate =
                new Date(
                    now.getTime()+(expiry.time()*MILLISECONDS_IN_A_SECOND));
            _headers.add(new DateHeader("Expires", expiryDate));
            _headers.add(
                new StringHeader("Cache-Control", "max-age="+expiry.time()));
        }
    }


    /**
     * Mutator.
     *
     * @param disposition The new disposition for this response.
     */
    public void setDisposition(final String disposition) {
        _headers.add(new StringHeader("Content-Disposition", disposition));
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
    public List<Header> getHeaders() {
        return new ArrayList<Header>(_headers);
    }


    /**
     * Write the response using the servlet API.
     *
     * @param httpResponse The servlet response.
     * @throws IOException Thrown if writing fails.
     */
    public void write(final HttpServletResponse httpResponse,
                      final TextProcessor processor) throws IOException {
        writeHeaders(httpResponse);
        writeBody(
            httpResponse.getOutputStream(),
            httpResponse.getCharacterEncoding(),
            processor);
    }


    /**
     * Write this response's body to an output stream.
     *
     * @param os The output stream.
     * @param charsetName The character set to use.
     *
     * @throws IOException If the output stream encounters an error.
     */
    void writeBody(final OutputStream os,
                   final String charsetName,
                   final TextProcessor processor) throws IOException {
        Charset charset = Charset.defaultCharset();
        try {
            charset = Charset.forName(charsetName);
        } catch (final RuntimeException e) {
            LOG.warn("Ignoring invalid charset: "+charset);
        }
        _body.write(os, charset, processor);
    }


    /**
     * Write this response's headers to the servlet response.
     *
     * @param httpResponse The servlet response.
     */
    void writeHeaders(final HttpServletResponse httpResponse) {
        for (final Header h : _headers) {
            h.writeTo(httpResponse);
        }
    }
}
