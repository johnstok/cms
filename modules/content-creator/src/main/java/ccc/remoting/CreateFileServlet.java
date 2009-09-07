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
package ccc.remoting;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.domain.File;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.ResourceSummary;


/**
 * Servlet to create a file.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFileServlet
    extends
        MultipartServlet {
    private static final Logger LOG = Logger.getLogger(CreateFileServlet.class);


    /**
     * {@inheritDoc}
     */
    @Override
    public void doPost(final HttpServletRequest request,
                      final HttpServletResponse response) throws IOException {


        /* ====================================================================
         * Parse the request payload.
         * ================================================================== */
        response.setContentType("text/html");

        final MultipartForm form = new MultipartForm(request);

        final FileItem file        = form.getFileItem();
        final FileItem name        = form.getFormItem("fileName");
        final FileItem title       = form.getFormItem("title");
        final FileItem description = form.getFormItem("description");
        final FileItem path        = form.getFormItem("path");
        final FileItem publish     = form.getFormItem("publish");
        final FileItem lastUpdate  = form.getFormItem("lastUpdate");

        final FileItem cItem = form.getFormItem("comment");
        final String comment = cItem==null ? null : cItem.getString();

        final FileItem bItem = form.getFormItem("majorEdit");
        final boolean isMajorEdit = bItem == null ? false : true;

        final UUID parentId = UUID.fromString(path.getString());
        final boolean p =
            (null==publish)
                ? false
                : Boolean.parseBoolean(publish.getString());

        final Map<String, String> props = new HashMap<String, String>();
        props.put(File.CHARSET, toCharset(file.getContentType()));

        final FileDelta delta =
            new FileDelta(
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize(),
                props);

        final String titleString =
            (title == null) ? name.getString() : title.getString();

        final String descriptionString =
                (description == null) ? "" : description.getString();

        Date lastUpdateDate = new Date();
        if (lastUpdate != null) {
            lastUpdateDate = new Date(
                Long.valueOf(lastUpdate.getString()).longValue());
        }


        /* ====================================================================
         * Perform the create.
         * ================================================================== */
        final InputStream dataStream = file.getInputStream();

        try {
            final ResourceSummary rs =
                getFiles().createFile(
                    parentId,
                    delta,
                    name.getString(),
                    dataStream,
                    titleString,
                    descriptionString,
                    lastUpdateDate,
                    p,
                    comment,
                    isMajorEdit);
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
}
