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

import org.apache.log4j.Logger;

import ccc.domain.LogEntry;
import ccc.types.DBC;


/**
 * Implementation of {@link AuditLog}.
 *
 * @author Civic Computing Ltd.
 */
public class AuditLogImpl
    implements
        AuditLog {
    private static final Logger LOG = Logger.getLogger(AuditLogImpl.class);

    private Repository _em;

    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    public AuditLogImpl(final Repository em) {
        DBC.require().notNull(em);
        _em = em;
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
