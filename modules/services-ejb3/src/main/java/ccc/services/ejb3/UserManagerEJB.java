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

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.CreatorRoles;
import ccc.domain.User;
import ccc.services.UserManagerRemote;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="UserManager")
@TransactionAttribute(REQUIRED)
@Remote(UserManagerRemote.class)
public class UserManagerEJB implements UserManagerRemote {

    @PersistenceContext(unitName = "ccc-persistence")
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
    @Override
    public List<User> listUsers() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public List<User> listUsersWithRole(final CreatorRoles role) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

}
