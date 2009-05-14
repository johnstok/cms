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

import ccc.commands.UpdateFileCommand;
import ccc.content.actions.PersistenceAction;
import ccc.content.actions.ReadWriteTxAction;
import ccc.content.actions.ReaderAction;
import ccc.content.actions.ServletAction;
import ccc.content.actions.SessionKeys;
import ccc.domain.LockMismatchException;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.api.FileDelta;
import ccc.services.api.ID;


/**
 * Servlet to update a file.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileServlet extends CreatorServlet {
    private static final Logger LOG = Logger.getLogger(UpdateFileServlet.class);

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
                        new UpdateFileAction()
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
    public class UpdateFileAction implements ServletAction {

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
            final ID fileId = new ID(form.get("id").getString());
            final FileItem file = form.get("file");

            final FileDelta delta =
                new FileDelta(
                    form.get("title").getString(),
                    form.get("description").getString(),
                    file.getContentType(),
                    (int) file.getSize());
            final InputStream dataStream = file.getInputStream();

            try {
                new UpdateFileCommand(dao, al, dm).execute(
                    actor,
                    new Date(),
                    UUID.fromString(fileId.toString()),
                    delta,
                    dataStream);

            } catch (final MimeTypeParseException e) {
                handleException(response, e);
            } catch (final UnlockedException e) {
                handleException(response, e);
            } catch (final LockMismatchException e) {
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

        private void handleException(final HttpServletResponse response,
                                     final Exception e) throws IOException {
            response.getWriter().write("File update failed. "+e.getMessage());
            LOG.error("File update failed "+e.getMessage(), e);
        }
    }
}
