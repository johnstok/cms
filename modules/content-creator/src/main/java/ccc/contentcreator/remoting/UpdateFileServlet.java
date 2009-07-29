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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimeTypeParseException;
import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.MimeType;
import ccc.domain.File;
import ccc.services.LocalCommands;


/**
 * Servlet to update a file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(UpdateFileServlet.class);

    @EJB(name=Commands.NAME) private LocalCommands _commands;


    /**
     * {@inheritDoc}
     */
    @Override
    public void service(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        final MultipartForm form = new MultipartForm(request);
        final ID fileId = new ID(form.getFormItem("id").getString());
        final FileItem file = form.getFileItem();

        final Map<String, String> props = new HashMap<String, String>();
        props.put(File.CHARSET, toCharset(file.getContentType()));

        final FileDelta delta =
            new FileDelta(
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize(),
                props);
        final InputStream dataStream = file.getInputStream();

        try {
            _commands.updateFile(fileId, delta, dataStream);
            response.getWriter().write("File was updated successfully.");

        } catch (final CommandFailedException e) {
            handleException(response, e);

        } finally {
            try {
                dataStream.close();
            } catch (final Exception e) {
                LOG.error("DataStream closing failed "+e.getMessage(), e);
            }
        }
    }


    private MimeType toMimeType(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return new MimeType(mt.getPrimaryType(), mt.getSubType());
        } catch (final MimeTypeParseException e) {
            LOG.warn("Ignored invalid mime type: "+contentType);
            return MimeType.BINARY_DATA;
        }
    }


    private String toCharset(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return mt.getParameter("charset");
        } catch (final MimeTypeParseException e) {
            return null;
        }
    }


    private void handleException(final HttpServletResponse response,
                                 final Exception e) throws IOException {
        response.getWriter().write("File update failed. "+e.getMessage());
        LOG.error("File update failed "+e.getMessage(), e);
    }
}
