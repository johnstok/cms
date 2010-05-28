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
package ccc.plugins.multipart.apache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import ccc.api.types.DBC;
import ccc.commons.Resources;
import ccc.plugins.multipart.MultipartFormData;


/**
 * A multi-part form implemented via Apache commons file-upload.
 *
 * @author Civic Computing Ltd.
 */
public class MultipartForm implements MultipartFormData {

    private static final Properties PROPS =
        Resources.readIntoProps("uploadConfig.properties");

    private final Map<String, FileItem> _formItems =
        new HashMap<String, FileItem>();
    private final Map<String, FileItem> _files =
        new HashMap<String, FileItem>();


    /**
     * Constructor.
     *
     * @param items The list of items on this form.
     */
    public MultipartForm(final List<FileItem> items) {
        DBC.require().notNull(items);

        for (final FileItem item : items) {
            final String key = item.getFieldName();

            if (item.isFormField()) {
                addItem(_formItems, key, item);
            } else {
                addItem(_files, key, item);
            }
        }
        /*
         *  #430: IE 6 and IE 7 send two fields with same name. One is with
         *  content type 'file' and the other one is the path of the original
         *  file location.
         */
    }


    /**
     * Constructor.
     *
     * @param charEncoding The character encoding of the input stream.
     * @param contentLength The number of bytes on the input stream.
     * @param contentType The input stream's media type.
     * @param inputStream The stream to parse as multipart.
     */
    public MultipartForm(final String charEncoding,
                         final int contentLength,
                         final String contentType,
                         final InputStream inputStream) {
        this(
            parseFileItems(
                new JaxrsRequestContext(
                    charEncoding, contentLength, contentType, inputStream)));
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


    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream(final String string) throws IOException {
        final FileItem item = getFormItem(string);
        return (null==item) ? null : item.getInputStream();
    }


    /** {@inheritDoc} */
    @Override
    public String getContentType(final String string) {
        final FileItem item = getFormItem(string);
        return (null==item) ? null : item.getContentType();
    }


    /** {@inheritDoc} */
    @Override
    public long getSize(final String string) {
        final FileItem item = getFormItem(string);
        return (null==item) ? -1 : item.getSize();
    }


    /** {@inheritDoc} */
    @Override
    public String getString(final String string) {
        final FileItem item = getFormItem(string);
        return (null==item) ? null : item.getString();
    }


    private void addItem(final Map<String, FileItem> items,
                         final String key,
                         final FileItem item) {
        if (items.containsKey(key)) {
            throw new RuntimeException("Duplicate field on form: "+key);
        }
        items.put(key, item);
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

        DBC.require().notNull(context);

        // Check that we have a file upload request
        final boolean isMultipart =
            FileUploadBase.isMultipartContent(context);
        if (!isMultipart) {
            throw new RuntimeException("Not a multipart.");
        }

        // Create a factory for disk-based file items
        final DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set factory constraints
        factory.setSizeThreshold(maxInMemorySize());

        // Create a new file upload handler
        final FileUpload upload = new FileUpload(factory);

        // Set overall request size constraint
        upload.setFileSizeMax(maxFileSize());

        try {
            return upload.parseRequest(context);
        } catch (final FileUploadException e) {
            throw new RuntimeException("Failed to parse multipart request.", e);
        }
    }


    /**
     * Get the file item for a key.
     * <p>If both a file & simple value exists the file will be returned.
     *
     * @param key The key to the file item.
     *
     * @return The corresponding file item or NULL if it doesn't exist.
     */
    private FileItem getFormItem(final String key) {
        final FileItem item = _files.get(key);
        if (null!=item) { return item; }
        return _formItems.get(key);
    }

    static int maxInMemorySize() {
        final int defaultValue = 500*1024; // 500kb
        final String propValue = PROPS.getProperty("max-in-memory");
        try {
            return DBC.ensure().greaterThan(
                -1, Integer.valueOf(propValue).intValue());
        } catch (final RuntimeException e) {
            return defaultValue;
        }
    }

    static int maxFileSize() {
        final int defaultValue = 32*1024*1024; //  32mb
        final String propValue = PROPS.getProperty("maxFileSize");
        try {
            return DBC.ensure().greaterThan(
                0, Integer.valueOf(propValue).intValue());
        } catch (final RuntimeException e) {
            return defaultValue;
        }
    }
}
