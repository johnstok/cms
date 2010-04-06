/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
            "Action: " + le.getAction()
            + " for " + le.getSubjectId()
            + " by " + le.getActor().getUsername());
    }
}
