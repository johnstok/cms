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
package ccc.security;

import static org.easymock.EasyMock.*;

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.commons.Registry;
import ccc.domain.CreatorRoles;
import ccc.domain.User;


/**
 * Tests for the {@link CCCLoginModule} class.
 *
 * @author Civic Computing Ltd.
 */
public class CCCLoginModuleTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCreateRoles() {

        // ARRANGE
        final List<String> roles =
            Arrays.asList(new String[]{"foo", "bar", "baz"});

        // ACT
        final Group roleGroup = new CCCLoginModule().createRoles(roles);

        // ASSERT
        assertEquals("Roles", roleGroup.getName());
        assertEquals(3, Collections.list(roleGroup.members()).size());
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
        expect(_c.prepareStatement(CCCLoginModule.SQL_LOOKUP_USER))
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

        replay(_ds, _c, _s, _rs);

        // ACT
        final Object[] result = new CCCLoginModule(_r).lookupUser(u.username());

        // ASSERT
        verify(_ds, _c, _s, _rs);
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
        expect(_c.prepareStatement(CCCLoginModule.SQL_LOOKUP_USER))
        .andReturn(_s);
        _s.setString(1, u.username());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.FALSE);
        _rs.close();
        _s.close();
        _c.close();

        replay(_ds, _c, _s, _rs);

        // ACT
        final Object[] result = new CCCLoginModule(_r).lookupUser(u.username());

        // ASSERT
        verify(_ds, _c, _s, _rs);
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
        expect(_c.prepareStatement(CCCLoginModule.SQL_LOOKUP_USER))
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

        replay(_ds, _c, _s, _rs);


        // ACT
        try {
            new CCCLoginModule(_r).lookupUser(u.username());
            fail("Should throw exception.");


        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Duplicate users for username: user", e.getMessage());
        }
        verify(_ds, _c, _s, _rs);
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
        expect(_c.prepareStatement(CCCLoginModule.SQL_LOOKUP_ROLES))
            .andReturn(_s);
        _s.setString(1, u.id().toString());
        expect(_s.executeQuery()).andReturn(_rs);
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(CreatorRoles.ADMINISTRATOR.name());
        expect(_rs.next()).andReturn(Boolean.TRUE);
        expect(_rs.getString(1)).andReturn(CreatorRoles.CONTENT_CREATOR.name());
        expect(_rs.next()).andReturn(Boolean.FALSE);
        _rs.close();
        _s.close();
        _c.close();

        replay(_ds, _c, _s, _rs);

        // ACT
        final Set<String> result =
            new CCCLoginModule(_r).lookupRoles(u.id().toString());

        // ASSERT
        verify(_ds, _c, _s, _rs);
        assertEquals(
            new HashSet<String>(){{
                add(CreatorRoles.ADMINISTRATOR.name());
                add(CreatorRoles.CONTENT_CREATOR.name());
            }},
            result);
    }

    private Registry _r;
    private     DataSource _ds;
    private Connection _c;
    private PreparedStatement _s;
    private ResultSet _rs;

    /** {@inheritDoc} */
    @Override protected void setUp() throws Exception {
        _r = new MapRegistry();
        _ds = createStrictMock(DataSource.class);
        _c = createStrictMock(Connection.class);
        _s = createStrictMock(PreparedStatement.class);
        _rs = createStrictMock(ResultSet.class);
        _r.put("java:/ccc", _ds);
    }

    /** {@inheritDoc} */
    @Override protected void tearDown() throws Exception {
        _r = null;
        _ds = null;
        _c = null;
        _s = null;
        _rs = null;
    }
}
