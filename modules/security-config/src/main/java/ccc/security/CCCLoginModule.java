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

import java.security.acl.Group;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

import ccc.domain.Password;



/**
 * A JAAS login module for authenticating CCC users.
 *
 * @author Civic Computing Ltd.
 */
public class CCCLoginModule implements LoginModule {
    private static final Logger LOG = Logger.getLogger(CCCLoginModule.class);

    private CallbackHandler _cbHandler;
    private Object[] _user;
    private Set<String> _roles;
    private Group _roleGroup;
    private Subject _subject;
    private Group _callerPrincipal;
    private Database _db;

    /**
     * Constructor.
     */
    public CCCLoginModule() {
        this(new JdbcDatabase());
    }

    /**
     * Constructor.
     *
     * @param db The database to search for users.
     */
    public CCCLoginModule(final Database db) {
        _db = db;
    }


    /** {@inheritDoc} */
    @Override
    public boolean abort() {
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
    public boolean commit() {
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
        _db.setOptions(options);
    }

    /** {@inheritDoc} */
    @Override
    public boolean login() throws LoginException {
        try { // 50ms wait impedes brute force password attacks.
            Thread.sleep(50);
        } catch (final InterruptedException e) {
            return false;
        }

        try {
            final NameCallback nc = new NameCallback("Name");
            final PasswordCallback pc = new PasswordCallback("Password", false);
            final Callback[] callbacks = {nc, pc};
            _cbHandler.handle(callbacks);

            _user = _db.lookupUser(nc.getName());

            if (null==_user) { // Anonymous logins disallowed
                LOG.debug("No user in db with username: "+nc.getName());
                return false;
            }
            LOG.debug("Found user in db with username: "+nc.getName());

            _roles = _db.lookupRoles((String) _user[0]);
            LOG.debug("User "+nc.getName()+" has roles: "+_roles);

            _callerPrincipal = createCallerPrincipal(nc.getName());
            _roleGroup = createRoles(_roles);

            final boolean passwordOk =
                Password.matches(
                    (byte[]) _user[1],
                    new String(pc.getPassword()),
                    (String) _user[2]);
            LOG.debug("User "+nc.getName()+" password ok? "+passwordOk);

            return passwordOk;


        } catch (final Exception e) {
            LOG.error("Login failed", e); // TODO: do we need to log & throw?
            throw new LoginException("login failed: "+e.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean logout() {
        _subject.getPrincipals().remove(_roleGroup);
        _subject.getPrincipals().remove(_callerPrincipal);
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
     * Accessor.
     *
     * @return Returns the _cbHandler.
     */
    CallbackHandler getCbHandler() {
        return _cbHandler;
    }


    /**
     * Accessor.
     *
     * @return Returns the _user.
     */
    Object[] getUser() {
        return _user;
    }


    /**
     * Accessor.
     *
     * @return Returns the _roles.
     */
    Set<String> getRoles() {
        return _roles;
    }


    /**
     * Accessor.
     *
     * @return Returns the _roleGroup.
     */
    Group getRoleGroup() {
        return _roleGroup;
    }


    /**
     * Accessor.
     *
     * @return Returns the _subject.
     */
    Subject getSubject() {
        return _subject;
    }


    /**
     * Accessor.
     *
     * @return Returns the _callerPrincipal.
     */
    Group getCallerPrincipal() {
        return _callerPrincipal;
    }


    /**
     * Accessor.
     *
     * @return Returns the _db.
     */
    Database getDb() {
        return _db;
    }
}
