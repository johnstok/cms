package ccc.cli;



import java.util.Properties;

import org.apache.log4j.Logger;

import ccc.migration.Migrations;

/**
 * Entry class for the 'create' application.
 */
public final class Create extends CccApp {
    private static final Logger LOG = Logger.getLogger(Create.class);
    private static Properties props = new Properties();
    private static ServiceLookup services;

    private Create() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        services = new ServiceLookup(args[0]);

        login(args[1], args[2]);

        createSchemaStructure();

        logout();

        report("Finished in ");
    }

    private static void createSchemaStructure() {
        final Migrations migrations =
            new Migrations(
                null,
                props,
                services.lookupCommands(),
                services.lookupQueries(),
                null);
        migrations.createDefaultFolderStructure();
    }
}
