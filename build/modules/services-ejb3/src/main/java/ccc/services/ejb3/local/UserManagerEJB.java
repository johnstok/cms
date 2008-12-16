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

import static javax.ejb.TransactionAttributeType.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ccc.domain.CreatorRoles;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseDao;


/**
 * EJB implementation of the UserManager API.
 * TODO: Confirm Locale.US is a sensible locale for lower-casing.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="UserManager")
@TransactionAttribute(REQUIRED)
@Local(UserManager.class)
public class UserManagerEJB extends BaseDao implements UserManager {

    @Resource private EJBContext _context;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    public UserManagerEJB() { /* NO-OP */ }

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
    @Override
    public Collection<User> listUsers() {
        return uniquify(list("users", User.class));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final String searchParam =
            (null==username) ? "" : username.toLowerCase(Locale.US);
        return list("usersWithUsername", User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final String searchParam =
            (null==email) ? "" : email.toLowerCase(Locale.US);
        return list("usersWithEmail", User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithRole(final CreatorRoles role) {
        return uniquify(list("usersWithRole", User.class, role.name()));
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        return exists(find("usersWithUsername", User.class, username));
    }

    /** {@inheritDoc} */
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
        final User user = find("usersWithUsername", User.class, principalName);
        return user;
    }
}
