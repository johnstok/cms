/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.scheduling;

import ccc.api.core.Scheduler;


/**
 * Holder class for singleton schedulers.
 *
 * @author Civic Computing Ltd.
 */
public final class Schedulers {

    private static ActionScheduler actionScheduler;

    private Schedulers() { super(); }


    /**
     * Get the current action scheduler.
     *
     * @return The current scheduler or null if none exists.
     */
    public static synchronized Scheduler getInstance() {
        return actionScheduler;
    }


    /**
     * Set the action action scheduler.
     * <br>If a scheduler is already set this method does nothing.
     *
     * @param scheduler The action scheduler to set.
     */
    public static synchronized void setInstance(
                                              final ActionScheduler scheduler) {
        if (null==actionScheduler) { actionScheduler = scheduler; }
    }


    /**
     * Clear the action scheduler.
     */
    public static synchronized void clearInstance() {
        if (null!=actionScheduler) {
            actionScheduler.cancel();
            actionScheduler = null;
        }
    }
}
