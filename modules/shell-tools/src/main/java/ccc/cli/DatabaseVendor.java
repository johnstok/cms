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

import ccc.migration.MigrationException;


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
            throw new MigrationException("Unsupported database");
        }

    }
}
