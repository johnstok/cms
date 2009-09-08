package ccc.cli;



import static ccc.types.PredefinedResourceNames.*;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.migration.ServiceLookup;
import ccc.rest.RestException;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.ResourcesExt;

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
            final ResourcesExt resourcesExt = services.lookupCommands();
            final FoldersExt foldersExt = services.lookupFolderCommands();

            final ResourceSummary assets = foldersExt.createRoot(ASSETS);
            final ResourceSummary content = foldersExt.createRoot(CONTENT);

            foldersExt.createFolder(new FolderDto(assets.getId(), TEMPLATES));
            foldersExt.createFolder(new FolderDto(assets.getId(), CSS));
            foldersExt.createFolder(new FolderDto(assets.getId(), IMAGES));

            foldersExt.createFolder(new FolderDto(content.getId(), FILES));
            foldersExt.createFolder(new FolderDto(content.getId(), IMAGES));
            resourcesExt.createSearch(content.getId(), "search");

            // TODO: Remove. Should set 'publish' root via UI
            resourcesExt.lock(content.getId());
            resourcesExt.publish(content.getId());
            resourcesExt.unlock(content.getId());
            resourcesExt.lock(assets.getId());
            resourcesExt.publish(assets.getId());
            resourcesExt.unlock(assets.getId());

            LOG.info("Created default folder structure.");
        } catch (final RestException e) {
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
