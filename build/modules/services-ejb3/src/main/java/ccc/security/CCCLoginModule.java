/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.types.Username;
import ccc.domain.UserEntity;



/**
 * A JAAS login module for authenticating CCC users.
 *
 * @author Civic Computing Ltd.
 */
public class CCCLoginModule implements LoginModule {
    /** LOGIN_DELAY : int. */
    private static final int LOGIN_DELAY = 50;

    private static final Logger LOG = Logger.getLogger(CCCLoginModule.class);

    private CallbackHandler _cbHandler;
    private Object[] _user;
    private Set<String> _permissions;
    private Group _permGroup;
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
        _permissions = null;
        _user = null;
        _permGroup = null;
        _callerPrincipal = null;

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean commit() {
        _subject.getPrincipals().add(_callerPrincipal);
        _subject.getPrincipals().add(_permGroup);
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
        try { // Wait impedes brute force password attacks.
            Thread.sleep(LOGIN_DELAY);
        } catch (final InterruptedException e) {
            return false;
        }

        try {
            final NameCallback nc = new NameCallback("Name");
            final PasswordCallback pc = new PasswordCallback("Password", false);
            final Callback[] callbacks = {nc, pc};
            _cbHandler.handle(callbacks);

            _user = _db.lookupUser(new Username(nc.getName()));

            if (null==_user) { // Anonymous logins disallowed
                LOG.debug("No user in db with username: "+nc.getName());
                return false;
            }
            LOG.debug("Found user in db with username: "+nc.getName());

            _permissions = _db.lookupPerms((String) _user[0]);
            LOG.debug("User "+nc.getName()+" has permissions: "+_permissions);

            _callerPrincipal = createCallerPrincipal(nc.getName());
            _permGroup = createPerms(_permissions);

            final boolean passwordOk =
                UserEntity.matches(
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
        _subject.getPrincipals().remove(_permGroup);
        _subject.getPrincipals().remove(_callerPrincipal);
        return true;
    }

    /**
     * Create a JAAS group representing a user's permissions.
     *
     * @param perms The permissions represented as strings.
     * @return The permissions represented as a JAAS group.
     */
    public Group createPerms(final Collection<String> perms) {
        final Group permGroup = new SimpleGroup("Roles");
        for (final String perm : perms) {
            permGroup.addMember(new SimplePrincipal(perm));
        }
        return permGroup;
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
     * @return Returns the permissions.
     */
    Set<String> getPermissions() {
        return _permissions;
    }


    /**
     * Accessor.
     *
     * @return Returns the permissions group.
     */
    Group getPermGroup() {
        return _permGroup;
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
