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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.LocalCommands;
import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.domain.CCCException;


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

        final FileItem file        = form.get("file");
        final FileItem name        = form.get("fileName");
        final FileItem title       = form.get("title");
        final FileItem description = form.get("description");
        final FileItem path        = form.get("path");
        final FileItem publish     = form.get("publish");

        final ID parentId = new ID(path.getString());
        final boolean p =
            (null==publish)
                ? false
                : Boolean.parseBoolean(publish.getString());

        final FileDelta delta =
            new FileDelta(
                title.getString(),
                description.getString(),
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize());

        final InputStream dataStream = file.getInputStream();

        try {
            final ResourceSummary rs = _commands.createFile(
                parentId, delta, name.getString(), dataStream, p);
            response.getWriter().write(toJSON(rs).toString());

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
     * FIXME: We are missing parameters - see ResourceSummaryModelData.
     *
     * @param rs The {@link ResourceSummary} to convert.
     * @return A JSON object.
     */
    public JSONObject toJSON(final ResourceSummary rs) {
        try {
            final JSONObject o = new JSONObject();
            o.put("id", rs.getId().toString());
            o.put("name", rs.getName());
            o.put("parentId", rs.getParentId().toString());
            o.put("type", rs.getType());
            o.put("lockedBy", rs.getLockedBy());
            o.put("title", rs.getTitle());
            o.put("publishedBy", rs.getPublishedBy());
            o.put("childCount", rs.getChildCount());
            o.put("folderCount", rs.getFolderCount());
            o.put("includeInMainMenu", rs.isIncludeInMainMenu());
            o.put("sortOrder", rs.getSortOrder());
            return o;
        } catch (final JSONException e) {
            throw new CCCException(e);
        }
    }
}
