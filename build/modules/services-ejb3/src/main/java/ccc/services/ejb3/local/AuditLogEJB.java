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

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.commons.DBC;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.ejb3.support.QueryNames;


/**
 * EJB3 implementation of {@link AuditLog}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=AuditLog.NAME)
@TransactionAttribute(REQUIRED)
@Local(AuditLog.class)
public class AuditLogEJB
    implements
        AuditLog {

    @PersistenceContext(unitName="ccc-persistence")
    private EntityManager _em;


    /** Constructor. */
    @SuppressWarnings("unused") public AuditLogEJB() { super(); }

    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    AuditLogEJB(final EntityManager em) {
        DBC.require().notNull(em);
        _em = em;
    }

    /** {@inheritDoc} */
    @Override
    public void recordLock(final Resource resource,
                           final User actor,
                           final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forLock(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUnlock(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUnlock(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordCreate(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forCreate(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordChangeTemplate(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forTemplateChange(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdate(final Resource resource,
                             final User actor,
                             final Date happenedOn,
                             final String comment,
                             final boolean isMajorEdit) {
        DBC.require().notNull(resource);
        final LogEntry le = LogEntry.forUpdate(
            resource,
            actor,
            happenedOn,
            comment,
            isMajorEdit);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdate(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le = LogEntry.forUpdate(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordMove(final Resource resource,
                           final User actor,
                           final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forMove(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordRename(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forRename(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordPublish(final Resource resource,
                              final User actor,
                              final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forPublish(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUnpublish(final Resource resource,
                                final User actor,
                                final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUnpublish(resource, actor, happenedOn);
        _em.persist(le);
    }

    /** {@inheritDoc} */
    @Override
    public LogEntry findEntryForIndex(final long index) {
        final Query q = _em.createNamedQuery(QueryNames.LOG_ENTRY_BY_ID);
        q.setParameter(1, new Long(index));

        try {
            final Object singleResult = q.getSingleResult();
            final LogEntry le = LogEntry.class.cast(singleResult);
            return le;
        } catch (final NoResultException e) {
            return null;
        }
    }
}
