/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.sql.Connection;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;



/**
 * Command line interface for settings management.
 *
 * @author Civic Computing Ltd.
 */
public final class Settings extends CccApp {
    private static final Logger LOG = Logger.getLogger(Settings.class);


    /**
     * App entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        final Settings s  = parseOptions(args, Settings.class);
        s.updateSettings();
    }

    /**
     * Modify settings.
     *
     */
    public void updateSettings() {
        final DatabaseVendor vendor =
            DatabaseVendor.forConnectionString(getConString());
        final Connection newConnection =
            getConnection(
                vendor.driverClassName(),
                getConString(),
                getUsername(),
                getPassword());
        try {
            final NewDBQueries queries = new NewDBQueries(newConnection);
            queries.updateSettingPaths(
                getPath());
            LOG.info("Updated path");
        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }

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
        name="-path", required=true, usage="Path to use.")
        private String _path;



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
        if (_password == null) {
            return readConsolePassword("Password for connecting to CCC DB");
        }
        return _password;
    }


    /**
     * Accessor.
     *
     * @return Returns the conString.
     */
    String getConString() {
        return _conString;
    }


    /**
     * Accessor.
     *
     * @return Returns the path.
     */
    String getPath() {
        return _path;
    }
}
