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
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    private void create() {
        final DatabaseVendor vendor =
            DatabaseVendor.forConnectionString(_conString);

        final Connection newConnection =
            getConnection(
                vendor.driverClassName(),
                _conString,
                _username,
                _password);

        try {
            final String sqlPath =
                "/create/"
                +_version+"/"
                +vendor.name().toLowerCase(Locale.US)
                +"/ccc7-schema.sql";

            LOG.debug("Executing "+sqlPath);
            LOG.info("Running create script.");

            final List<String> statements =
                Resources.readIntoList(sqlPath, Charset.forName("UTF-8"));
            for (final String statement : statements) {
                execute(newConnection, statement);
            }

            try {
                newConnection.commit();
                LOG.info("Commited.");
            } catch (final SQLException e) {
                LOG.info("Error commiting changes.", e);
            }

            LOG.info("Finished.");

        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }

    private void execute(final Connection newConnection,
                         final String statement) {
        try {
            final PreparedStatement ps =
                newConnection.prepareStatement(
                    statement.substring(0, statement.length()-1));

            try {
                ps.execute();
            } finally {
                try {
                    ps.close();
                } catch (final SQLException e) {
                    swallow(e);
                }
            }
        } catch (final SQLException e) {
            LOG.warn("Failed to execute statement: "+statement, e);
        }
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
