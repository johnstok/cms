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
package ccc.cli;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.exceptions.CCException;
import ccc.api.http.ProxyServiceLocator;
import ccc.api.types.ResourcePath;
import ccc.cli.fileupload.CccServer;
import ccc.cli.fileupload.Server;



/**
 * A command line tool that allows bulk upload of files into CCC.
 *
 * TODO: Report failure to create a file (currently silently ignored).
 * TODO: Add the ability to overwrite existing files.
 *
 * @author Civic Computing Ltd.
 */
public class FileUpload extends CccApp {
    private static final Logger LOG = Logger.getLogger(FileUpload.class);

    @Option(
        name="-u", required=true, usage="Username for connecting to CCC.")
    private String _username;

    @Option(
        name="-p", required=true, usage="Password for connecting to CCC.")
    private String _password;

    @Option(
        name="-r", required=true, usage="Remote folder path.")
    private String _remotePath;

    @Option(
        name="-l", required=true, usage="Local folder path.")
    private String _localPath;

    @Option(
        name="-o", required=true, usage="The URL for file upload.")
    private String _uploadUrl;

    @Option(
        name="-h", required=false, usage="Include hidden files/folders.")
    private boolean _includeHidden;

    @Option(
        name="-b", required=false, usage="Publish uploaded files/folders.")
    private boolean _publish;


    /**
     * Application entry point.
     *
     * @param args Command line arguments.
     * @throws IOException If the local folder cannot be opened.
     */
    public static void main(final String[] args) throws IOException {
        LOG.info("Starting.");
        final FileUpload o = parseOptions(args, FileUpload.class);

        try {
            o.run();
        } catch (final RuntimeException e) {
            LOG.error("Error performing command.", e);
            System.exit(1);
        }
    }

    public void run() throws IOException {

        final ProxyServiceLocator sl =
            new ProxyServiceLocator(getUploadUrl());

        sl.getSecurity().login(getUsername(), getPassword());

        final Server server = new CccServer(
            new ResourcePath(getRemotePath()),
            sl.getFiles(),
            sl.getFolders(),
            sl.getResources());

        final File localFile = new File(getLocalPath()).getCanonicalFile();
        LOG.info("Uploading from "+localFile.getAbsolutePath());

        recurse(
            server,
            server.getRoot(),
            localFile,
            isIncludeHidden(),
            isPublish());

        report("Upload finished in ");
    }


    private void recurse(final Server server,
                         final UUID parentId,
                         final File localFolder,
                         final boolean includeHidden,
                         final boolean publish) {
        for (final File child : localFolder.listFiles()) {
            if (child.isHidden()&&!includeHidden) {
                LOG.warn("Ignored hidden file: '"+child.getAbsolutePath()+"'.");
            } else {
                if (child.isFile()) {
                    server.createFile(parentId, child, publish);
                } else {
                    try {
                        final UUID childFolder = server.createFolder(
                            parentId, child.getName(), publish);
                        recurse(server, childFolder, child, includeHidden, publish);
                    } catch (final CCException e) {
                        LOG.warn(
                            "Failed to create folder '"
                            + child.getName()
                            + ": "
                            + e.getMessage());
                    }
                }
            }
        }
    }


    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    String getUsername() {
        return _username;
    }


    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    String getPassword() {
        return _password;
    }


    /**
     * Accessor.
     *
     * @return Returns the remotePath.
     */
    String getRemotePath() {
        return _remotePath;
    }


    /**
     * Accessor.
     *
     * @return Returns the localPath.
     */
    String getLocalPath() {
        return _localPath;
    }


    /**
     * Accessor.
     *
     * @return Returns the includeHidden.
     */
    boolean isIncludeHidden() {
        return _includeHidden;
    }


    /**
     * Accessor.
     *
     * @return Returns the publish.
     */
    boolean isPublish() {
        return _publish;
    }


    /**
     * Accessor.
     *
     * @return Returns the uploadUrl.
     */
    public final String getUploadUrl() {
        return _uploadUrl;
    }


    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }


    /**
     * Mutator.
     *
     * @param password The password to set.
     */
    public void setPassword(final String password) {
        _password = password;
    }


    /**
     * Mutator.
     *
     * @param remotePath The remotePath to set.
     */
    public void setRemotePath(final String remotePath) {
        _remotePath = remotePath;
    }


    /**
     * Mutator.
     *
     * @param localPath The localPath to set.
     */
    public void setLocalPath(final String localPath) {
        _localPath = localPath;
    }


    /**
     * Mutator.
     *
     * @param uploadUrl The uploadUrl to set.
     */
    public void setUploadUrl(final String uploadUrl) {
        _uploadUrl = uploadUrl;
    }


    /**
     * Mutator.
     *
     * @param includeHidden The includeHidden to set.
     */
    public void setIncludeHidden(final boolean includeHidden) {
        _includeHidden = includeHidden;
    }


    /**
     * Mutator.
     *
     * @param publish The publish to set.
     */
    public void setPublish(final boolean publish) {
        _publish = publish;
    }
}
