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
package ccc.services.ejb3.remote;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.actions.Action;
import ccc.services.Scheduler;
import ccc.services.ejb3.local.IActionExecutor;
import ccc.services.ejb3.support.Dao;


/**
 * EJB implementation of the {@link Scheduler}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="Scheduler")
@TransactionAttribute(REQUIRED)
@Local(Scheduler.class)
public class SchedulerEJB implements Scheduler {
    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;
    private static final String TIMER_NAME = "action_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SchedulerEJB.class.getName());

    @Resource private EJBContext _context;
    @EJB(name="ActionExecutor") private IActionExecutor _executor;
    @EJB(name="Dao") private Dao _dao;

    /** Constructor. */
    @SuppressWarnings("unused") public SchedulerEJB() { super(); }


    /**
     * Run the scheduled action.
     *
     * @param timer The timer that called this method.
     */
    @Timeout
    public void run(@SuppressWarnings("unused") final Timer timer) {
        executeAction();
    }


    /** {@inheritDoc} */
    @Override
    public void executeAction() {
        LOG.debug("Executing scheduled actions.");
        final List<Action> actions =
            _dao.list("latest_action", Action.class, new Date());
        LOG.debug("Actions to execute: "+actions.size());
        try {
            if (actions.size() > 0) {
                _executor.executeAction(actions.get(0));
            }
        } catch (final RuntimeException e) {
            LOG.warn("Error executing action.", e);
            throw e;
        }
    }


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
    public void schedule(final Action action) {
        _dao.create(action);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<Action> pending() {
        return _dao.list("pending", Action.class);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<Action> executed() {
        return _dao.list("executed", Action.class);
    }


    /** {@inheritDoc} */
    @Override
    public void cancel(final UUID actionId) {
        _dao.find(Action.class, actionId).cancel();
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
}
