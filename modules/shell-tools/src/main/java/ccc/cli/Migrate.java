/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;


import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.client1.JaxrsServiceLocator;
import ccc.migration.DbUtilsDB;
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
    private static JaxrsServiceLocator sl;
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

        services =
            new ServiceLookup(options.getApp(), options.getProviderURL());
        sl = new JaxrsServiceLocator(options._ccURL);

        login(options.getUsername(), options.getPassword());
        sl.getSecurity().login(options.getUsername(), options.getPassword());

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
        // TODO: pass options instead of 8 parameters?
        final Migrations migrations =
            new Migrations(
                legacyDBQueries,
                services.getResources(),
                services.getPages(),
                services.getFolders(),
                services.getUsers(),
                sl.getFileUploader(),
                services.getTemplates(),
                options);
        migrations.migrate();
    }

    /**
     * Options for the migration tool.
     *
     * @author Civic Computing Ltd.
     */
    public static class Options {
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
            usage="Optional JNDI provider URL, defaults to localhost")
            private String _providerURL;

        @Option(
            name="-hp",
            required=false,
            usage="Optional homepage migration (post CCC 6.4).")
        private boolean _migrateHomepage;

        @Option(
            name="-mj",
            required=false,
            usage="Optional migration c3_content.is_major_edit. Requires -v.")
            private boolean _migrateIsMajorEdit;

        @Option(
            name="-v",
            required=false,
            usage="Optional migration of paragraph versions.")
            private boolean _migrateVersions;

        @Option(
            name="-ignore",
            required=false,
            usage="Optional path to ignore in the migration")
            private String _ignorePaths;

        /**
         * Accessor.
         *
         * @return Returns the username.
         */
        public String getUsername() {
            return _username;
        }


        /**
         * Accessor.
         *
         * @return Returns the password.
         */
        public String getPassword() {
            return _password;
        }


        /**
         * Accessor.
         *
         * @return Returns the app.
         */
        public String getApp() {
            return _app;
        }


        /**
         * Accessor.
         *
         * @return Returns the ccURL.
         */
        public String getCcURL() {
            return _ccURL;
        }


        /**
         * Accessor.
         *
         * @return Returns the legUsername.
         */
        public String getLegUsername() {
            return _legUsername;
        }


        /**
         * Accessor.
         *
         * @return Returns the legPassword.
         */
        public String getLegPassword() {
            return _legPassword;
        }


        /**
         * Accessor.
         *
         * @return Returns the legConString.
         */
        public String getLegConString() {
            return _legConString;
        }


        /**
         * Accessor.
         *
         * @return Returns the JNDI provider URL.
         */
        public String getProviderURL() {
            return _providerURL;
        }

        /**
         * Accessor.
         *
         * @return Returns the migrateHomepage.
         */
        public boolean isMigrateHomepage() {
            return _migrateHomepage;
        }

        /**
         * Accessor.
         *
         * @return Returns the migrateIsMajorEdit.
         */
        public boolean isMigrateIsMajorEdit() {
            return _migrateIsMajorEdit;
        }

        /**
         * Accessor.
         *
         * @return Returns the migrateVersions.
         */
        public boolean isMigrateVersions() {
            return _migrateVersions;
        }

        /**
         * Accessor.
         *
         * @return Returns the JNDI provider URL.
         */
        public String getIgnorePaths() {
            return _ignorePaths;
        }
    }
}

