package ccc.contentcreator.remoting;


import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import ccc.services.api.FileDelta;
import ccc.services.api.ID;


/**
 * Servlet to update the data in a file.
 * TODO: extend CCCServlet?
 *
 * @author Civic Computing Ltd
 */
public class UpdateFileServlet extends CreatorServlet {

    private static final Logger LOG = Logger.getLogger(UpdateFileServlet.class);


    /**
     * {@inheritDoc}
     */
    @Override
    public void service(final HttpServletRequest request,
                        final HttpServletResponse response)
    throws ServletException, IOException {

        response.setContentType("text/html");

        try {
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
                _services.dataManager().updateFile(null, fileId, delta, dataStream);
            } finally {
                try {
                    dataStream.close();
                } catch (final Exception e) {
                    LOG.error("DataStream closing failed "+e.getMessage(), e);
                }
            }

            response.getWriter().write("File was updated successfully.");

        } catch (final RuntimeException e) {
            response.getWriter().write("File update failed. "+e.getMessage());
            LOG.error("File update failed "+e.getMessage(), e);
        }
    }
}
