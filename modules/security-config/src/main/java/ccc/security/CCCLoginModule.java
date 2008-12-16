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

import static ccc.commons.Exceptions.*;

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Password;



/**
 * A JAAS login module for authenticating CCC users.
 *
 * @author Civic Computing Ltd.
 */
public class CCCLoginModule implements LoginModule {

    /** SQL_LOOKUP_USER : String. */
    public static final String SQL_LOOKUP_USER =
        "SELECT USER._ID, PASSWORD._HASH, PASSWORD._ID "
        + "FROM PASSWORD, USER "
        + "WHERE PASSWORD._USER=USER._ID "
        + "AND USER._USERNAME=?";

    /** SQL_LOOKUP_ROLES : String. */
    public static final String SQL_LOOKUP_ROLES =
        "SELECT ROLE "
        + "FROM USER__ROLES "
        + "where ID=?";

    private Registry _r = new JNDI();

    private CallbackHandler _cbHandler;
    private Object[] _user;
    private Set<String> _roles;
    private Group _roleGroup;
    private Subject _subject;
    private Group _callerPrincipal;

    /**
     * Constructor.
     *
     * @param r The registry to use for resource lookup.
     */
    public CCCLoginModule(final Registry r) {
        _r = r;
    }

    /**
     * Constructor.
     */
    public CCCLoginModule() { /* No-op */ }


    /** {@inheritDoc} */
    @Override
    public boolean abort() throws LoginException {
        _cbHandler = null;
        _subject = null;
        _roles = null;
        _user = null;
        _roleGroup = null;
        _callerPrincipal = null;

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean commit() throws LoginException {
        _subject.getPrincipals().add(_callerPrincipal);
        _subject.getPrincipals().add(_roleGroup);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final Subject subject,
                           final CallbackHandler callbackHandler,
                           final Map<String, ?> sharedState,
                           final Map<String, ?> options) {
        _subject = subject;
        _cbHandler = callbackHandler;
    }

    /** {@inheritDoc} */
    @Override
    public boolean login() throws LoginException {
        try {
            final NameCallback nc =
                new NameCallback("User name: "); // Default name is null
            final PasswordCallback pc =
                new PasswordCallback("Password: ", false);
            final Callback[] callbacks = {nc, pc};
            _cbHandler.handle(callbacks);

            _user = lookupUser(nc.getName());
            if (null==_user) {

                _roles = new HashSet<String>();
                final UUID id = UUID.randomUUID();
                _callerPrincipal = createCallerPrincipal(id.toString());
                _roleGroup = createRoles(_roles);

            } else {

                _roles = lookupRoles((String) _user[0]);

                _callerPrincipal = createCallerPrincipal((String) _user[0]);
                _roleGroup = createRoles(_roles);

                return
                    Password.matches(
                        (byte[]) _user[1],
                        new String(pc.getPassword()),
                        (String) _user[2]);
            }

            return true;

        } catch (final Exception e) {
            throw new LoginException("login failed: "+e.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean logout() throws LoginException {
        _subject.getPrincipals().remove(_roleGroup);
        return true;
    }

    /**
     * Create a JAAS group representing a user's roles.
     *
     * @param roles The roles represented as strings.
     * @return The roles represented as a JAAS group.
     */
    public Group createRoles(final Collection<String> roles) {
        final Group roleGroup = new SimpleGroup("Roles");
        for (final String role : roles) {
            roleGroup.addMember(new SimplePrincipal(role));
        }
        return roleGroup;
    }

    /**
     * Create a JAAS principal representing the user.
     *
     * @param name The name for the principal.
     * @return The JAAS principal.
     */
    public Group createCallerPrincipal(final String name) {
        final Group cpGroup = new SimpleGroup("CallerPrincipal");
        cpGroup.addMember(new SimplePrincipal(name));
        return cpGroup;
    }

    /**
     * Lookup details of a user from the DB. Details are returned in object
     * array with the following structure:
     * <br>[0] user id,         as a string
     * <br>[1] hashed password, as a byte array
     * <br>[2] password salt,   as a string
     *
     * @param username The username representing the user to look up.
     * @return The user data as an object array.
     * @throws SQLException If an error occurs while communicating with the DB.
     */
    public Object[] lookupUser(final String username) throws SQLException {

        if (null==username) {
            return null;
        }

        final Object[] result = new Object[3];
        final DataSource ds = _r.get("java:/ccc");
        final Connection c = ds.getConnection();

        try { // Work with the Connection, close on error.
            final PreparedStatement s = c.prepareStatement(SQL_LOOKUP_USER);

            try { // Work with the Statement, close on error.
                s.setString(1, username);
                final ResultSet rs = s.executeQuery();

                try { // Work with the ResultSet, close on error.
                    rs.next();
                    result[0] = rs.getString(1);
                    result[1] = rs.getBytes(2);
                    result[2] = rs.getString(3);
                } finally {
                    try {
                        rs.close();
                    } catch (final SQLException e) {
                        swallow(e);
                    }
                }

            } finally {
                try {
                    s.close();
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

        return result;
    }

    /**
     * Look up a user's roles from the DB..
     *
     * @param userId The user id whose roles we will retrieve.
     * @return A set of roles, represented as strings.
     * @throws SQLException If an error occurs while communicating with the DB.
     */
    public Set<String> lookupRoles(final String userId) throws SQLException {
        final Set<String> result = new HashSet<String>();
        final DataSource ds = _r.get("java:/ccc");
        final Connection c = ds.getConnection();

        try { // Work with the Connection, close on error.
            final PreparedStatement s = c.prepareStatement(SQL_LOOKUP_ROLES);

            try { // Work with the Statement, close on error.
                s.setString(1, userId);
                final ResultSet rs = s.executeQuery();

                try { // Work with the ResultSet, close on error.
                    while (rs.next()) {
                        result.add(rs.getString(1));
                    }
                } finally {
                    try {
                        rs.close();
                    } catch (final SQLException e) {
                        swallow(e);
                    }
                }

            } finally {
                try {
                    s.close();
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

        return result;
    }

}
