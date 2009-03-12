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

import static ccc.commons.Testing.*;
import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.activation.MimeType;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.h2.jdbcx.JdbcDataSource;

import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourceName;
import ccc.services.DataManager;
import ccc.services.ResourceDao;


/**
 * Tests for the {@link DataManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class DataManagerEJBTest extends TestCase {

    private final InputStream _dummyStream =
        new ByteArrayInputStream(new byte[]{1});

    private DataSource _ds;
    private Connection _c;
    private ResourceDao _al;
    private PreparedStatement _ps;
    private DataManager _dm;


    {
        try {
            Class.forName("org.h2.Driver");
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
         _ds = createStrictMock(DataSource.class);
         _c = createStrictMock(Connection.class);
         _al = createStrictMock(ResourceDao.class);
         _ps = createStrictMock(PreparedStatement.class);
         _dm = new DataManagerEJB(_ds, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _ds = null;
        _c = null;
        _al = null;
        _ps = null;
        _dm = null;
    }

    private void replayAll() {
        replay(_ps, _c, _ds,  _al);
    }

    private void verifyAll() {
        verify(_ps, _c, _ds, _al);
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     */
    public void testCreateFileData() throws SQLException {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final File file = new File(
            new ResourceName("file"), "title", "desc", new Data(), 0);

        _ps.setString(eq(1), isA(String.class));
        _ps.setInt(2, 0);
        _ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
                           _dummyStream,
                           Integer.MAX_VALUE);
        expect(Boolean.valueOf(_ps.execute())).andReturn(Boolean.TRUE);
        _ps.close();

        expect(_c.prepareStatement(DataManagerEJB.CREATE_STATEMENT))
            .andReturn(_ps);
        _c.close();

        expect(_ds.getConnection()).andReturn(_c);

        _al.create(assetRoot.id(), file);

        replayAll();


        // ACT
        _dm.createFile(file, assetRoot.id(), _dummyStream);

        // VERIFY
        verifyAll();
    }

    /**
     * Test.
     * @throws SQLException From JDBC.
     */
    public void testUpdateFile() throws SQLException {

        // ARRANGE
        _ps.setString(eq(1), isA(String.class));
        _ps.setInt(2, 0);
        _ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
                           _dummyStream,
                           Integer.MAX_VALUE);
        expect(Boolean.valueOf(_ps.execute())).andReturn(Boolean.TRUE);
        _ps.close();

        expect(_c.prepareStatement(DataManagerEJB.CREATE_STATEMENT))
            .andReturn(_ps);
        _c.close();

        expect(_ds.getConnection()).andReturn(_c);

        final File f =
            new File(new ResourceName("foo"), "bar", "x", new Data(), 0);
        expect(_al.findLocked(File.class, f.id())).andReturn(f);
        _al.update(f);

        replayAll();

        // ACT
        _dm.updateFile(f.id(), "x", "x", new MimeType(), 1, _dummyStream);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     */
    @SuppressWarnings("boxing")
    public void testCreate() throws SQLException {

        // ARRANGE
        final PreparedStatement ps = createStrictMock(PreparedStatement.class);
        ps.setString(eq(1), isA(String.class));
        ps.setInt(2, 0);
        ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
                           _dummyStream,
                           Integer.MAX_VALUE);
        expect(ps.execute()).andReturn(true);
        ps.close();
        replay(ps);

        final Connection c = createStrictMock(Connection.class);
        expect(c.prepareStatement(DataManagerEJB.CREATE_STATEMENT))
            .andReturn(ps);
        c.close();
        replay(c);

        final DataSource ds = createStrictMock(DataSource.class);
        expect(ds.getConnection()).andReturn(c);
        replay(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(ResourceDao.class));

        // ACT
        dm.create(_dummyStream);

        // ASSERT
        verify(ps, c, ds);
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     */
    @SuppressWarnings("boxing")
    public void testRetrieve() throws SQLException {

        // ARRANGE
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Data d = new Data();

        final ResultSet rs = createStrictMock(ResultSet.class);
        expect(rs.next()).andReturn(true);
        expect(rs.getBinaryStream(1)).andReturn(_dummyStream);
        expect(rs.next()).andReturn(false);
        rs.close();
        replay(rs);

        final PreparedStatement ps = createStrictMock(PreparedStatement.class);
        ps.setString(1, d.id().toString());
        expect(ps.executeQuery()).andReturn(rs);
        ps.close();
        replay(ps);

        final Connection c = createStrictMock(Connection.class);
        expect(c.prepareStatement(DataManagerEJB.RETRIEVE_STATEMENT))
            .andReturn(ps);
        c.close();
        replay(c);

        final DataSource ds = createStrictMock(DataSource.class);
        expect(ds.getConnection()).andReturn(c);
        replay(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(ResourceDao.class));

        // ACT
        dm.retrieve(d, os);

        // ASSERT
        verify(rs, ps, c, ds);
        assertEquals(1, os.toByteArray().length);
        assertEquals(1, os.toByteArray()[0]);
        // TODO: assert the stream was closed?
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     * @throws IOException sometimes.
     */
    public void testCreateWithInMemoryDb() throws SQLException, IOException {

        // ARRANGE
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1");

        createDataTable(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(ResourceDao.class));

        // ACT
        dm.create(_dummyStream);

        // ASSERT
        Connection c = ds.getConnection();
        final Statement s2 = c.createStatement();
        ResultSet rs = s2.executeQuery("select count(*) from data");
        rs.next();
        assertEquals(1, rs.getInt(1));
        s2.close();
        c.close();

        c = ds.getConnection();
        final Statement s3 = c.createStatement();
        rs = s3.executeQuery("select * from data");
        rs.next();
        final InputStream is = rs.getBinaryStream(3);
        assertEquals(1, is.read());
        assertEquals(-1, is.read());
        s3.close();
        c.close();
    }


    /**
     * Test.
     * @throws SQLException sometimes.
     */
    public void testRetrieveWithInMemoryDb() throws SQLException {

        // ARRANGE
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1");

        createDataTable(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(ResourceDao.class));
        final Data d = dm.create(_dummyStream);

        // ACT
        dm.retrieve(d, os);

        // ASSERT
        assertEquals(1, os.toByteArray().length);
        assertEquals(1, os.toByteArray()[0]);
    }


    private void createDataTable(final JdbcDataSource ds) throws SQLException {

        final String sql =
            "CREATE TABLE data("
            + "_ID VARCHAR(255) NOT NULL, "
            + "_VERSION INTEGER NOT NULL, "
            + "_bytes BLOB NOT NULL)";

        final Connection c = ds.getConnection();

        try {
            final PreparedStatement s = c.prepareStatement(sql);
            try {
                s.execute();
            } finally {
                s.close();
            }
        } finally {
            c.close();
        }
    }
}
