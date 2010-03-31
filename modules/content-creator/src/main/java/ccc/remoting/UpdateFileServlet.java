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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.rest.dto.FileDelta;
import ccc.types.FilePropertyNames;


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
        final UUID fileId = UUID.fromString(form.getFormItem("id").getString());

        final FileItem cItem = form.getFormItem("comment");
        final String comment = cItem==null ? null : cItem.getString();

        final FileItem bItem = form.getFormItem("majorEdit");
        final boolean isMajorEdit = bItem == null ? false : true;

        final FileItem file = form.getFileItem().get("file");

        final Map<String, String> props = new HashMap<String, String>();
        props.put(FilePropertyNames.CHARSET, toCharset(file.getContentType()));

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
            getFiles().updateFile(
                                    fileId,
                                    delta,
                                    comment,
                                    isMajorEdit,
                                    dataStream);
            response.getWriter().write("NULL");

        } catch (final EJBException e) {
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
