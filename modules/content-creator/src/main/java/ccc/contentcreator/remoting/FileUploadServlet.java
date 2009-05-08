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
import org.json.JSONException;
import org.json.JSONObject;

import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.ResourceName;
import ccc.services.ModelTranslation;
import ccc.services.api.ResourceSummary;


/**
 * Servlet to handle form file uploads.
 * TODO: extend CCCServlet.
 *
 * @author Civic Computing Ltd
 */
public class FileUploadServlet extends CreatorServlet {

    private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);


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
            final FileItem name        = form.get("fileName");
            final FileItem title       = form.get("title");
            final FileItem description = form.get("description");
            final FileItem path        = form.get("path");

//            final String realName = file.getName(); // See #145
            final UUID parentId = UUID.fromString(path.getString());

            final File f =
                new File(
                    new ResourceName(name.getString()),
                    title.getString(),
                    description.getString(),
                    new Data(),
                    (int) file.getSize(), //FIXME: need to set limit to MaxInt
                    new MimeType(file.getContentType()));

            final InputStream dataStream = file.getInputStream();
            try {
                _services.dataManager().createFile(f, parentId, dataStream);
            } finally {
                try {
                    dataStream.close();
                } catch (final Exception e) {
                    LOG.error("DataStream closing failed "+e.getMessage(), e);
                }
            }

            final ResourceSummary rs = new ModelTranslation().map(f);
            response.getWriter().write(toJSON(rs).toString());

        } catch (final MimeTypeParseException e) {
            response.getWriter().write("File Upload failed. "+e.getMessage());
            LOG.error("File Upload failed "+e.getMessage(), e);
        }
    }


    /**
     * Convert a {@link ResourceSummary} to JSON.
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
