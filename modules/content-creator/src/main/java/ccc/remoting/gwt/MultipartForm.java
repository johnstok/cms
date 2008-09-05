/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.gwt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ccc.commons.DBC;
import ccc.domain.CCCException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MultipartForm {

    private static final long MAX_FILE_SIZE      = 32*1024*1024; //  32mb
    private static final int  MAX_IN_MEMORY_SIZE = 500*1024;     // 500kb

    private final Map<String, FileItem> _items =
        new HashMap<String, FileItem>();

    /**
     * Constructor.
     *
     * @param items
     */
    public MultipartForm(final List<FileItem> items) {
        DBC.require().notNull(items);

        for (final FileItem item : items) {
            final String fn = item.getFieldName();
            if (_items.containsKey(fn)) {
                throw new CCCException("Duplicate field name on form: "+fn);
            }
            _items.put(fn, item);
        }
    }

    /**
     * Constructor.
     *
     * @param request
     */
    public MultipartForm(final HttpServletRequest request) {
        this(parseFileItems(request));
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List<FileItem> parseFileItems(final HttpServletRequest request) {
        DBC.require().notNull(request);

        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new CCCException("Not a multipart");
        }

        // Create a factory for disk-based file items
        final DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set factory constraints
        factory.setSizeThreshold(MAX_IN_MEMORY_SIZE);

        // Create a new file upload handler
        final ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        upload.setFileSizeMax(MAX_FILE_SIZE);

        try {
            return upload.parseRequest(request);
        } catch (final FileUploadException e) {
            throw new CCCException("Failed to parse multipart request.", e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @return
     */
    public FileItem get(final String string) {
        return _items.get(string);
    }

}
