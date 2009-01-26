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

import static ccc.commons.DBC.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import ccc.migration.ccc6.handlers.SqlQuery;


/**
 * Implementation of {@link DB} interface, based on Apache's DbUtils library.
 *
 * @author Civic Computing Ltd.
 */
public class DbUtilsDB
    implements
        DB {

    private final QueryRunner _queryRunner;

    /**
     * Constructor.
     *
     * @param ds JDBC datasource for the database.
     */
    public DbUtilsDB(final DataSource ds) {
        require().notNull(ds);
        _queryRunner = new QueryRunner(ds);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T select(final SqlQuery<T> q,
                          final Object... param) {
        try {
            final Object result = _queryRunner.query(q.getSql(), param, q);
            return (T) result;
        } catch (final SQLException e1) {
            throw new MigrationException(e1);
        }
    }
}
