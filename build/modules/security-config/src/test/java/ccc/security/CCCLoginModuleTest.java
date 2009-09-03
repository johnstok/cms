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

import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;

import junit.framework.TestCase;
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
     * @throws LoginException If authentication fails.
     * @throws SQLException If a DB query fails.
     */
    @SuppressWarnings("unchecked")
    public void testLogin() throws LoginException, SQLException {

        // ARRANGE
        final UUID pw = UUID.randomUUID();
        final UUID u = UUID.randomUUID();
        _db.setOptions(isA(Map.class));
        expect(_db.lookupUser("foo")).andReturn(
            new Object[]{
                u.toString(),
                User.hash("a", pw.toString()),
                pw.toString()
            });
        expect(_db.lookupRoles(u.toString())).andReturn(new HashSet<String>());
        replay(_db);

        _lm.initialize(
            null,
            new CallbackHandler(){
                @Override public void handle(final Callback[] callbacks) {
                    ((NameCallback) callbacks[0]).setName("foo");
                    ((PasswordCallback) callbacks[1])
                        .setPassword("a".toCharArray());
                }},
            new HashMap<String, Object>(),
            new HashMap<String, Object>());

        // ACT
        final boolean success = _lm.login();

        // ASSERT
        verify(_db);
        assertTrue("Login should be accepted.", success);
        assertNotNull(_lm.getCallerPrincipal());
        assertNotNull(_lm.getRoleGroup());
    }

    /**
     * Test.
     */
    @SuppressWarnings("unchecked")
    public void testCreateCallerPrincipal() {

        // ARRANGE

        // ACT
        final Group p = _lm.createCallerPrincipal("foo");

        // ASSERT
        final List<Principal> callers =
            (List<Principal>) Collections.list((p).members());
        assertEquals(1, callers.size());
        assertEquals("foo", callers.get(0).getName());

    }

    /**
     * Test.
     */
    public void testAbort() {

        // ARRANGE

        // ACT
        final boolean success = _lm.abort();

        // ASSERT
        assertTrue("Should be true.", success);
        assertNull(_lm.getCbHandler());
        assertNull(_lm.getSubject());
        assertNull(_lm.getRoles());
        assertNull(_lm.getUser());
        assertNull(_lm.getRoleGroup());
        assertNull(_lm.getCallerPrincipal());
        assertNotNull(_lm.getDb());
    }

    /**
     * Test.
     * @throws LoginException If login fails.
     * @throws SQLException From the database.
     */
    @SuppressWarnings("unchecked")
    public void testLoginRejectsAnonymousUsers()
                                           throws LoginException, SQLException {

        // ARRANGE
        _db.setOptions(isA(Map.class));
        expect(_db.lookupUser("foo")).andReturn(null);
        replay(_db);

        _lm.initialize(
            null,
            new CallbackHandler(){
                @Override public void handle(final Callback[] callbacks) {
                    ((NameCallback) callbacks[0]).setName("foo");
                    ((PasswordCallback) callbacks[1])
                        .setPassword("a".toCharArray());
                }},
            new HashMap<String, Object>(),
            new HashMap<String, Object>());

        // ACT
        final boolean success = _lm.login();

        // ASSERT
        verify(_db);
        assertFalse("Login should be rejected.", success);
    }

    /**
     * Test.
     */
    public void testCreateRoles() {

        // ARRANGE
        replay(_db);
        final List<String> roles =
            Arrays.asList(new String[]{"foo", "bar", "baz"});

        // ACT
        final Group roleGroup = _lm.createRoles(roles);

        // ASSERT
        verify(_db);
        assertEquals("Roles", roleGroup.getName());
        assertEquals(3, Collections.list(roleGroup.members()).size());
    }

    private Database _db;
    private CCCLoginModule _lm;

    /** {@inheritDoc} */
    @Override protected void setUp() {
        _db = createStrictMock(Database.class);
        _lm = new CCCLoginModule(_db);
    }

    /** {@inheritDoc} */
    @Override protected void tearDown() {
        _db = null;
        _lm = null;
    }
}
