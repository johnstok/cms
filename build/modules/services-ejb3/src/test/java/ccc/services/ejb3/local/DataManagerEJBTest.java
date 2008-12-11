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

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.h2.jdbcx.JdbcDataSource;

import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourceName;
import ccc.services.AuditLog;
import ccc.services.DataManager;


/**
 * Tests for the {@link DataManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class DataManagerEJBTest extends TestCase {

    {
        try {
            Class.forName("org.h2.Driver");
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     */
    public void testCreateFileData() throws SQLException {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final InputStream dummyStream = new ByteArrayInputStream(new byte[]{1});

        final File file = new File(
            new ResourceName("file"), "title", "desc", new Data(), 0);

        final PreparedStatement ps = createStrictMock(PreparedStatement.class);
        ps.setString(eq(1), isA(String.class));
        ps.setInt(2, 0);
        ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
                           dummyStream,
                           Integer.MAX_VALUE);
        expect(Boolean.valueOf(ps.execute())).andReturn(Boolean.TRUE);
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

        final EntityManager em = createStrictMock(EntityManager.class);
        em.persist(file);
        expect(em.find(Folder.class, assetRoot.id())).andReturn(assetRoot);
        replay(em);

        final AuditLog al = createStrictMock(AuditLog.class);
        al.recordCreate(file);

        final DataManager dm = new DataManagerEJB(ds, em, al);

        // ACT
        dm.createFile(file, assetRoot.id(), dummyStream);

        // VERIFY
        verify(ps, c, ds, em);
    }

    /**
     * Test.
     * @throws SQLException sometimes.
     */
    @SuppressWarnings("boxing")
    public void testCreate() throws SQLException {

        // ARRANGE
        final InputStream dummyStream = new ByteArrayInputStream(new byte[]{1});

        final PreparedStatement ps = createStrictMock(PreparedStatement.class);
        ps.setString(eq(1), isA(String.class));
        ps.setInt(2, 0);
        ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
                           dummyStream,
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
                               dummy(EntityManager.class),
                               dummy(AuditLog.class));

        // ACT
        dm.create(dummyStream);

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
        final InputStream dummyStream = new ByteArrayInputStream(new byte[]{1});
        final Data d = new Data();

        final ResultSet rs = createStrictMock(ResultSet.class);
        expect(rs.next()).andReturn(true);
        expect(rs.getBinaryStream(1)).andReturn(dummyStream);
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
                               dummy(EntityManager.class),
                               dummy(AuditLog.class));

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
        final InputStream dummyStream = new ByteArrayInputStream(new byte[]{1});
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1");

        createDataTable(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(EntityManager.class),
                               dummy(AuditLog.class));

        // ACT
        dm.create(dummyStream);

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
        final InputStream dummyStream = new ByteArrayInputStream(new byte[]{1});
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1");

        createDataTable(ds);

        final DataManager dm =
            new DataManagerEJB(ds,
                               dummy(EntityManager.class),
                               dummy(AuditLog.class));
        final Data d = dm.create(dummyStream);

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
