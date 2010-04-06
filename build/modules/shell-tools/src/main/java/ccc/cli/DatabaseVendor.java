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



/**
 * An enumeration of the supported database vendors.
 *
 * @author Civic Computing Ltd.
 */
public enum DatabaseVendor {


    /**
     * Oracle DB vendor.
     */
    ORACLE {
        /** {@inheritDoc} */
        @Override public String driverClassName() {
            return "oracle.jdbc.driver.OracleDriver";
        }
    },


    /**
     * H2 DB vendor.
     */
    H2 {
        /** {@inheritDoc} */
        @Override public String driverClassName() {
            return "org.h2.Driver";
        }
    },


    /**
     * MySQL DB vendor.
     */
    MYSQL {
        /** {@inheritDoc} */
        @Override public String driverClassName() {
            return "com.mysql.jdbc.Driver";
        }
    },


    /**
     * SqlServer DB vendor.
     */
    SQLSERVER {
        /** {@inheritDoc} */
        @Override public String driverClassName() {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
    };


    /**
     * Accessor.
     *
     * @return The DB vendor's driver class name.
     */
    public abstract String driverClassName();


    /**
     * Determine the driver class based on connection string.
     *
     * @param conString The connection string.
     * @return The corresponding driver class.
     */
    public static DatabaseVendor forConnectionString(final String conString) {
        if (conString.startsWith("jdbc:oracle")) {
            return DatabaseVendor.ORACLE;
        } else if (conString.startsWith("jdbc:mysql")) {
            return DatabaseVendor.MYSQL;
        } else if (conString.startsWith("jdbc:sqlserver")) {
            return DatabaseVendor.SQLSERVER;
        } else if (conString.startsWith("jdbc:h2")) {
            return DatabaseVendor.H2;
        } else {
            throw new RuntimeException("Unsupported database");
        }

    }
}
