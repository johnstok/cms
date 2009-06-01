package ccc.cli;


import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.Scheduler;
import ccc.migration.ServiceLookup;

/**
 * Command line management of Action executor.
 */
public final class Scheduling extends CccApp {
    private static final Logger LOG = Logger.getLogger(Scheduling.class);

    private static Options _options;
    private static ServiceLookup services;

    private Scheduling() { super(); }


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

        final Scheduler s = services.lookupActionScheduler();

        if ("start".equals(_options._action)) {
            s.start();
            LOG.info("Started.");

        } else if ("stop".equals(_options._action)) {
            s.stop();
            LOG.info("Stopped.");

        } else if ("running".equals(_options._action)) {
            final boolean running = s.isRunning();
            LOG.info("Running: "+running+".");

        } else {
            LOG.error("Invalid command.");
        }

        logout();

        report("Finished in ");
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
            name="-c", required=true, usage="Action.")
        String _action;
    }
}
