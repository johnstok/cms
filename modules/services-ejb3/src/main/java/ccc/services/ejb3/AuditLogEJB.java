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

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commons.DBC;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.services.AuditLogLocal;
import ccc.services.UserManagerLocal;


/**
 * EJB3 implementation of {@link AuditLog}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="AuditLog")
@TransactionAttribute(REQUIRED)
@Local(AuditLogLocal.class)
public class AuditLogEJB
    implements
        AuditLogLocal {

    @PersistenceContext(unitName="ccc-persistence")
    private EntityManager _em;
    @EJB(name="UserManager", beanInterface=UserManagerLocal.class)
    private UserManagerLocal _um;


    /** Constructor. */
    @SuppressWarnings("unused") private AuditLogEJB() { super(); }

    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     * @param um The user manager used to determine the logged in user.
     */
    AuditLogEJB(final EntityManager em, final UserManagerLocal um) {
        DBC.require().notNull(em);
        DBC.require().notNull(um);

        _em = em;
        _um = um;
    }

    /** {@inheritDoc} */
    @Override
    public void recordLock(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forLock(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUnlock(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUnlock(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordCreate(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forCreate(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordChangeTemplate(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forTemplateChange(resource,
                                       _um.loggedInUser(),
                                       new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdate(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUpdate(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordMove(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forMove(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordRename(final Resource resource) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forRename(resource, _um.loggedInUser(), new Date());
        _em.persist(le);
    }


}
