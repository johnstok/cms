/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3;

import static ccc.commons.Exceptions.*;
import static javax.ejb.TransactionAttributeType.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.sql.DataSource;

import ccc.commons.DBC;
import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.services.DataManager;


/**
 * EJB implementation of the {@link DataManager} interface.
 *
 * TODO: Confirm the interaction between stateful and stateless session beans
 * w.r.t. propagating transactions...
 *
 * @author Civic Computing Ltd.
 */
@Stateless
@TransactionAttribute(REQUIRED)
@Local(DataManager.class)
public class DataManagerEJB implements DataManager {

    @Resource(mappedName = "java:/ccc")
    private DataSource _datasource;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private DataManagerEJB() {

        super();
    }

    /**
     * Constructor.
     *
     * @param ds The JDBC datasource used to manage data.
     */
    public DataManagerEJB(final DataSource ds) {

        DBC.require().notNull(ds);
        _datasource = ds;
    }

    /** {@inheritDoc} */
    @Override
    public void create(final Data data, final InputStream dataStream) {

        try {
            final Connection c = _datasource.getConnection();

            try {
                final PreparedStatement ps =
                    c.prepareStatement(CREATE_STATEMENT);

                try {
                    ps.setString(1, data.id().toString());
                    ps.setInt(2, 0);
                    ps.setBinaryStream(3, dataStream, Integer.MAX_VALUE);
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
    }

    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final OutputStream dataStream) {

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
                        IO.copy(rs.getBinaryStream(1), dataStream);
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
        }

    }

    /** CREATE_STATEMENT : String. */
    static final String        CREATE_STATEMENT   =
        "INSERT INTO data (_id, _version, _bytes) VALUES (?,?,?)";

    /** RETRIEVE_STATEMENT : String. */
    public static final String RETRIEVE_STATEMENT =
        "SELECT _bytes FROM data WHERE _id=?";
}
