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

package ccc.contentcreator.remoting;

import java.io.IOException;

import javax.activation.MimeTypeParseException;
import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.api.ResourceSummary;
import ccc.domain.CommandFailedException;
import ccc.domain.JsonImpl;
import ccc.persistence.LocalCommands;
import ccc.services.Commands;
import ccc.types.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class MultipartServlet
    extends
        HttpServlet {
    private static final Logger LOG = Logger.getLogger(MultipartServlet.class);

    @EJB(name = Commands.NAME) private LocalCommands _commands;


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
                                   final CommandFailedException e)
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
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final LocalCommands getCommands() {
        return _commands;
    }
}