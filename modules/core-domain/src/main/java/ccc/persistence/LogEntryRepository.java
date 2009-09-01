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

import ccc.domain.LogEntry;


/**
 * API for log entry repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface LogEntryRepository {

    /**
     * Record a log entry.
     *
     * @param le The log entry to record.
     */
    void record(LogEntry le);
}
