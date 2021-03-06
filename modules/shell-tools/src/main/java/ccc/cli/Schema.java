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

import static ccc.commons.Exceptions.swallow;

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
/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Schema
    extends
        CccApp {

    private static final int    NEW_DB_VERSION = -1;
    private static final int    LATEST_DB_VERSION = 3;
    private static final Logger LOG = Logger.getLogger(Schema.class);

    @Option(
        name="-u",
        required=true,
        usage="Username for connecting to CCC DB.")
    private String _username;

    @Option(
        name="-p",
        required=false,
        usage="Password for connecting to CCC DB.")
    private String _password;

    @Option(
        name="-c", required=true, usage="Connection string for the DB.")
    private String _conString;

    @Option(
        name="-v", usage="Version of the database to build.")
        private int _version = LATEST_DB_VERSION;

    @Option(
        name="-d", required=false, usage="Drop existing tables first.")
    private boolean _drop = false;

    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    String getUsername() {
        return _username;
    }

    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }

    /**
     * Accessor.
     *
     * @return Returns the version.
     */
    int getVersion() {
        return _version;
    }

    /**
     * Mutator.
     *
     * @param version The version to set.
     */
    public void setVersion(final int version) {

        _version = version;
    }


    /**
     * Accessor.
     *
     * @return Returns the drop value.
     */
    boolean isDrop() {
        return _drop;
    }


    /**
     * Mutator.
     *
     * @param drop The drop value to set.
     */
    public void setDrop(final boolean drop) {
        _drop = drop;
    }


    private void create() {
        final DatabaseVendor vendor =
            DatabaseVendor.forConnectionString(_conString);

        final Connection newConnection =
            getConnection(
                vendor.driverClassName(),
                _conString,
                _username,
                getPassword());
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
                // execute only when upgrading from 2 to 3.
                if (i == 3 && currentVersion < 3) {
                    final StringToDecConverterUtil stdcu =
                        new StringToDecConverterUtil();
                    stdcu.convertParagraphs(newConnection);
                }
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
        final StringBuilder statement = new StringBuilder();

        int statementCount = 0;
        for (final String line : statements) {
            if (line.trim().length()<1) {
                if (0<statement.length()) {
                    LOG.info("Executing statement: "+statement);
                    execute(newConnection, statement.toString());
                    statement.setLength(0);
                    statementCount++;
                }
            } else if(!line.trim().startsWith("--")) {
                statement.append(line);
                if(!create.toLowerCase().contains("oracle")) {
                    statement.append("\n");
                } else {
                    if(statement.lastIndexOf(";") != statement.length() -1) {
                        statement.append(" ");
                    }
                }
            } else { LOG.info("Ignoring: "+line); }
        }
        if (0<statement.length()) {
            LOG.debug("Executing statement: "+statement);
            execute(newConnection, statement.toString());
            statementCount++;
        }
        LOG.info("Statements to process: "+statementCount);
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
//    throws Exception {
        final Schema s = parseOptions(args, Schema.class);
        s.create();

        /*final String source = "sql/0/oracle/create.sql";
        final List<String> strlist =
            Resources.readIntoList(source, Charset.forName("UTF-8"));

        for(final String line : strlist) {
            System.out.println("Line is: "+line);
            System.out.println("Matches? "
                +line.matches(".*[^\\u0020-\\u007e]?"));
        }*/
    }

    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    String getPassword() {
        if (_password == null) {
            return readConsolePassword("Password for connecting to CCC DB");
        }
        return _password;
    }


    /**
     * Mutator.
     *
     * @param password The password to set.
     */
    public void setPassword(final String password) {
        _password = password;
    }

}

