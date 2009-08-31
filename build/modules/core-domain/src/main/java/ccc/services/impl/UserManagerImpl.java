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

import static ccc.persistence.QueryNames.*;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import ccc.domain.User;
import ccc.persistence.Repository;
import ccc.persistence.UserManager;


/**
 * EJB implementation of the UserManager API.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerImpl implements UserManager {

    private Repository _repository;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     */
    public UserManagerImpl(final Repository repository) {
        _repository = repository;
    }


    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsers() {
        return _repository.uniquify(USERS, User.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final String searchParam =
            (null==username) ? "" : username.toLowerCase(Locale.US);
        return _repository.list(USERS_WITH_USERNAME, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final String searchParam =
            (null==email) ? "" : email.toLowerCase(Locale.US);
        return _repository.list(USERS_WITH_EMAIL, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithRole(final String role) {
        return _repository.uniquify(USERS_WITH_ROLE, User.class, role);
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        return _repository.exists(USERS_WITH_USERNAME, User.class, username);
    }

    /** {@inheritDoc} */
    @Override
    public User find(final UUID userId) {
        return _repository.find(User.class, userId);
    }
}
