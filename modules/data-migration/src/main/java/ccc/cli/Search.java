package ccc.cli;


import java.util.Properties;

import org.apache.log4j.Logger;

import ccc.api.Scheduler;
import ccc.services.ServiceLookup;

/**
 * Command line management of CCC7 search engine.
 */
public final class Search extends CccApp {
    private static final Logger LOG = Logger.getLogger(Search.class);
    private static ServiceLookup services;
    private static Properties props = new Properties();
    private Search() { super(); }


    /**
     * Entry point for this application.
     * TODO: Supply app_name, username, password from the console.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");
        loadSettings(props, "migration.properties");
        login("super", "sup3r2008");

        services = new ServiceLookup(props.getProperty("app-name"));

        final Scheduler s = services.lookupSearchScheduler();

        if (1 != args.length) {
            LOG.error("Wrong number of arguments.");
        } else {
            if ("start".equals(args[0])) {
                s.start();
                LOG.info("Started.");

            } else if ("stop".equals(args[0])) {
                s.stop();
                LOG.info("Stopped.");

            } else if ("running".equals(args[0])) {
                final boolean running = s.isRunning();
                LOG.info("Running: "+running+".");

            } else {
                LOG.error("Invalid command.");
            }
        }

        logout();

        report("Finished in ");
    }
}
