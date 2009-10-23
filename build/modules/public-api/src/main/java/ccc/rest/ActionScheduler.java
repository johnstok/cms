/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1762 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-09-01 15:17:03 +0100 (Tue, 01 Sep 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest;




/**
 * API for a action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionScheduler {

    /** NAME : String. */
    String NAME = "ActionScheduler";

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
