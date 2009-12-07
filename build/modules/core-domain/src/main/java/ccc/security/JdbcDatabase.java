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
package ccc.security;

import static ccc.commons.Exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.CCCException;
import ccc.types.Username;


/**
 * JDBC implementation of the {@link Database} api.
 *
 * @author Civic Computing Ltd.
 */
public class JdbcDatabase
    implements
        Database {

    private Registry _r = new JNDI();
    private String _datasource;
    private String _principalQuery;
    private String _rolesQuery;

    /**
     * Constructor.
     *
     * @param r The registry to use for resource lookup.
     */
    public JdbcDatabase(final Registry r) {
        _r = r;
    }

    /**
     * Constructor.
     */
    public JdbcDatabase() {
        this(new JNDI());
    }


    /** {@inheritDoc} */
    @Override
    public Object[] lookupUser(final Username username) throws SQLException {

        if (null==username) {
            return null;
        }

        final DataSource ds = _r.get(_datasource);
        final Connection c = ds.getConnection();

        try { // Work with the Connection, close on error.
            final PreparedStatement s = c.prepareStatement(_principalQuery);

            try { // Work with the Statement, close on error.
                s.setString(1, username.toString());
                final ResultSet rs = s.executeQuery();

                try { // Work with the ResultSet, close on error.

                    if (!rs.next()) { // No user exists with username.
                        return null;
                    }

                    final Object[] result = new Object[3];
                    result[0] = rs.getString(1);
                    result[1] = rs.getBytes(2);
                    result[2] = rs.getString(3);

                    if (rs.next()) { // Duplicate users with username - error.
                        throw new CCCException(
                            "Duplicate users for username: "+username);
                    }

                    return result;
                } finally {
                    try {
                        rs.close();
                    } catch (final SQLException e) {
                        swallow(e);
                    }
                }

            } finally {
                try {
                    s.close();
                } catch (final SQLException e) {
                    swallow(e);
                }
            }

        } finally {
            try {
                c.close();
            } catch (final SQLException e) {
                swallow(e);
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public Set<String> lookupRoles(final String userId) throws SQLException {
        final Set<String> result = new HashSet<String>();
        final DataSource ds = _r.get(_datasource);
        final Connection c = ds.getConnection();

        try { // Work with the Connection, close on error.
            final PreparedStatement s = c.prepareStatement(_rolesQuery);

            try { // Work with the Statement, close on error.
                s.setString(1, userId);
                final ResultSet rs = s.executeQuery();

                try { // Work with the ResultSet, close on error.
                    while (rs.next()) {
                        result.add(rs.getString(1));
                    }
                } finally {
                    try {
                        rs.close();
                    } catch (final SQLException e) {
                        swallow(e);
                    }
                }

            } finally {
                try {
                    s.close();
                } catch (final SQLException e) {
                    swallow(e);
                }
            }

        } finally {
            try {
                c.close();
            } catch (final SQLException e) {
                swallow(e);
            }
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void setOptions(final Map<String, ?> options) {
        _datasource = (String) options.get("dsJndiName");
        _principalQuery = (String) options.get("principalsQuery");
        _rolesQuery = (String) options.get("rolesQuery");
    }
}
