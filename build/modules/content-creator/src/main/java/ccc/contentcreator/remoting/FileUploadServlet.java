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
import java.util.UUID;

import javax.activation.MimeTypeParseException;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.commands.CreateFileCommand;
import ccc.content.actions.PersistenceAction;
import ccc.content.actions.ReadWriteTxAction;
import ccc.content.actions.ReaderAction;
import ccc.content.actions.ServletAction;
import ccc.content.actions.SessionKeys;
import ccc.domain.CCCException;
import ccc.domain.File;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.ModelTranslation;
import ccc.services.api.FileDelta;
import ccc.services.api.ResourceSummary;


/**
 * Servlet to create a file.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploadServlet extends CreatorServlet {
    private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);

    @Resource                    private transient UserTransaction      _utx;
    @PersistenceUnit             private transient EntityManagerFactory _emf;


    /**
     * {@inheritDoc}
     */
    @Override
    public void service(final HttpServletRequest request,
                        final HttpServletResponse response)
    throws ServletException, IOException {

        final ServletAction action =
            new ReadWriteTxAction(
                new PersistenceAction(
                    new ReaderAction(
                        new CreateFileAction()
                        ),
                    _emf),
                _utx);

        action.execute(request, response);
    }


    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    public class CreateFileAction implements ServletAction {

        /** {@inheritDoc} */
        @Override
        public void execute(final HttpServletRequest request,
                            final HttpServletResponse response)
                                          throws ServletException, IOException {

            final Dao dao =
                (Dao) request.getAttribute(SessionKeys.DAO_KEY);
            final User actor =
                (User) request.getAttribute(SessionKeys.CURRENT_USER);
            final AuditLog al  =
                (AuditLog) request.getAttribute(SessionKeys.AUDIT_KEY);
            final DataManager dm =
                (DataManager) request.getAttribute(SessionKeys.DATA_KEY);

            response.setContentType("text/html");

            final MultipartForm form = new MultipartForm(request);

            final FileItem file        = form.get("file");
            final FileItem name        = form.get("fileName");
            final FileItem title       = form.get("title");
            final FileItem description = form.get("description");
            final FileItem path        = form.get("path");

            final UUID parentId = UUID.fromString(path.getString());

            final FileDelta delta =
                new FileDelta(
                    title.getString(),
                    description.getString(),
                    file.getContentType(),
                    (int) file.getSize());

            final InputStream dataStream = file.getInputStream();

            try {
                final File f =
                    new CreateFileCommand(dao, al, dm).execute(
                        actor,
                        new Date(),
                        parentId,
                        delta,
                        new ResourceName(name.getString()),
                        dataStream);

                final ResourceSummary rs =
                    new ModelTranslation().mapResource(f);
                response.getWriter().write(toJSON(rs).toString());

            } catch (final MimeTypeParseException e) {
                handleException(response, e);
            } catch (final ResourceExistsException e) {
                handleException(response, e);

            } finally {
                try {
                    dataStream.close();
                } catch (final Exception e) {
                    LOG.error("DataStream closing failed "+e.getMessage(), e);
                }
            }
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
}
