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
 * Revision      $Rev: 1946 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-10-05 17:07:14 +0100 (Mon, 05 Oct 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.api.ActionScheduler;
import ccc.api.Actions;
import ccc.api.Scheduler;


/**
 * EJB implementation of the {@link Scheduler}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=ActionScheduler.NAME)
@TransactionAttribute(REQUIRED)
@Local(ActionScheduler.class)
@RolesAllowed({ACTION_SCHEDULE})
public class ActionsSchedulerEJB
    implements
        ActionScheduler {

    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;
    private static final String TIMER_NAME = "action_scheduler";
    private static final Logger LOG =
        Logger.getLogger(ActionsSchedulerEJB.class.getName());

    @EJB(name = Actions.NAME)  private transient Actions _action;
    @javax.annotation.Resource private EJBContext _context;


    /** Constructor. */
    public ActionsSchedulerEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    public void start() {
        LOG.debug("Starting scheduler.");
        _context.getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started scheduler.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void stop() {
        LOG.debug("Stopping scheduler.");
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                t.cancel();
            }
        }
        LOG.debug("Stopped scheduler.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public boolean isRunning() {
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Run the scheduled action.
     *
     * @param timer The timer that called this method.
     */
    @Timeout
    public void run(@SuppressWarnings("unused") final Timer timer) {
        _action.executeAll();
    }
}
