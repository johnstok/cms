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
 *
 */
public final class App extends CccApp {
    private static final Logger LOG = Logger.getLogger(App.class);
    private static LegacyDBQueries legacyDBQueries;
    private static ServiceLookup services;
    private static Options _options;

    private App() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        _options  = parseOptions(args, Options.class);

        login(_options._username, _options._password);

        services = new ServiceLookup(_options._app);

        connectToLegacySystem();

        performMigration();

        logout();

        report("Migration finished in ");
    }

    private static void connectToLegacySystem() {
        final DataSource legacyConnection =
            getOracleDatasource(
                _options._legConString,
                _options._legUsername,
                _options._legPassword);
        legacyDBQueries = new LegacyDBQueries(new DbUtilsDB(legacyConnection));
        LOG.info("Connected to legacy DB.");
    }

    private static void performMigration() {
        final Migrations migrations =
            new Migrations(
                legacyDBQueries,
                "/"+_options._app+"/",
                services.lookupCommands(),
                services.lookupQueries(),
                new FileUploader(
                    _options._ccURL,
                    _options._username,
                    _options._password)
            );
        migrations.migrate();
    }

    static class Options {
        @Option(
            name="-u", required=true, usage="Username for connecting to CCC.")
        String _username;

        @Option(
            name="-p", required=true, usage="Password for connecting to CCC.")
        String _password;

        @Option(
            name="-a", required=true, usage="App name.")
        String _app;

        @Option(
            name="-cu", required=true, usage="Content-creator URL.")
        String _ccURL;

        @Option(
            name="-lu", required=true, usage="Username for legacy DB.")
        String _legUsername;

        @Option(
            name="-lp", required=true, usage="Password for legacy DB.")
        String _legPassword;

        @Option(
            name="-lc", required=true, usage="Connection string for legacy DB.")
        String _legConString;
    }
}

