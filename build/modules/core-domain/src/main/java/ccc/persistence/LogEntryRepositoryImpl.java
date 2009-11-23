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
package ccc.persistence;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import ccc.domain.LogEntry;
import ccc.types.DBC;


/**
 * Implementation of {@link LogEntryRepository}.
 *
 * @author Civic Computing Ltd.
 */
class LogEntryRepositoryImpl
    implements
        LogEntryRepository {
    private static final Logger LOG =
        Logger.getLogger(LogEntryRepositoryImpl.class);

    private Repository _em;


    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    public LogEntryRepositoryImpl(final Repository em) {
        DBC.require().notNull(em);
        _em = em;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public LogEntryRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override
    public void record(final LogEntry le) {
        _em.create(le);
        log(le);
    }


    private void log(final LogEntry le) {
        LOG.info(
            "Action: " + le.action()
            + " for " + le.subjectId()
            + " by " + le.actor().username());
    }
}
