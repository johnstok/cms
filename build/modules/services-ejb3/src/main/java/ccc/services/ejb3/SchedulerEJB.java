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
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.action.ActionExecutor;
import ccc.action.ActionExecutorImpl;
import ccc.commands.CancelActionCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.domain.Action;
import ccc.domain.Scheduler;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.QueryNames;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.Actions;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.migration.ResourcesExt;


/**
 * EJB implementation of the {@link Scheduler}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Actions.NAME)
@TransactionAttribute(REQUIRED)
@Local(Actions.class)
@Remote(Scheduler.class)
@RolesAllowed({ADMINISTRATOR})
@RunAs(CONTENT_CREATOR)
public class SchedulerEJB
    extends
        BaseCommands
    implements
        Scheduler,
        Actions {

    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;
    private static final String TIMER_NAME = "action_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SchedulerEJB.class.getName());

    @Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;
    @EJB(name=ResourcesExt.NAME) private ResourcesExt _resourcesExt;

    private ActionExecutor _executor;
    private LogEntryRepository _audit;

    /** Constructor. */
    public SchedulerEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    public void executeAction() {
        LOG.debug("Executing scheduled actions.");
        final List<Action> actions =
            _bdao.list(
                QueryNames.LATEST_ACTION, Action.class, new Date());
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
    public Collection<ActionSummary> listPendingActions() {
        return mapActions(_bdao.list(QueryNames.PENDING, Action.class));
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return mapActions(_bdao.list(QueryNames.EXECUTED, Action.class));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void cancelAction(final UUID actionId) {
        new CancelActionCommand(_bdao, _audit).execute(
            loggedInUser(_context), new Date(), actionId);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createAction(final ActionDto action) {
      final Action a =
          new Action(
              action.getCommand(),
              action.getExecuteAfter(),
              loggedInUser(_context),
              _bdao.find(
                  ccc.domain.Resource.class, action.getResourceId()),
              action.getParameters());

      new ScheduleActionCommand(_bdao, _audit).execute(
          loggedInUser(_context), new Date(), a);
    }



    /* ====================================================================
     * Scheduling implementation.
     * ================================================================== */


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
        executeAction();
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _executor = new ActionExecutorImpl(_resourcesExt);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
    }
}
