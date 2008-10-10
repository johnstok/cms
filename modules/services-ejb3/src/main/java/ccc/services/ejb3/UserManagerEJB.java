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
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;
import static javax.persistence.PersistenceContextType.*;

import java.util.Collection;
import java.util.HashSet;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CreatorRoles;
import ccc.domain.User;
import ccc.services.UserManagerLocal;
import ccc.services.UserManagerRemote;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateful(name="UserManager")
@TransactionAttribute(REQUIRED)
@Remote(UserManagerRemote.class)
@Local(UserManagerLocal.class)
public class UserManagerEJB implements UserManagerRemote, UserManagerLocal {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _em;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private UserManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param em A JPA entity manager.
     */
    public UserManagerEJB(final EntityManager em) {
        _em = em;
    }

    /** {@inheritDoc} */
    @Override
    public void createUser(final User user) {
        _em.persist(user);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsers() {
        final Query q =
            _em.createQuery(NamedQueries.ALL_USERS.queryString());
        return uniquify(q.getResultList());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final Query q =
            _em.createQuery(NamedQueries.USERS_WITH_USERNAME.queryString());
        String searchParam = "";
        if (username != null) {
            searchParam = username;
        }
        q.setParameter("username", searchParam.toLowerCase());

        return q.getResultList();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final Query q =
            _em.createQuery(NamedQueries.USERS_WITH_EMAIL.queryString());
        String searchParam = "";
        if (email != null) {
            searchParam = email;
        }
        q.setParameter("email", searchParam.toLowerCase());

        return q.getResultList();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithRole(final CreatorRoles role) {
        final Query q =
            _em.createQuery(NamedQueries.USERS_WITH_ROLE.queryString());
        q.setParameter("role", role.name());

        return uniquify(q.getResultList());
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        final Query q =
            _em.createQuery(NamedQueries.USERS_WITH_USERNAME.queryString());
        q.setParameter("username", username);
        try {
            q.getSingleResult();
            return true;
        } catch (final NoResultException e) {
            return false;
        }
    }

    private <T> Collection<T> uniquify(final Collection<T> collection) {
        return new HashSet<T>(collection);
    }

    /**
     * Available named queries.
     *
     * @author Civic Computing Ltd.
     */
    static enum NamedQueries {

        /** ALL_USERS : NamedQueries. */
        ALL_USERS("from ccc.domain.User u left join fetch u._roles"),

        /** USERS_WITH_ROLE : NamedQueries. */
        USERS_WITH_ROLE(
            "from ccc.domain.User u "
            + "left join fetch u._roles "
            + "where :role in elements(u._roles)"),

        /** USERS_WITH_USERNAME : NamedQueries. */
        USERS_WITH_USERNAME(
        "from ccc.domain.User u where lower(u._username) like :username"),

        /** USERS_WITH_EMAIL : NamedQueries. */
        USERS_WITH_EMAIL(
        "from ccc.domain.User u where lower(u._email) like :email");

        private final String _queryString;

        private NamedQueries(final String qString) {
            _queryString = qString;
        }

        /**
         * Accessor for the query string.
         *
         * @return The JPA query as a string.
         */
        String queryString() {
            return _queryString;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final User user) {
        final User current = _em.find(User.class, user.id());
        current.username(user.username());
        current.email(user.email());
    }
}
