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
package ccc.api.client1;

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

import ccc.types.ResourceName;


/**
 * Helper class to upload files to the new system.
 *
 * @author Civic Computing Ltd.
 */
class FileUploader implements IFileUploader {

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
                LOG.error(
                    "Upload failed for "+file.getAbsolutePath()+" - "
                    + status+"\n"
                    + filePost.getResponseBodyAsString());
            }
        } catch (final RuntimeException e) {
            LOG.error("File migration failed ", e);
        } catch (final IOException e) {
            LOG.error("File migration failed ", e);
        }
    }


    /** {@inheritDoc} */
    public void uploadFile(final UUID parentId,
                    final String fileName,
                    final String title,
                    final String description,
                    final Date lastUpdate,
                    final String directory
                    ) {

        final String path = directory+fileName;
        final File file = new File(path);
        if (!file.exists()) {
            LOG.warn("File not found: "+path);
        } else {
            uploadFile(parentId,
                fileName,
                title,
                description,
                lastUpdate,
                file,
                false);
        }
    }
}
