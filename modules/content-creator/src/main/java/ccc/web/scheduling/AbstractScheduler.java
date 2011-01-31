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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ccc.api.core.Scheduler;
import ccc.api.types.DBC;


/**
 * Scheduler implementation that uses the JRE {@link Timer} class.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractScheduler
    implements
        Scheduler {

    private static final Logger LOG = Logger.getLogger(ActionScheduler.class);

    private final Timer _actionTimer;
    private TimerTask _task;


    /**
     * Constructor.
     *
     * @param name The name of the scheduler.
     */
    public AbstractScheduler(final String name) {
        _actionTimer =
            new Timer(DBC.require().notEmpty(name), true);
    }


    /** {@inheritDoc} */
    @Override
    public synchronized boolean isRunning() {
        return null!=_task;
    }


    /** {@inheritDoc} */
    @Override
    public synchronized void start() {
        LOG.debug("Starting scheduler.");

        // TODO: Check for termination?
        if (isRunning()) {
            LOG.debug("Scheduler already running.");
        } else {
            schedule();
            LOG.debug("Started scheduler.");
            // Handle IllegalStateException for stopped timer thread?
        }
    }


    /** {@inheritDoc} */
    @Override
    public synchronized void stop() {
        LOG.debug("Stopping scheduler.");

        // TODO: Check for termination?
        if (!isRunning()) {
            LOG.debug("Scheduler already stopped.");
        } else {
            _task.cancel();
            _task = null;
            LOG.debug("Stopped scheduler.");
        }
    }


    /**
     * Terminates this scheduler - it may not be used again.
     */
    public synchronized void cancel() {
        stop();
        _actionTimer.cancel();
    }


    /**
     * Schedule a task.
     *
     * @param task   The task to perform.
     * @param period The period between invocations of the task.
     * @param delay  The delay before the first time the task is invoked.
     */
    void schedule(final Runnable task, final int period, final int delay) {
        _task = new RunnableTimerTask(task);
        _actionTimer.schedule(_task, delay, period);
    }


    /**
     * Schedule a task.
     */
    abstract void schedule();
}