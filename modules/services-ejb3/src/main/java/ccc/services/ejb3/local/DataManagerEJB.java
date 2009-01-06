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

package ccc.services.ejb3.local;

import static ccc.commons.Exceptions.*;
import static javax.ejb.TransactionAttributeType.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.activation.MimeType;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import ccc.commons.DBC;
import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.services.AuditLog;
import ccc.services.DataManager;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * EJB implementation of the {@link DataManager} interface.
 *
 * TODO: Confirm the interaction between stateful and stateless session beans
 * w.r.t. propagating transactions...
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="DataManager")
@TransactionAttribute(REQUIRED)
@Local(DataManager.class)
public class DataManagerEJB extends BaseResourceDao implements DataManager {

    @Resource(mappedName = "java:/ccc")
    private DataSource _datasource;

    /** Constructor. */
    @SuppressWarnings("unused") public DataManagerEJB() { super(); }

    /**
     * Constructor.
     *
     * @param ds The JDBC datasource used to manage data.
     * @param em The entityManager used to persist domain objects.
     * @param auditLog An audit logger.
     */
    public DataManagerEJB(final DataSource ds,
                          final EntityManager em,
                          final AuditLog auditLog) {
        DBC.require().notNull(ds);
        DBC.require().notNull(em);
        DBC.require().notNull(auditLog);
        _datasource = ds;
        _em = em;
        _audit = auditLog;
    }

    /** {@inheritDoc} */
    @Override
    public void createFile(final File file,
                           final UUID parentId,
                           final InputStream dataStream) {
        final Data data = create(dataStream);
        file.data(data);
        create(parentId, file);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFile(final UUID fileId,
                           final long version,
                           final String title,
                           final String description,
                           final MimeType mimeType,
                           final long size,
                           final InputStream dataStream) {

        final File f = find(File.class, fileId, version);
        f.title(title);
        f.description(description);
        f.mimeType(mimeType);
        f.size(size);
        f.data(create(dataStream)); // TODO: Delete old data?
        _audit.recordUpdate(f);
    }

    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream) {

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
                                       Integer.MAX_VALUE);
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

    /** STREAM_POSITION_CREATE : int. */
    public static final int STREAM_POSITION_CREATE = 3;
}
