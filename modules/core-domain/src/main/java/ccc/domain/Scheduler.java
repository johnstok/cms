/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;




/**
 * API for a scheduler.
 *
 * @author Civic Computing Ltd.
 */
public interface Scheduler {

    /**
     * Start the scheduler running.
     */
    void start();

    /**
     * Stop the scheduler running.
     */
    void stop();

    /**
     * Query whether the scheduler is running.
     *
     * @return True if the scheduler is running; false otherwise.
     */
    boolean isRunning();
}
