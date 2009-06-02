package ccc.cli;


import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.Scheduler;
import ccc.migration.ServiceLookup;

/**
 * Command line management of CCC7 search engine.
 */
public final class Search extends CccApp {
    private static final Logger LOG = Logger.getLogger(Search.class);

    private static Options options;
    private static ServiceLookup services;

    private Search() { super(); }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        options  = parseOptions(args, Options.class);

        login(options._username, options._password);

        services = new ServiceLookup(options._app);

        final Scheduler s = services.lookupSearchScheduler();

        if ("start".equals(options._action)) {
            s.start();
            LOG.info("Started.");

        } else if ("stop".equals(options._action)) {
            s.stop();
            LOG.info("Stopped.");

        } else if ("running".equals(options._action)) {
            final boolean running = s.isRunning();
            LOG.info("Running: "+running+".");

        } else {
            LOG.error("Invalid command.");
        }

        logout();

        report("Finished in ");
    }

    /**
     * Options for the search scheduler tool.
     *
     * @author Civic Computing Ltd.
     */
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
            name="-c", required=true, usage="Action.")
        String _action;
    }
}
