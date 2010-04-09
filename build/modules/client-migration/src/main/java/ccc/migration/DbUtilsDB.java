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
package ccc.migration;

import static ccc.api.types.DBC.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import ccc.migration.ccc6.handlers.SqlQuery;


/**
 * Implementation of {@link DB} interface, based on Apache's DbUtils library.
 *
 * @author Civic Computing Ltd.
 */
public class DbUtilsDB
    implements
        DB {
    private static Logger log = Logger.getLogger(DbUtilsDB.class);

    private final DataSource _ds;
    private final Connection _c;

    /**
     * Constructor.
     *
     * @param ds JDBC datasource for the database.
     */
    public DbUtilsDB(final DataSource ds) {
        require().notNull(ds);
        _ds = ds;
        try {
            _c = _ds.getConnection();
            Runtime.getRuntime().addShutdownHook(
                new Thread(){
                    /** {@inheritDoc} */
                    @SuppressWarnings("synthetic-access")
                    @Override public void run() {
                        DbUtils.closeQuietly(_c);
                        log.info("Legacy connection closed.");
                    }

                }
            );
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T select(final SqlQuery<T> q,
                          final Object... param) {

        try {
            final PreparedStatement s = _c.prepareStatement(q.getSql());
            try {
                int index = 1;
                for (final Object o : param) {
                    s.setObject(index, o);
                    index++;
                }

                log.debug("Running query.");
                final boolean resultsReturned = s.execute();
                log.debug("Running finished.");

                if (!resultsReturned) {
                    throw new MigrationException("Query returned no results.");
                }

                final ResultSet rs = s.getResultSet();

                try {
                    final Object result = q.handle(rs);
                    return (T) result;

                } catch (final SQLException e) {
                    throw new MigrationException(e);
                } finally {
                    DbUtils.closeQuietly(rs);
                }

            } catch (final SQLException e) {
                throw new MigrationException(e);
            } finally {
                DbUtils.closeQuietly(s);
            }

        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
    }
}
