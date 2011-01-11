/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Revision      $1$
 * Modified by   $Panos$
 * Modified on   $29 Oct 2010$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;


/**
 * Tool to convert numbers represented as strings to decimal.
 *
 * @author Civic Computing Ltd.
 */
public class StringToDecConverter extends CccApp {
    private static final Logger LOG =
        Logger.getLogger(StringToDecConverter.class);


    @Option (name="-u",
             required = true,
             usage = "Username for connecting to CCC DB.")
    private String _username;

    @Option (name="-p",
        required = true,
        usage = "Password for connecting to CCC DB.")
    private String _password;

    @Option (name="-c",
        required = true,
        usage = "Connection string for connecting for the DB.")
    private String _conString;

    private Connection createConnection() {
        Connection con = null;
        final DatabaseVendor vendor =
            DatabaseVendor.forConnectionString(getConnectionString());

        con = getConnection(vendor.driverClassName(),
                          getConnectionString(), _username, _password);
        LOG.debug("Connected to: "+getConnectionString());
        return con;
    }

    private void convert() {
        final StringToDecConverterUtil stdcu = new StringToDecConverterUtil();
        final Connection conn = createConnection();
        stdcu.convertParagraphs(conn);
        commit(conn);
        DbUtils.closeQuietly(conn);
    }

    private void commit(final Connection con) {
        try {
            con.commit();
        } catch (final SQLException e) {
            LOG.fatal("Error committing changes", e);
            try {
                LOG.error("Rolling back connection");
                con.rollback();
                LOG.info("Rolled back connection successfully");
            } catch (final SQLException e1) {
                LOG.error("Failed to rollback connection");
            }
        }
    }


    /**
     * main().
     *
     * @param args -u <user> -p <password> -c <database_connection string>
     */
    public static void main(final String[] args) {
        final StringToDecConverter stdc =
            parseOptions(args, StringToDecConverter.class);
        stdc.convert();
    }


    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    public String getUsername() {

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
     * @return Returns the password.
     */
    public String getPassword() {

        return _password;
    }

    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    public String getConnectionString() {

        return _conString;
    }

    /**
     * Mutator.
     *
     * @param conString The connection string
     */
    public void setConnectionString(final String conString) {

        _conString = conString;
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
