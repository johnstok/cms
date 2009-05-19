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
package ccc.migration;

import static ccc.api.DBC.*;

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
                boolean resultsReturned = s.execute();
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
