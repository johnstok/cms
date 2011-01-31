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

import ccc.api.core.Actions2;
import ccc.api.types.DBC;
import ccc.api.types.Permission;
import ccc.plugins.security.Sessions;
import ccc.web.jaxrs.CCCProperties;


/**
 * Scheduler for action execution.
 *
 * @author Civic Computing Ltd.
 */
public class ActionScheduler
    extends
        AbstractScheduler {

    private final Actions2 _actions;
    private final Sessions _sessions;


    /**
     * Constructor.
     *
     * @param actions The actions implementation to call.
     * @param session The session under which tasks will run.
     *
     */
    public ActionScheduler(final Actions2 actions, final Sessions session) {
        super("CC-actions-"+CCCProperties.getAppName());
        _actions = DBC.require().notNull(actions);
        _sessions = DBC.require().notNull(session);
    }


    /** {@inheritDoc} */
    @Override
    void schedule() {
        final int delay = 30*1000;
        final int period = 60*1000;

        final Runnable r =
            new RunAsRunnable(
                _sessions,
                Permission.ACTION_EXECUTE,
                new ExecuteActionsTask(_actions));

        schedule(r, period, delay);
    }
}
