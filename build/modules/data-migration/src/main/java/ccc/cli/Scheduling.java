package ccc.cli;


import org.apache.log4j.Logger;

import ccc.api.Scheduler;
import ccc.migration.ServiceLookup;

/**
 * Command line management of Action executor.
 */
public final class Scheduling extends CccApp {
    private static final Logger LOG = Logger.getLogger(Scheduling.class);

    private Scheduling() { super(); }


    /**
     * Entry point for this application.
     * TODO: Supply app_name, username, password from the console.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        login("super", "sup3r2008");

        final ServiceLookup services =
            new ServiceLookup("ash");

        final Scheduler s = services.lookupActionScheduler();

        if (1 != args.length) {
            LOG.error("Wrong number of arguments.");
        } else {
            if ("start".equals(args[0])) {
                s.start();

            } else if ("stop".equals(args[0])) {
                s.stop();

            } else {
                LOG.error("Invalid command.");
            }
        }

        logout();

        report("Finished in ");
    }
}
