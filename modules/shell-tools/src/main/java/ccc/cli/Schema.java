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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;

import static ccc.commons.Exceptions.*;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.commons.Resources;


/**
 * Entry class for the schema creation application.
 *
 * @author Civic Computing Ltd.
 */
public class Schema
    extends
        CccApp {

    private static final int    NEW_DB_VERSION = -1;
    private static final Logger LOG = Logger.getLogger(Schema.class);

    @Option(
        name="-u",
        required=true,
        usage="Username for connecting to CCC DB.")
    private String _username;

    @Option(
        name="-p",
        required=true,
        usage="Password for connecting to CCC DB.")
    private String _password;

    @Option(
        name="-c", required=true, usage="Connection string for the DB.")
    private String _conString;

    @Option(
        name="-v", required=true, usage="Version of the database to build.")
        private int _version;

    @Option(
        name="-d", required=false, usage="Drop existing tables first.")
    private boolean _drop = false;


    private void create() {
        final DatabaseVendor vendor =
            DatabaseVendor.forConnectionString(_conString);

        final Connection newConnection =
            getConnection(
                vendor.driverClassName(),
                _conString,
                _username,
                _password);
        LOG.info("Connected to "+_conString);

        int currentVersion = currentVersion(newConnection);
        LOG.info("Current database version: "+currentVersion);

        try {
            if (_drop) {
                doDrop(vendor, newConnection, currentVersion);
                currentVersion = NEW_DB_VERSION;
            }

            for (int i=(currentVersion+1); i<=_version; i++) {
                doCreate(vendor, newConnection, i);
            }

            commit(newConnection);

        } finally {
            DbUtils.closeQuietly(newConnection);
        }

        LOG.info("Finished.");
    }


    private void commit(final Connection newConnection) {
        try {
            newConnection.commit();
            LOG.info("Commited.");
        } catch (final SQLException e) {
            LOG.info("Error commiting changes.", e);
        }
    }


    private void doCreate(final DatabaseVendor vendor,
                          final Connection newConnection,
                          final int version) {
        final String create =
            "sql/"
            +version+"/"
            +vendor.name().toLowerCase(Locale.US)
            +"/create.sql";
        runScript(newConnection, create);
    }


    private void doDrop(final DatabaseVendor vendor,
                        final Connection newConnection,
                        final int currentVersion) {
        LOG.info("Dropping existing schema.");
        for (int i=currentVersion; i>=0; i--) {
            final String drop =
                "sql/"
                +i+"/"
                +vendor.name().toLowerCase(Locale.US)
                +"/drop.sql";
            runScript(newConnection, drop);
        }
    }


    private void runScript(final Connection newConnection,
                           final String create) {
        LOG.info("Running script: "+create);
        final List<String> statements =
            Resources.readIntoList(create, Charset.forName("UTF-8"));
        LOG.info("Statements to process: "+statements.size());
        for (final String statement : statements) {
            if (statement.trim().length()<1) { continue; }
//            LOG.debug("Executing statement: "+statement);
            execute(newConnection, statement);
        }
    }


    private void execute(final Connection newConnection,
                         final String statement) {
        try {
            final Statement ps = newConnection.createStatement();

            try {
                ps.execute(statement.substring(0, statement.length()-1));

            } finally {
                try {
                    ps.close();
                } catch (final SQLException e) {
                    swallow(e);
                }
            }
        } catch (final SQLException e) {
            LOG.warn(
                "Failed to execute statement: "+statement
                +"\n > "+e.getMessage());
        }
    }


    private int currentVersion(final Connection connection) {
        final String versionQuery =
            "SELECT value FROM settings WHERE name='DATABASE_VERSION'";
        int dbVersion = NEW_DB_VERSION;

        try {
            final Statement s = connection.createStatement();

            try {
                final ResultSet rs = s.executeQuery(versionQuery);
                if (rs.next()) {
                    dbVersion = rs.getInt(1);
                }

            } catch (final SQLException e) { // Assume SETTINGS table missing.
                try {
                    s.close();
                } catch (final SQLException ee) {
                    LOG.warn("Error closing statement.", ee);
                }
            }

        } catch (final SQLException e) {
            throw new RuntimeException(
                "Failed to determine current DB version.", e);
        }

        return dbVersion;
    }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static final void main(final String[] args) {
        parseOptions(args, Schema.class).create();
    }
}
