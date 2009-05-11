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

import static ccc.services.QueryNames.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commons.EmailAddress;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.persistence.jpa.BaseDao;
import ccc.services.Dao;
import ccc.services.UserManager;
import ccc.services.api.UserDelta;


/**
 * EJB implementation of the UserManager API.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=UserManager.NAME)
@TransactionAttribute(REQUIRED)
@Local(UserManager.class)
@PermitAll
public class UserManagerEJB implements UserManager {

    @PersistenceContext private EntityManager _em;
    private Dao _dao;


    /** Constructor. */
    public UserManagerEJB() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public UserManagerEJB(final Dao dao) {
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public User createUser(final UserDelta delta, final String password) {
        final User user = new User(delta.getUsername().toString());
        user.email(new EmailAddress(delta.getEmail()));
        user.roles(delta.getRoles());
        _dao.create(user);

        final Password defaultPassword = new Password(user, password);
        _dao.create(defaultPassword);

        return user;
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
    public User updateUser(final UUID userId, final UserDelta delta) {
        final User current = _dao.find(User.class, userId);
        current.username(delta.getUsername().toString());
        current.email(new EmailAddress(delta.getEmail()));
        current.roles(delta.getRoles());
        return current;
    }

    /** {@inheritDoc} */
    @Override
    public User find(final UUID userId) {
        return _dao.find(User.class, userId);
    }

    @PostConstruct
    public void configure() {
        _dao = new BaseDao(_em);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePassword(final UUID userId, final String password) {
        final Password p =
                _dao.find(PASSWORD_FOR_USER, Password.class, userId);
        p.password(password);
    }
}
