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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.domain.CommandFailedException;
import ccc.domain.File;
import ccc.rest.FileDelta;
import ccc.types.ID;


/**
 * Servlet to update a file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileServlet extends MultipartServlet {
    private static final Logger LOG = Logger.getLogger(UpdateFileServlet.class);


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
        final ID fileId = new ID(form.getFormItem("id").getString());

        final FileItem cItem = form.getFormItem("comment");
        final String comment = cItem==null ? null : cItem.getString();

        final FileItem bItem = form.getFormItem("majorEdit");
        final boolean isMajorEdit = bItem == null ? false : true;

        final FileItem file = form.getFileItem();

        final Map<String, String> props = new HashMap<String, String>();
        props.put(File.CHARSET, toCharset(file.getContentType()));

        final FileDelta delta =
            new FileDelta(
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize(),
                props);


        /* ====================================================================
         * Perform the update.
         * ================================================================== */
        final InputStream dataStream = file.getInputStream();

        try {
            getCommands().updateFile(
                                    fileId,
                                    delta,
                                    comment,
                                    isMajorEdit,
                                    dataStream);
            response.getWriter().write("NULL");

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
