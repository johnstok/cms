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

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.StatefulReader;
import ccc.services.ejb3.support.QueryNames;


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
        final Folder root = lookupRoot(rootName);
        if (null==root) {
            return null;
        }
        try {
            return root.navigateTo(path);
        } catch (final CCCException e) {
            return null;
        }
    }

    private Folder lookupRoot(final String rootName) {
        final Query q = _em.createNamedQuery(QueryNames.ROOT_BY_NAME);
        q.setParameter(1, new ResourceName(rootName));

        try {
            final Object singleResult = q.getSingleResult();
            final Folder folder = Folder.class.cast(singleResult);
            return folder;
        } catch (final NoResultException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Resource lookup(final UUID resourceId) {
        return _em.find(Resource.class, resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public LogEntry lookup(final long index) {
        // FIXME: Duplicate of AuditLog.findEntryForIndex().
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
