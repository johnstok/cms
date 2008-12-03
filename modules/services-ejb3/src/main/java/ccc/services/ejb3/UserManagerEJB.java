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

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CreatorRoles;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.UserManagerLocal;
import ccc.services.UserManagerRemote;


/**
 * EJB implementation of the UserManager API.
 * TODO: Confirm Locale.US is a sensible locale for lower-casing.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="UserManager")
@TransactionAttribute(REQUIRED)
@Remote(UserManagerRemote.class)
@Local(UserManagerLocal.class)
public class UserManagerEJB implements UserManagerRemote, UserManagerLocal {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _em;
    @Resource private EJBContext _context;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private UserManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param em A JPA entity manager.
     * @param context The j2ee context within which this ejb operates.
     */
    public UserManagerEJB(final EntityManager em,
                          final EJBContext context) {
        _em = em;
        _context = context;
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public User createUser(final User user, final String password) {
        _em.persist(user);
        final Password defaultPassword = new Password(user, password);
        _em.persist(defaultPassword);
        return user;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsers() {
        final Query q = _em.createNamedQuery("users");
        return uniquify(q.getResultList());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final Query q = _em.createNamedQuery("usersWithUsername");
        String searchParam = "";
        if (username != null) {
            searchParam = username;
        }
        q.setParameter("username", searchParam.toLowerCase(Locale.US));

        return q.getResultList();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final Query q = _em.createNamedQuery("usersWithEmail");
        String searchParam = "";
        if (email != null) {
            searchParam = email;
        }
        q.setParameter("email", searchParam.toLowerCase(Locale.US));

        return q.getResultList();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    public Collection<User> listUsersWithRole(final CreatorRoles role) {
        final Query q = _em.createNamedQuery("usersWithRole");
        q.setParameter("role", role.name());

        return uniquify(q.getResultList());
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        final Query q = _em.createNamedQuery("usersWithUsername");
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

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA API doesn't support generics.
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updateUser(final User user, final String password) {
        final User current = _em.find(User.class, user.id());
        current.username(user.username());
        current.email(user.email());
        current.roles(user.roles());
        if (password != null) {
            Password newPassword;
            final Query q =
                _em.createNamedQuery("passwordForUser");
            q.setParameter("user", user);
            try {
                newPassword = (Password) q.getSingleResult();
            } catch (final NoResultException e) {
                newPassword = null;
            }
            if (newPassword != null) {
                newPassword.password(password);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public User loggedInUser() {
        final Principal p = _context.getCallerPrincipal();
        final String principalName = p.getName();
        final User user = _em.find(User.class, UUID.fromString(principalName));
        return user;
    }
}
