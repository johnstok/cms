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

import java.util.TimerTask;

import org.apache.log4j.Logger;

import ccc.api.types.DBC;


/**
 * Timer task that invokes a {@link Runnable} object.
 *
 * @author Civic Computing Ltd.
 */
public class RunnableTimerTask
    extends
        TimerTask {

    private static final Logger LOG = Logger.getLogger(RunnableTimerTask.class);

    private final Runnable _runnable;


    /**
     * Constructor.
     *
     * @param runnable The runnable object the task will invoke.
     */
    public RunnableTimerTask(final Runnable runnable) {
        _runnable = DBC.require().notNull(runnable);
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        try {
            _runnable.run();
        } catch (final RuntimeException e) {
            LOG.error("Error executing task.", e);
        }
    }
}
