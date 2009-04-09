package ccc.contentcreator.remoting;


import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;


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

            final FileItem file        = form.get("file");
            final FileItem id          = form.get("id");
            final FileItem title       = form.get("title");
            final FileItem description = form.get("description");


            final InputStream dataStream = file.getInputStream();
            try {
                _services.dataManager().updateFile(
                    UUID.fromString(id.getString()),
                    title.getString(),
                    description.getString(),
                    new MimeType(file.getContentType()),
                    file.getSize(),
                    dataStream
                );
            } finally {
                try {
                    dataStream.close();
                } catch (final Exception e) {
                    LOG.error("DataStream closing failed "+e.getMessage(), e);
                }
            }

            response.getWriter().write("File was updated successfully.");

        } catch (final MimeTypeParseException e) {
            response.getWriter().write("File update failed. "+e.getMessage());
            LOG.error("File update failed "+e.getMessage(), e);
        }
    }
}
