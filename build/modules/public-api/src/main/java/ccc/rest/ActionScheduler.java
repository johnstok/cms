/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
