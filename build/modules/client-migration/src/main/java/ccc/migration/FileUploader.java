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
package ccc.migration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

import ccc.api.dto.ResourceSummary;
import ccc.api.jaxrs.providers.ResSummaryReader;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.api.types.DBC;
import ccc.api.types.ResourceName;


/**
 * Implementation of the file uploader API.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploader
    implements
        IFileUploader {

    private static final int CONNECTION_TIMEOUT = 5000;
    private static final Logger LOG = Logger.getLogger(FileUploader.class);

    private final HttpClient _client;
    private final String _filesUrl;
    private MimetypesFileTypeMap _mimemap;


    /**
     * Constructor.
     *
     * @param httpClient
     * @param hostUrl
     */
    public FileUploader(final HttpClient httpClient,
                        final String hostUrl) {
        DBC.require().notEmpty(hostUrl);
        _filesUrl = hostUrl+"/ccc/api/secure/files/bin";
        _client   = httpClient;

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

            final PartSource ps = new FilePartSource(file.getName(), file);
            uploadFile(
                _filesUrl,
                parentId,
                fileName,
                originalTitle,
                originalDescription,
                originalLastUpdate,
                ps,
                _mimemap.getContentType(file),
                null,
                publish);

        } catch (final RuntimeException e) {
            LOG.error("Upload failed: "+file.getAbsolutePath(), e);
        } catch (final IOException e) {
            LOG.error("Upload failed: "+file.getAbsolutePath(), e);
        }
    }


    private ResourceSummary uploadFile(
                           final String hostPath,
                           final UUID parentId,
                           final String fileName,
                           final String originalTitle,
                           final String originalDescription,
                           final Date originalLastUpdate,
                           final PartSource ps,
                           final String contentType,
                           final String charset,
                           final boolean publish) throws IOException {

            final PostMethod filePost = new PostMethod(hostPath);

            try {
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

                final FilePart fp = new FilePart("file", ps);
                fp.setContentType(contentType);
                fp.setCharSet(charset);
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

                _client
                    .getHttpConnectionManager()
                    .getParams()
                    .setConnectionTimeout(CONNECTION_TIMEOUT);


                final int status = _client.executeMethod(filePost);
                if (status == HttpStatus.SC_OK) {
                    final String entity = filePost.getResponseBodyAsString();
                    LOG.debug("Upload complete, response=" + entity);

                    return
                        new ResSummaryReader().readFrom(
                            ResourceSummary.class,
                            ResourceSummary.class,
                            null,
                            MediaType.valueOf(
                                filePost
                                    .getResponseHeader("Content-Type")
                                    .getValue()),
                            null,
                            filePost.getResponseBodyAsStream());
                }

                throw new RestExceptionMapper().fromResponse(
                        filePost.getResponseBodyAsString());

            } finally {
                filePost.releaseConnection();
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
