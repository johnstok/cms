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
package ccc.persistence;

import static ccc.commons.Exceptions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.persistence.streams.CoreData;
import ccc.types.DBC;


/**
 * An implementation of {@link CoreData} that reads / writes via JDBC.
 *
 * @author Civic Computing Ltd.
 */
class JdbcCoreData implements CoreData {

    private final DataSource _datasource;

    /**
     * Constructor.
     *
     * @param datasource JDBC datasource to read/write to.
     */
    public JdbcCoreData(final DataSource datasource) {
        DBC.require().notNull(datasource);
        _datasource = datasource;
    }


    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream, final int length) {

        final Data data = new Data();

        try {
            final Connection c = _datasource.getConnection();

            try {
                final PreparedStatement ps =
                    c.prepareStatement(CREATE_STATEMENT);

                try {
                    ps.setString(1, data.id().toString());
                    ps.setInt(2, 0);
                    ps.setBinaryStream(STREAM_POSITION_CREATE,
                                       dataStream,
                                       length);
                    ps.execute();
                } finally {
                    try {
                        ps.close();
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
        } catch (final SQLException e) {
            throw new CCCException(e);
        }

        return data;
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final StreamAction action) {
        try {
            final Connection c = _datasource.getConnection();

            try {
                final PreparedStatement ps =
                    c.prepareStatement(RETRIEVE_STATEMENT);

                try {
                    ps.setString(1, data.id().toString());
                    final ResultSet rs = ps.executeQuery();

                    try {
                        DBC.ensure().toBeTrue(rs.next());
                        action.execute(rs.getBinaryStream(1));
                        DBC.ensure().toBeFalse(rs.next());

                    } finally {
                        try {
                            rs.close();
                        } catch (final SQLException e) {
                            swallow(e);
                        }
                    }

                } finally {
                    try {
                        ps.close();
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

        } catch (final SQLException e) {
            throw new CCCException(e);
        } catch (final Exception e) {
            throw new CCCException(e);
        }
    }

    /** CREATE_STATEMENT : String. */
    static final String        CREATE_STATEMENT   =
        "INSERT INTO data (id, version, bytes) VALUES (?,?,?)";

    /** RETRIEVE_STATEMENT : String. */
    public static final String RETRIEVE_STATEMENT =
        "SELECT bytes FROM data WHERE id=?";

    /** STREAM_POSITION_CREATE : int. */
    public static final int STREAM_POSITION_CREATE = 3;
}

