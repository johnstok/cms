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
package ccc.persistence.jpa;

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

import junit.framework.TestCase;

import org.h2.jdbcx.JdbcDataSource;

import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.persistence.StreamAction;


/**
 * Tests for the {@link JdbcCoreData} class.
 *
 * @author Civic Computing Ltd.
 */
public class JdbcCoreDataTest
    extends
        TestCase {

    private final InputStream _dummyStream =
        new ByteArrayInputStream(new byte[]{1});

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

        final JdbcCoreData cd = new JdbcCoreData(ds);
        final Data d = cd.create(_dummyStream, 1);

        // ACT
        cd.retrieve(
            d,
            new StreamAction() {
                @Override
                public void execute(final InputStream is) throws Exception {
                    IO.copy(is, os);
                }
            }
        );

        // ASSERT
        assertEquals(1, os.toByteArray().length);
        assertEquals(1, os.toByteArray()[0]);
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

        final JdbcCoreData cd = new JdbcCoreData(ds);

        // ACT
        cd.create(_dummyStream, 1);

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


    {
        try {
            Class.forName("org.h2.Driver");
        } catch (final ClassNotFoundException e) {
            throw new CCCException(e);
        }
    }

    private void createDataTable(final JdbcDataSource ds) throws SQLException {

        final String sql =
            "CREATE TABLE data("
            + "ID VARCHAR(255) NOT NULL, "
            + "VERSION INTEGER NOT NULL, "
            + "bytes BLOB NOT NULL)";

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
