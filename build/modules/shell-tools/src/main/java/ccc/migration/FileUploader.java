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
package ccc.migration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

import ccc.domain.ResourceName;


/**
 * Helper class to upload files to the new system.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploader {
    /** CONNECTION_TIMEOUT : int. */
    private static final int CONNECTION_TIMEOUT = 5000;

    private static Logger log = Logger.getLogger(FileUploader.class);

    private final HttpClient _client = new HttpClient();
    private final String _targetUploadURL;
    private final String _appURL;
    private final String _username;
    private final String _password;
    private MimetypesFileTypeMap _mimemap;

    /**
     * Constructor.
     *
     * @param appURL The url to upload to.
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    public FileUploader(final String appURL,
                        final String username,
                        final String password) {
        _appURL = appURL;
        _targetUploadURL = appURL+"/upload";
        _username = username;
        _password = password;

        final InputStream mimes =
            Thread.currentThread().
            getContextClassLoader().
            getResourceAsStream("ccc7mime.types");
        _mimemap = new MimetypesFileTypeMap(mimes);

        authenticateForUpload();
    }

    /**
     * Authenticate using login form.
     *
     * @param client
     */
    void authenticateForUpload() {

        final GetMethod get =
            new GetMethod(_appURL+"/upload");
        try {
            _client.executeMethod(get);
        } catch (final Exception e) {
            log.error("initial get method failed ", e);
        }
        get.releaseConnection();

        final PostMethod authpost = new PostMethod(_appURL+"/j_security_check");

        final NameValuePair userid   =
            new NameValuePair("j_username", _username);
        final NameValuePair password =
            new NameValuePair("j_password", _password);
        authpost.setRequestBody(
            new NameValuePair[] {userid, password});

        try {
            final int status = _client.executeMethod(authpost);
            log.debug("Authenticate response code: "+status);
        } catch (final IOException e) {
            log.error("Authentication failed ", e);
        }
        authpost.releaseConnection();

        /* in order to prevent 'not a multi-part
         * post error' for the first upload      */
        try {
            _client.executeMethod(get);
        } catch (final Exception e) {
            log.error("get method failed ", e);
        }
        get.releaseConnection();
    }

    /**
     * Upload a file.
     *
     * @param parentId The folder in which the file should be uploaded.
     * @param fileName The name of the file.
     * @param originalTitle The title of the file.
     * @param originalDescription The file's description.
     * @param originalLastUpdate The file's last update date.
     * @param file The local file reference.
     * @param publish Should the file be published.
     */
    public void uploadFile(final UUID parentId,
                    final String fileName,
                    final String originalTitle,
                    final String originalDescription,
                    final Date originalLastUpdate,
                    final File file,
                    final boolean publish) {
        try {
            if (file.length() < 1) {
                log.warn("Zero length file : "+fileName);
                return;
            }

            final PostMethod filePost =
                new PostMethod(_targetUploadURL);
            log.debug("Migrating file: "+fileName);
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
                log.debug(
                    "Upload complete, response="
                    + filePost.getResponseBodyAsString()
                );
            } else {
                log.error(
                    "Upload failed, response="
                    + status+", "
                    + HttpStatus.getStatusText(status)
                );
            }
        } catch (final RuntimeException e) {
            log.error("File migration failed ", e);
        } catch (final IOException e) {
            log.error("File migration failed ", e);
        }
    }


    /**
     * Upload a file.
     *
     * @param parentId The folder in which the file should be uploaded.
     * @param fileName The name of the file.
     * @param title The title of the file.
     * @param lastUpdate The last update of the file.
     * @param description The file's description.
     * @param directory The directory that the local file is stored.
     */
    void uploadFile(final UUID parentId,
                    final String fileName,
                    final String title,
                    final String description,
                    final Date lastUpdate,
                    final String directory
                    ) {

        final File file = new File(directory+fileName);
        if (!file.exists()) {
            log.debug("File not found: "+fileName);
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
