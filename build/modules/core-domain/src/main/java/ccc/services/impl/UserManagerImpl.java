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
package ccc.services.impl;

import static ccc.services.QueryNames.*;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import ccc.domain.User;
import ccc.services.Dao;
import ccc.services.UserManager;


/**
 * EJB implementation of the UserManager API.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerImpl implements UserManager {

    private Dao _dao;


    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public UserManagerImpl(final Dao dao) {
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsers() {
        return _dao.uniquify(USERS, User.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final String searchParam =
            (null==username) ? "" : username.toLowerCase(Locale.US);
        return _dao.list(USERS_WITH_USERNAME, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final String searchParam =
            (null==email) ? "" : email.toLowerCase(Locale.US);
        return _dao.list(USERS_WITH_EMAIL, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithRole(final String role) {
        return _dao.uniquify(USERS_WITH_ROLE, User.class, role);
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        return _dao.exists(USERS_WITH_USERNAME, User.class, username);
    }

    /** {@inheritDoc} */
    @Override
    public User find(final UUID userId) {
        return _dao.find(User.class, userId);
    }
}
