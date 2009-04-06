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
package ccc.security;

import static org.easymock.EasyMock.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.callback.CallbackHandler;
import javax.sql.DataSource;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.commons.Registry;
import ccc.domain.CreatorRoles;
import ccc.domain.User;


/**
 * Tests for the {@link JdbcDatabase} class.
 *
 * @author Civic Computing Ltd.
 */
public class JdbcDatabaseTest
    extends
        TestCase {

    /**
     * Test.
     * @throws SQLException From JDBC.
     */
    public void testLookupUserReturnsNullForNullUsername() throws SQLException {

        // ARRANGE

        // ACT
        final Object[] user = new JdbcDatabase(_r).lookupUser(null);

        // ASSERT
        assertNull("Should be NULL.", user);
    }

    /**
     * Test.
     *
     * @throws SQLException Due to JDBC API.
     */
    public void testLookupUserSucceeds() throws SQLException {

        // ARRANGE
        final User u = new User("user");

        expect(_ds.getConnection()).andReturn(_c);
        expect(_c.prepareStatement("x"))
            .andReturn(_s);
        _s.setString(1, u.username());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(u.id().toString());
        expect(_rs.getBytes(2)).andReturn(new byte[]{0});
        expect(_rs.getString(3)).andReturn(UUID.randomUUID().toString());
        expect(_rs.next()).andReturn(Boolean.FALSE);
        _rs.close();
        _s.close();
        _c.close();

        replayAll();

        // ACT
        final Object[] result = _db.lookupUser(u.username());

        verifyAll();
        assertEquals(3, result.length);
        assertEquals(u.id().toString(), result[0]);
        assertTrue(
            "Arrays should be equal.",
            Arrays.equals(new byte[]{0}, (byte[]) result[1]));
    }

    /**
     * Test.
     *
     * @throws SQLException Due to JDBC API.
     */
    public void testLookupUserFailsForMissingUser() throws SQLException {

        // ARRANGE
        final User u = new User("user");

        expect(_ds.getConnection()).andReturn(_c);
        expect(_c.prepareStatement("x"))
        .andReturn(_s);
        _s.setString(1, u.username());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.FALSE);
        _rs.close();
        _s.close();
        _c.close();

        replayAll();

        // ACT
        final Object[] result = _db.lookupUser(u.username());

        verifyAll();
        assertNull("Should be NULL", result);
    }

    /**
     * Test.
     *
     * @throws SQLException Due to JDBC API.
     */
    public void testLookupUserFailsForDuplicateUsers() throws SQLException {

        // ARRANGE
        final User u = new User("user");

        expect(_ds.getConnection()).andReturn(_c);
        expect(_c.prepareStatement("x"))
        .andReturn(_s);
        _s.setString(1, u.username());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(u.id().toString());
        expect(_rs.getBytes(2)).andReturn(new byte[]{0});
        expect(_rs.getString(3)).andReturn(UUID.randomUUID().toString());
        expect(_rs.next()).andReturn(Boolean.TRUE);
        _rs.close();
        _s.close();
        _c.close();

        replayAll();


        // ACT
        try {
            _db.lookupUser(u.username());
            fail("Should throw exception.");


        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Duplicate users for username: user", e.getMessage());
        }
        verifyAll();
    }

    /**
     * Test.
     *
     * @throws SQLException Due to JDBC API.
     */
    public void testLookupRoles() throws SQLException {

        // ARRANGE
        final User u = new User("user");
        u.addRole(CreatorRoles.ADMINISTRATOR);
        u.addRole(CreatorRoles.CONTENT_CREATOR);

        expect(_ds.getConnection()).andReturn(_c);
        expect(_c.prepareStatement("y"))
            .andReturn(_s);
        _s.setString(1, u.id().toString());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(CreatorRoles.ADMINISTRATOR);
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(CreatorRoles.CONTENT_CREATOR);
        expect(_rs.next()).andReturn(Boolean.FALSE);
        _rs.close();
        _s.close();
        _c.close();

        replayAll();

        // ACT
        final Set<String> result =
            _db.lookupRoles(u.id().toString());

        // ASSERT
        verifyAll();
        assertEquals(
            new HashSet<String>(){{
                add(CreatorRoles.ADMINISTRATOR);
                add(CreatorRoles.CONTENT_CREATOR);
            }},
            result);
    }

    private void replayAll() {
        replay(_ds, _c, _s, _rs, _cbHandler);
    }

    private void verifyAll() {
        verify(_ds, _c, _s, _rs, _cbHandler);
    }

    private Registry _r;
    private DataSource _ds;
    private Connection _c;
    private PreparedStatement _s;
    private ResultSet _rs;
    private Database _db;
    private CallbackHandler _cbHandler;

    /** {@inheritDoc} */
    @Override protected void setUp() throws Exception {
        _r = new MapRegistry();
        _ds = createStrictMock(DataSource.class);
        _c = createStrictMock(Connection.class);
        _s = createStrictMock(PreparedStatement.class);
        _rs = createStrictMock(ResultSet.class);
        _cbHandler = createStrictMock(CallbackHandler.class);
        _r.put("java:/ccc", _ds);
        _db = new JdbcDatabase(_r);
        _db.setOptions(new HashMap<String, Object>() {{
            put("dsJndiName", "java:/ccc");
            put("principalsQuery", "x");
            put("rolesQuery", "y");
        }});
    }

    /** {@inheritDoc} */
    @Override protected void tearDown() throws Exception {
        _r = null;
        _ds = null;
        _c = null;
        _s = null;
        _rs = null;
        _cbHandler = null;
        _db = null;
    }
}
