package ccc.cli;


import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.domain.Scheduler;
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

        login(options.getUsername(), options.getPassword());

        services = new ServiceLookup(options.getApp(), options._providerURL);

        final Scheduler s = services.lookupSearchScheduler();

        if ("start".equals(options.getAction())) {
            s.start();
            LOG.info("Started.");

        } else if ("stop".equals(options.getAction())) {
            s.stop();
            LOG.info("Stopped.");

        } else if ("running".equals(options.getAction())) {
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
        private String _username;

        @Option(
            name="-p", required=true, usage="Password for connecting to CCC.")
        private String _password;

        @Option(
            name="-a", required=true, usage="App name.")
        private String _app;

        @Option(
            name="-c", required=true, usage="Action.")
        private String _action;

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
         * @return Returns the action.
         */
        String getAction() {
            return _action;
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
