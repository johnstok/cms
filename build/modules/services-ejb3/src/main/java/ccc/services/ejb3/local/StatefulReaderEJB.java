/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;
import ccc.services.StatefulReader;


/**
 * EJB3 implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
@Stateful(name=StatefulReader.NAME)
@TransactionAttribute(REQUIRED)
@Local(StatefulReader.class)
public final class StatefulReaderEJB
    implements
        StatefulReader {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type=PersistenceContextType.EXTENDED)
    @SuppressWarnings("unused")
    private EntityManager _em; // Required to insure method calls are stateful.

    @EJB(name=AuditLog.NAME)     private AuditLog     _log;
    @EJB(name=ResourceDao.NAME)  private ResourceDao  _resources;

    /** Constructor. */
    @SuppressWarnings("unused") public StatefulReaderEJB() { super(); }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    StatefulReaderEJB(final EntityManager entityManager) {
        _em = entityManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String rootName, final ResourcePath path) {
        return _resources.lookup(rootName, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String id) {
        return _resources.find(Resource.class, UUID.fromString(id));
    }


    /** {@inheritDoc} */
    @Override
    public LogEntry lookup(final long index) {
        return _log.findEntryForIndex(index);
    }

    /** {@inheritDoc} */
    @Override
    public String absolutePath(final String legacyId) {
        final Resource r = _resources.lookupWithLegacyId(legacyId);
        return (null==r) ? null : r.absolutePath().toString();
    }

    /** {@inheritDoc} */
    @Override
    @Remove
    public void close() {
        // Nothing to do
    }
}
