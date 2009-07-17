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
import java.util.Date;

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
import ccc.api.ResourceSummary;
import ccc.domain.Snapshot;
import ccc.services.LocalCommands;


/**
 * Servlet to create a file.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFileServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(CreateFileServlet.class);

    @EJB(name=Commands.NAME) private LocalCommands _commands;


    /**
     * {@inheritDoc}
     */
    @Override
    public void service(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        final MultipartForm form = new MultipartForm(request);

        final FileItem file        = form.getFileItem();
        final FileItem name        = form.getFormItem("fileName");
        final FileItem title       = form.getFormItem("title");
        final FileItem description = form.getFormItem("description");
        final FileItem path        = form.getFormItem("path");
        final FileItem publish     = form.getFormItem("publish");
        final FileItem lastUpdate  = form.getFormItem("lastUpdate");

        final ID parentId = new ID(path.getString());
        final boolean p =
            (null==publish)
                ? false
                : Boolean.parseBoolean(publish.getString());

        final FileDelta delta =
            new FileDelta(
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize());

        final String titleString =
            (title == null) ? name.getString() : title.getString();

        final String descriptionString =
                (description == null) ? "" : description.getString();

        Date lastUpdateDate = new Date();
        if (lastUpdate != null) {
            lastUpdateDate = new Date(
                Long.valueOf(lastUpdate.getString()).longValue());
        }

        final InputStream dataStream = file.getInputStream();

        try {
            final ResourceSummary rs =
                _commands.createFile(
                    parentId,
                    delta,
                    name.getString(),
                    dataStream,
                    titleString,
                    descriptionString,
                    lastUpdateDate,
                    p);
            response.getWriter().write(toJSON(rs));

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
        final String[] parts = contentType.split("/");
        if (2!=parts.length) {
            LOG.warn("Ignored invalid mime type: "+contentType);
            return MimeType.BINARY_DATA;
        }
        return new MimeType(parts[0], parts[1]);
    }


    private void handleException(final HttpServletResponse response,
                                 final Exception e) throws IOException {
        response.getWriter().write("File Upload failed. "+e.getMessage());
        LOG.error("File Upload failed "+e.getMessage(), e);
    }


    /**
     * Convert a {@link ResourceSummary} to JSON.
     *
     * @param rs The {@link ResourceSummary} to convert.
     * @return The JSON representation,as a string.
     */
    public String toJSON(final ResourceSummary rs) {
        final Snapshot s = new Snapshot();
        rs.toJson(s);
        return s.getDetail();
    }
}
