package ccc.remoting.gwt;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.File;
import ccc.domain.FileData;
import ccc.domain.ResourceName;
import ccc.services.AssetManager;
import ccc.services.ContentManager;


/**
 * Servlet to handle form file uploads.
 *
 * @author Civic Computing Ltd
 */
public class FileUploadServlet extends HttpServlet {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 4396761206168690263L;

    private static Logger log = Logger.getLogger(FileUploadServlet.class);

    private final int _maxMemorySize = 500*1024;
    private final java.io.File _tempDirectory = new java.io.File("/tmp");
    private final long _maxRequestSize = 32*1024*1024;

    private final Registry _registry = new JNDI();

    /**
     * {@inheritDoc}
     */
    public void service(final HttpServletRequest request,
                        final HttpServletResponse response)
    throws ServletException, IOException {

        response.setContentType("text/html");

        try {
            // Check that we have a file upload request
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                log.error("Not a multipart");
                return;
            }

            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Set factory constraints
            factory.setSizeThreshold(_maxMemorySize);
            factory.setRepository(_tempDirectory);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Set overall request size constraint
            upload.setSizeMax(_maxRequestSize);

            byte[] data = null;
            String fileName = null;
            String title = null;
            String description = null;
            String path = null;

            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    if (item.getFieldName().equals("fileName")) {
                        fileName = item.getString();
                    } else if (item.getFieldName().equals("title")) {
                        title = item.getString();
                    } else if (item.getFieldName().equals("description")) {
                        description = item.getString();
                    } else if (item.getFieldName().equals("path")) {
                        path = item.getString();
                    }
                } else {
                    data = item.get();
                }
            }

            FileData fileData = new FileData(data);
            File file = new File(
                new ResourceName(fileName), title, description, fileData);
            // Call EJB
            assetManager().createFile(file, path);
            response.getWriter().write("File was uploaded successfully.");

        } catch (FileUploadException e) {
            response.getWriter().write("File Upload failed.");
            log.error("File Upload failed "+e.getMessage(), e);
        }
    }

    /**
     * Accessor for the asset manager.
     *
     * @return An AssetManager.
     */
    AssetManager assetManager() {
        return _registry.get("AssetManagerEJB/local");
    }

    /**
     * Accessor for the content manager.
     *
     * @return A ContentManager.
     */
    ContentManager contentManager() {
        return _registry.get("ContentManagerEJB/local");
    }

}
