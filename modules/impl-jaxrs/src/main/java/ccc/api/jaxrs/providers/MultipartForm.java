/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.api.jaxrs.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import ccc.api.types.DBC;


/**
 * This class represents a mime encoded multi-part form.
 *
 * @author Civic Computing Ltd.
 */
public class MultipartForm {

    private static final long MAX_FILE_SIZE      = 32*1024*1024; //  32mb
    private static final int  MAX_IN_MEMORY_SIZE = 500*1024;     // 500kb

    private final Map<String, FileItem> _formItems =
        new HashMap<String, FileItem>();

    private Map<String, FileItem> _fileItem = new HashMap<String, FileItem>();

    /**
     * Constructor.
     *
     * @param items The list of items on this form.
     */
    public MultipartForm(final List<FileItem> items) {
        DBC.require().notNull(items);

        for (final FileItem item : items) {
            // #430: IE 6 and IE 7 send two fields with same name.
            // The other one is with content type 'file' and the other one
            // is the path of the original file location.
            if (item.isFormField()) {
                final String fn = item.getFieldName();
                if (_formItems.containsKey(fn)) {
                    throw new RuntimeException(
                        "Duplicate field name on form: "+fn);
                }
                _formItems.put(fn, item);
            } else {
                _fileItem.put(item.getFieldName(), item);
            }
        }
    }

    /**
     * Constructor.
     *
     * @param context The JAXRS context for the client request.
     */
    public MultipartForm(final JaxrsRequestContext context) {
        this(parseFileItems(context));
    }

    /**
     * Parse an HTTP request, extracting the file items.
     *
     * @param context The JAXRS context to parse.
     *
     * @return A list of file items.
     */
    @SuppressWarnings("unchecked")
    private static List<FileItem> parseFileItems(
                                            final JaxrsRequestContext context) {

//        DBC.require().notNull(request);

        // Check that we have a file upload request
        // FIXME: Use non Servlet API version.
//        final boolean isMultipart =
//            ServletFileUpload.isMultipartContent(request);
//        if (!isMultipart) {
//            throw new RuntimeException("Not a multipart");
//        }

        // Create a factory for disk-based file items
        final DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set factory constraints
        factory.setSizeThreshold(MAX_IN_MEMORY_SIZE);

        // Create a new file upload handler
        final FileUpload upload = new FileUpload(factory);

        // Set overall request size constraint
        upload.setFileSizeMax(MAX_FILE_SIZE);

        try {
            return upload.parseRequest(context);
        } catch (final FileUploadException e) {
            throw new RuntimeException("Failed to parse multipart request.", e);
        }
    }

    /**
     * Retrieve a form item by name.
     *
     * @param string The name of the item.
     * @return The form item identified by the specified name.
     */
    public FileItem getFormItem(final String string) {
        return _formItems.get(string);
    }

    /**
     * Retrieve form item keys.
     *
     * @return List of names.
     */
    public List<String> getFormItemNames() {
        final List<String> keyList = new ArrayList<String>();
        for (final String key : _formItems.keySet()) {
            keyList.add(key);
        }
        return keyList;
    }

    /**
     * Retrieve a file item map.
     *
     * @return The file items as a map.
     */
    public Map<String, FileItem> getFileItem() {
        return _fileItem;
    }
}
