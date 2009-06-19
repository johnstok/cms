/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.commons.Resources;


/**
 * TODO: Add a description for this type.
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
        final Connection newConnection =
            getConnection(
                getDriverForConnectionString(_conString),
                _conString,
                _username,
                _password);
        try {
            final List<String> statements =
                Resources.readIntoList(
                    "version1.0/h2/ccc7-schema.sql",
                    Charset.forName("UTF-8"));

            for (final String statement : statements) {
                execute(newConnection, statement);
            }

        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }

    private void execute(final Connection newConnection,
                         final String statement) {
        try {
            final PreparedStatement ps =
                newConnection.prepareStatement(statement);

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

    public static final void main(final String[] args) {
        parseOptions(args, Schema.class).create();
    }
}
