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

import org.apache.log4j.Logger;

import ccc.api.core.Actions2;
import ccc.api.core.Scheduler;
import ccc.api.types.DBC;
import ccc.plugins.security.Sessions;


/**
 * Action scheduler implementation that uses the JRE {@link Timer} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionScheduler
    implements
        Scheduler {

    private static final Logger LOG = Logger.getLogger(ActionScheduler.class);

    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;

    private final Timer        _actionTimer = new Timer(true);
    private final Actions2     _actions;
    private final Sessions     _sessions;

    private ExecuteActionsTask _task;


    /**
     * Constructor.
     *
     * @param actions The actions implementation to call.
     * @param session The session under which tasks will run.
     *
     */
    public ActionScheduler(final Actions2 actions, final Sessions session) {
        _actions = DBC.require().notNull(actions);
        _sessions = DBC.require().notNull(session);
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
            _task = new ExecuteActionsTask(_actions, _sessions);
            _actionTimer.schedule(
                _task, INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS);
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
}
