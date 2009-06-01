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
package ccc.cli;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.Queries;
import ccc.cli.fileupload.CccServer;
import ccc.cli.fileupload.Server;
import ccc.domain.ResourcePath;
import ccc.migration.FileUploader;
import ccc.migration.ServiceLookup;



/**
 * A command line tool that allows bulk upload of files into CCC.
 *
 * TODO: Report failure to create a file (currently silently ignored).
 * TODO: Add the ability to overwrite/ignore existing files/folders.
 *
 * @author Civic Computing Ltd.
 */
public class FileUpload extends CccApp {
    private static final Logger LOG = Logger.getLogger(FileUpload.class);

    private static Server        server;
    private static ServiceLookup services;
    private static Properties    props = new Properties();

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
                    } catch (final CommandFailedException e) {
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

        loadSettings(props, "migration.properties");

        services = new ServiceLookup(props.getProperty("app-name"));

        final Queries queries = services.lookupQueries();
        final Commands commands = services.lookupCommands();

        login(o._username, o._password);

        server = new CccServer(
            new ResourcePath(o._remotePath),
            new FileUploader(
                props.getProperty("targetApplicationURL"),
                o._username,
                o._password),
            commands,
            queries);

        recurse(
            server.getRoot(),
            new File(o._localPath).getCanonicalFile(),
            o._includeHidden,
            o._publish);

        logout();

        report("Migration finished in ");
    }


    private static class Options {
        @Option(
            name="-u", required=true, usage="Username for connecting to CCC.")
        String _username;

        @Option(
            name="-p", required=true, usage="Password for connecting to CCC.")
        String _password;

        @Option(
            name="-r", required=true, usage="Remote folder path.")
        String _remotePath;

        @Option(
            name="-l", required=true, usage="Local folder path.")
        String _localPath;

        @Option(
            name="-h", required=false, usage="Include hidden files\folders.")
        boolean _includeHidden;

        @Option(
            name="-b", required=false, usage="Publish uploaded files\folders.")
        boolean _publish;
    }
}
