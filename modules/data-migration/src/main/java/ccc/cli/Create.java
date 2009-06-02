package ccc.cli;



import static ccc.domain.PredefinedResourceNames.*;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.ResourceSummary;
import ccc.migration.ServiceLookup;

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

        services = new ServiceLookup(options.getApp());

        createSchemaStructure();

        logout();

        report("Finished in ");
    }

    private static void createSchemaStructure() {
        try {
            final Commands commands = services.lookupCommands();

            final ResourceSummary assets = commands.createRoot(ASSETS);
            final ResourceSummary content = commands.createRoot(CONTENT);

            commands.createFolder(assets.getId(), TEMPLATES);
            commands.createFolder(assets.getId(), CSS);
            commands.createFolder(assets.getId(), IMAGES);

            commands.createFolder(content.getId(), FILES);
            commands.createFolder(content.getId(), IMAGES);
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
    }
}
