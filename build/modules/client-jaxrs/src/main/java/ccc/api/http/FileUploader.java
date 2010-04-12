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
package ccc.api.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

import ccc.api.types.ResourceName;


/**
 * Helper class to upload files to the new system.
 *
 * @author Civic Computing Ltd.
 */
class FileUploader
    implements
        IFileUploader {

    /** CONNECTION_TIMEOUT : int. */
    private static final int CONNECTION_TIMEOUT = 5000;

    private static final Logger LOG = Logger.getLogger(FileUploader.class);

    private final HttpClient _client;
    private final String _targetUploadURL;
    private MimetypesFileTypeMap _mimemap;

    /**
     * Constructor.
     */
    public FileUploader(final HttpClient httpClient,
                        final String targetUploadURL) {
        _targetUploadURL = targetUploadURL;
        _client          = httpClient;

        final InputStream mimes =
            Thread.currentThread().
            getContextClassLoader().
            getResourceAsStream("ccc7mime.types");
        _mimemap = new MimetypesFileTypeMap(mimes);
    }

    /** {@inheritDoc} */
    public void uploadFile(final UUID parentId,
                    final String fileName,
                    final String originalTitle,
                    final String originalDescription,
                    final Date originalLastUpdate,
                    final File file,
                    final boolean publish) {
        try {
            if (file.length() < 1) {
                LOG.warn("Zero length file : "+fileName);
                return;
            }

            final PostMethod filePost =
                new PostMethod(_targetUploadURL);
            LOG.debug("Migrating file: "+fileName);
            final String name =
                ResourceName.escape(fileName).toString();

            final String title =
                (null!=originalTitle) ? originalTitle : fileName;

            final String description =
                (null!=originalDescription) ? originalDescription : "";

            final String lastUpdate;
            if (originalLastUpdate == null) {
                lastUpdate = ""+new Date().getTime();
            } else {
                lastUpdate = ""+originalLastUpdate.getTime();
            }


            final FilePart fp = new FilePart("file", file.getName(), file);
            fp.setContentType(_mimemap.getContentType(file));
            final Part[] parts = {
                    new StringPart("fileName", name),
                    new StringPart("title", title),
                    new StringPart("description", description),
                    new StringPart("lastUpdate", lastUpdate),
                    new StringPart("path", parentId.toString()),
                    new StringPart("publish", String.valueOf(publish)),
                    fp
            };
            filePost.setRequestEntity(
                new MultipartRequestEntity(parts, filePost.getParams())
            );

            _client.getHttpConnectionManager().
                getParams().setConnectionTimeout(CONNECTION_TIMEOUT);


            final int status = _client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                LOG.debug(
                    "Upload complete, response="
                    + filePost.getResponseBodyAsString());
            } else {
                throw new RuntimeException(
                    "Error returned by server: "+status+"\n"
                    + filePost.getResponseBodyAsString());
            }

        } catch (final RuntimeException e) {
            LOG.error("Upload failed: "+file.getAbsolutePath(), e);
        } catch (final IOException e) {
            LOG.error("Upload failed: "+file.getAbsolutePath(), e);
        }
    }


    /** {@inheritDoc} */
    public void uploadFile(final UUID parentId,
                           final String fileName,
                           final String title,
                           final String description,
                           final Date lastUpdate,
                           final String directory) {

        final String path = directory+fileName;
        final File file = new File(path);

        if (!file.exists()) {
            LOG.warn("File not found: "+path);
        } else {
            uploadFile(
                parentId,
                fileName,
                title,
                description,
                lastUpdate,
                file,
                false);
        }
    }
}
