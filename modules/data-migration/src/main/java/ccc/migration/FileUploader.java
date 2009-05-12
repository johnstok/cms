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
import java.io.InputStream;

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
import ccc.services.api.FileDelta;
import ccc.services.api.ResourceSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploader {
    private static Logger log = Logger.getLogger(FileUploader.class);

    private final HttpClient _client = new HttpClient();
    private final String _targetUploadURL;
    private final String _appURL;
    private final String _username;
    private final String _password;
    private MimetypesFileTypeMap _mimemap;

    public FileUploader(final String targetUploadURL,
                 final String appURL,
                 final String username,
                 final String password) {
        _targetUploadURL = targetUploadURL;
        _appURL = appURL;
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
            log.debug(status);
        } catch (final Exception e) {
            log.error("Authentication failed ", e);
        }
        authpost.releaseConnection();

        // in order to prevent 'not a multipart post error' for the first upload
        try {
            _client.executeMethod(get);
        } catch (final Exception e) {
            log.error("get method failed ", e);
        }
        get.releaseConnection();
    }

    void uploadFile(final ResourceSummary filesResource,
                    final String fileName,
                    final FileDelta legacyFile,
                    final File file) {
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
                (null!=legacyFile.getTitle())
                    ? legacyFile.getTitle()
                    : fileName;

            final String description =
                (null!=legacyFile.getDescription())
                    ? legacyFile.getDescription()
                    : "";

            final FilePart fp = new FilePart("file", file.getName(), file);
            fp.setContentType(_mimemap.getContentType(file));
            final Part[] parts = {
                    new StringPart("fileName", name),
                    new StringPart("title", title),
                    new StringPart("description", description),
                    new StringPart("path", filesResource.getId().toString()),
                    fp
            };
            filePost.setRequestEntity(
                new MultipartRequestEntity(parts, filePost.getParams())
            );

            _client.getHttpConnectionManager().
                getParams().setConnectionTimeout(5000);


            final int status = _client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                log.debug(
                    "Upload complete, response="
                    + filePost.getResponseBodyAsString()
                );
            } else {
                log.error(
                    "Upload failed, response="
                    + HttpStatus.getStatusText(status)
                );
            }
        } catch (final Exception e) {
            log.error("File migration failed ", e);
        }
    }


    void uploadFile(final ResourceSummary filesResource,
                    final String fileName,
                    final FileDelta legacyFile,
                    final String directory) {

        final File file = new File(directory+fileName);
        if (!file.exists()) {
            log.debug("File not found: "+fileName);
        } else {
            uploadFile(filesResource, fileName, legacyFile, file);
        }
    }

}
