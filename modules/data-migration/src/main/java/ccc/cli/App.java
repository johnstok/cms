package ccc.cli;


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ccc.migration.DbUtilsDB;
import ccc.migration.FileUploader;
import ccc.migration.LegacyDBQueries;
import ccc.migration.Migrations;
import ccc.services.ServiceLookup;

/**
 * Entry class for the migration application.
 *
 */
public final class App extends CccApp {
    private static final Logger LOG = Logger.getLogger(App.class);
    private static final String USERNAME = "migration";
    private static final String PASSWORD = "migration";
    private static Properties props = new Properties();
    private static LegacyDBQueries legacyDBQueries;
    private static ServiceLookup services;

    private App() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        loadSettings(props, "migration.properties");

        services = new ServiceLookup(props.getProperty("app-name"));

        login(USERNAME, PASSWORD);

        connectToLegacySystem();

        performMigration();

        logout();

        report("Migration finished in ");
    }

    private static void connectToLegacySystem() {
        final DataSource legacyConnection = getOracleDatasource(props);
        legacyDBQueries = new LegacyDBQueries(new DbUtilsDB(legacyConnection));
        LOG.info("Connected to legacy DB.");
    }

    private static void performMigration() {
        final Migrations migrations =
            new Migrations(
                legacyDBQueries,
                props,
                services.lookupCommands(),
                services.lookupQueries(),
                new FileUploader(
                    props.getProperty("targetUploadURL"),
                    props.getProperty("targetApplicationURL"),
                    USERNAME,
                    PASSWORD)
            );
        migrations.createDefaultFolderStructure();
        migrations.migrate();
    }
}
