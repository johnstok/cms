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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.synchronous;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;




/**
 * API for a scheduler.
 *
 * @author Civic Computing Ltd.
 */
public interface Scheduler {

    /**
     * Start the scheduler running.
     */
    @POST @Path(ccc.api.synchronous.ResourceIdentifiers.Scheduler.SCHEDULER)
    void start();

    /**
     * Stop the scheduler running.
     */
    @DELETE @Path(ccc.api.synchronous.ResourceIdentifiers.Scheduler.SCHEDULER)
    void stop();

    /**
     * Query whether the scheduler is running.
     *
     * @return True if the scheduler is running; false otherwise.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.Scheduler.SCHEDULER)
    boolean isRunning();
}
