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
import java.util.List;
import java.util.UUID;

import javax.activation.MimeType;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.sql.DataSource;

import ccc.commons.DBC;
import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.services.DataManager;
import ccc.services.ResourceDao;
import ccc.services.ejb3.support.QueryNames;


/**
 * EJB implementation of the {@link DataManager} interface.
 *
 * TODO: Confirm the interaction between stateful and stateless session beans
 * w.r.t. propagating transactions...
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=DataManager.NAME)
@TransactionAttribute(REQUIRED)
@Local(DataManager.class)
public class DataManagerEJB implements DataManager {

    @EJB(name=ResourceDao.NAME) private ResourceDao _dao;
    @Resource(mappedName = "java:/ccc") private DataSource _datasource;

    /** Constructor. */
    @SuppressWarnings("unused") public DataManagerEJB() { super(); }

    /**
     * Constructor.
     *
     * @param ds The JDBC datasource used to manage data.
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public DataManagerEJB(final DataSource ds, final ResourceDao dao) {
        DBC.require().notNull(ds);
        DBC.require().notNull(dao);
        _datasource = ds;
        _dao = dao;
    }

    /** {@inheritDoc} */
    @Override
    public void createFile(final File file,
                           final UUID parentId,
                           final InputStream dataStream) {
        final Data data = create(dataStream, file.size());
        file.data(data);
        _dao.create(parentId, file);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFile(final UUID fileId,
                           final String title,
                           final String description,
                           final MimeType mimeType,
                           final int size,
                           final InputStream dataStream) {

        final File f = _dao.findLocked(File.class, fileId);
        f.title(title);
        f.description(description);
        f.mimeType(mimeType);
        f.size(size);
        f.data(create(dataStream, size)); // TODO: Delete old data?
        _dao.update(f);
    }

    /** {@inheritDoc} */
    @Override
    public List<File> findImages() {
        return _dao.list(QueryNames.ALL_IMAGES, File.class);
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

    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final OutputStream dataStream) {
        retrieve(
            data,
            new StreamAction(){
                @Override public void execute(final InputStream is) {
                    IO.copy(is, dataStream);
                }
            }
        );
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
