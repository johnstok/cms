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

import ccc.api.exceptions.RestException;
import ccc.api.types.ResourcePath;
import ccc.cli.fileupload.CccServer;
import ccc.cli.fileupload.Server;
import ccc.client.http.ProxyServiceLocator;



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

    private static Server        server;

    private static void recurse(final UUID parentId,
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
                        recurse(childFolder, child, includeHidden, publish);
                    } catch (final RestException e) {
                        LOG.warn(
                            "Failed to create folder '"+child.getName()
                            + "' [error code: "+e.getCode()+"].");
                    }
                }
            }
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command line arguments.
     * @throws IOException If the local folder cannot be opened.
     */
    public static void main(final String[] args) throws IOException {
        LOG.info("Starting.");

        final Options o = parseOptions(args, Options.class);

        final ProxyServiceLocator sl =
            new ProxyServiceLocator(o._uploadUrl);

        sl.getSecurity().login(o.getUsername(), o.getPassword());

        server = new CccServer(
            new ResourcePath(o.getRemotePath()),
            sl.getFileUploader(),
            sl.getFolders(),
            sl.getResources());

        try {
            recurse(
                server.getRoot(),
                new File(o.getLocalPath()).getCanonicalFile(),
                o.isIncludeHidden(),
                o.isPublish());
        } catch (final RestException e) {
            System.err.print("Root folder does not exist.");
        }

        report("Upload finished in ");
    }


    /**
     * Options for the file upload tool.
     *
     * @author Civic Computing Ltd.
     */
    static class Options {
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
            name="-a", required=true, usage="The name of the application.")
        private String _appName;

        @Option(
            name="-o", required=true, usage="The URL for file upload.")
        private String _uploadUrl;

        @Option(
            name="-h", required=false, usage="Include hidden files/folders.")
        private boolean _includeHidden;

        @Option(
            name="-b", required=false, usage="Publish uploaded files/folders.")
        private boolean _publish;

        @Option(
            name="-jn",
            required=false,
            usage="optional JNDI provider URL, defaults to localhost")
            private String _providerURL;


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
         * @return Returns the JNDI provider URL.
         */
        String getProviderURL() {
            return _providerURL;
        }



        /**
         * Accessor.
         *
         * @return Returns the appName.
         */
        public final String getAppName() {
            return _appName;
        }



        /**
         * Accessor.
         *
         * @return Returns the uploadUrl.
         */
        public final String getUploadUrl() {
            return _uploadUrl;
        }
    }
}
