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
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
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
@Remote(Scheduler.class)
public class SchedulerEJB implements Scheduler {
    private static final Logger LOG =
        Logger.getLogger(SchedulerEJB.class.getName());
    private static final String SCHEDULER = "scheduler";

    @Resource private EJBContext _context;
    @EJB(name="ActionExecutor") private IActionExecutor _executor;
    @EJB(name="Dao") private Dao _dao;

    /** Constructor. */
    @SuppressWarnings("unused") public SchedulerEJB() { super(); }


    @Timeout
    public void executeActions(final Timer timer) {
        LOG.debug("Executing scheduled actions.");
        final List<Action> actions = _dao.list("latest_action", Action.class); // FIXME should pass 'now'
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
        _context.getTimerService().createTimer(30*1000, 60*1000, SCHEDULER);
        LOG.debug("Started scheduler.");
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void stop() {
        LOG.debug("Stopping scheduler.");
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (SCHEDULER.equals(t.getInfo())) {
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
}
