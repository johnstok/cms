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

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
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
    @SuppressWarnings("unchecked")
    @Override
    public List<User> listUsers() {
        final Query q =
            _em.createQuery(NamedQueries.ALL_USERS.queryString());
        return q.getResultList();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> listUsersWithRole(final CreatorRoles role) {
        final Query q =
            _em.createQuery(NamedQueries.USERS_WITH_ROLE.queryString());
        q.setParameter("role", role.name());

        return q.getResultList();
    }


    /**
     * Available named queries.
     *
     * @author Civic Computing Ltd.
     */
    static enum NamedQueries {

        /** ALL_USERS : NamedQueries. */
        ALL_USERS("from ccc.domain.User"),

        /** USERS_WITH_ROLE : NamedQueries. */
        USERS_WITH_ROLE("from ccc.domain.User u where :role in elements(u._roles)");

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

}
