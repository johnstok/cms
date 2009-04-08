package ccc.cli;


import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.services.Scheduler;
import ccc.services.api.ServiceNames;

/**
 * Command line management of CCC7 search engine.
 */
public final class Search extends CccApp {

    private static final Logger LOG = Logger.getLogger(Search.class);

    private Search() { super(); }


    /**
     * Entry point for this application.
     * TODO: Supply app_name, username, password from the console.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        login("super", "sup3r2008");

        final Scheduler s =
            new JNDI().<Scheduler>get(ServiceNames.SEARCH_SCHEDULER);

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
                System.err.println("Invalid command.");
            }
        }

        logout();

        report("Finished in ");
    }
}
