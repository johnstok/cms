package ccc.cli;



import static ccc.types.PredefinedResourceNames.*;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.migration.ServiceLookup;
import ccc.rest.CommandFailedException;
import ccc.rest.Commands;
import ccc.rest.FolderCommands;
import ccc.rest.dto.ResourceSummary;

/**
 * Entry class for the 'create' application.
 */
public final class Create extends CccApp {
    private static final Logger LOG = Logger.getLogger(Create.class);

    private static Options options;
    private static ServiceLookup services;

    private Create() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        options  = parseOptions(args, Options.class);

        login(options.getUsername(), options.getPassword());

        services =
            new ServiceLookup(options.getApp(), options.getProviderURL());

        createSchemaStructure();

        logout();

        report("Finished in ");
    }

    private static void createSchemaStructure() {
        try {
            final Commands commands = services.lookupCommands();
            final FolderCommands folderCommands = services.lookupFolderCommands();

            final ResourceSummary assets = folderCommands.createRoot(ASSETS);
            final ResourceSummary content = folderCommands.createRoot(CONTENT);

            folderCommands.createFolder(assets.getId(), TEMPLATES);
            folderCommands.createFolder(assets.getId(), CSS);
            folderCommands.createFolder(assets.getId(), IMAGES);

            folderCommands.createFolder(content.getId(), FILES);
            folderCommands.createFolder(content.getId(), IMAGES);
            commands.createSearch(content.getId(), "search");

            // TODO: Remove. Should set 'publish' root via UI
            commands.lock(content.getId());
            commands.publish(content.getId());
            commands.unlock(content.getId());
            commands.lock(assets.getId());
            commands.publish(assets.getId());
            commands.unlock(assets.getId());

            LOG.info("Created default folder structure.");
        } catch (final CommandFailedException e) {
            LOG.error("Failed to create app.", e);
        }
    }

    /**
     * Options for the default layout tool.
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
            name="-a", required=true, usage="App name.")
        private String _app;

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
         * @return Returns the app.
         */
        String getApp() {
            return _app;
        }


        /**
         * Accessor.
         *
         * @return Returns the JNDI provider URL.
         */
        String getProviderURL() {
            return _providerURL;
        }
    }
}
