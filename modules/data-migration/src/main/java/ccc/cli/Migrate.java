package ccc.cli;


import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.migration.DbUtilsDB;
import ccc.migration.FileUploader;
import ccc.migration.LegacyDBQueries;
import ccc.migration.Migrations;
import ccc.migration.ServiceLookup;

/**
 * Entry class for the migration application.
 */
public final class Migrate extends CccApp {
    private static final Logger LOG = Logger.getLogger(Migrate.class);
    private static LegacyDBQueries legacyDBQueries;
    private static ServiceLookup services;
    private static Options options;

    private Migrate() { /* NO-OP */ }


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

        connectToLegacySystem();

        performMigration();

        logout();

        report("Migration finished in ");
    }

    private static void connectToLegacySystem() {
        final DataSource legacyConnection =
            getOracleDatasource(
                options.getLegConString(),
                options.getLegUsername(),
                options.getLegPassword());
        legacyDBQueries = new LegacyDBQueries(new DbUtilsDB(legacyConnection));
        LOG.info("Connected to legacy DB.");
    }

    private static void performMigration() {
        final Migrations migrations =
            new Migrations(
                legacyDBQueries,
                "/"+options.getApp()+"/",
                services.lookupCommands(),
                services.lookupQueries(),
                new FileUploader(
                    options.getCcURL(),
                    options.getUsername(),
                    options.getPassword())
            );
        migrations.migrate();
    }

    /**
     * Options for the migration tool.
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
            name="-cu", required=true, usage="Content-creator URL.")
        private String _ccURL;

        @Option(
            name="-lu", required=true, usage="Username for legacy DB.")
        private String _legUsername;

        @Option(
            name="-lp", required=true, usage="Password for legacy DB.")
        private String _legPassword;

        @Option(
            name="-lc", required=true, usage="Connection string for legacy DB.")
        private String _legConString;

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
         * @return Returns the ccURL.
         */
        String getCcURL() {
            return _ccURL;
        }


        /**
         * Accessor.
         *
         * @return Returns the legUsername.
         */
        String getLegUsername() {
            return _legUsername;
        }


        /**
         * Accessor.
         *
         * @return Returns the legPassword.
         */
        String getLegPassword() {
            return _legPassword;
        }


        /**
         * Accessor.
         *
         * @return Returns the legConString.
         */
        String getLegConString() {
            return _legConString;
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

