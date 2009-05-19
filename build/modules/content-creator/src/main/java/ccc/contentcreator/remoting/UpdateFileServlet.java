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

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.LocalCommands;
import ccc.api.MimeType;


/**
 * Servlet to update a file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileServlet extends CreatorServlet {
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
        final ID fileId = new ID(form.get("id").getString());
        final FileItem file = form.get("file");

        final FileDelta delta =
            new FileDelta(
                form.get("title").getString(),
                form.get("description").getString(),
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize());
        final InputStream dataStream = file.getInputStream();

        try {
            _commands.updateFile(fileId, delta, dataStream);

        } catch (final CommandFailedException e) {
            handleException(response, e);

        } finally {
            try {
                dataStream.close();
            } catch (final Exception e) {
                LOG.error("DataStream closing failed "+e.getMessage(), e);
            }
        }

        response.getWriter().write("File was updated successfully.");
    }


    private MimeType toMimeType(final String contentType) {
        final String[] parts = contentType.split("/");
        if (2!=parts.length) {
            LOG.warn("Ignored invalid mime type: "+contentType);
            return MimeType.BINARY_DATA;
        }
        return new MimeType(parts[0], parts[1]);
    }


    private void handleException(final HttpServletResponse response,
                                 final Exception e) throws IOException {
        response.getWriter().write("File update failed. "+e.getMessage());
        LOG.error("File update failed "+e.getMessage(), e);
    }
}
