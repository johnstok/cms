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
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.action.ActionExecutor;
import ccc.action.ActionExecutorImpl;
import ccc.commands.CancelActionCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.domain.Action;
import ccc.domain.CccCheckedException;
import ccc.domain.Scheduler;
import ccc.rest.Actions;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.extensions.ResourcesExt;


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
public class ActionsEJB
    extends
        AbstractEJB
    implements
        Scheduler,
        Actions {

    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;
    private static final String TIMER_NAME = "action_scheduler";
    private static final Logger LOG =
        Logger.getLogger(ActionsEJB.class.getName());

    @EJB(name=Resources.NAME) private ResourcesExt _resourcesExt;

    private ActionExecutor _executor;

    /** Constructor. */
    public ActionsEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    public void executeAction() {
        LOG.debug("Executing scheduled actions.");
        final List<Action> actions = getActions().latest(new Date());
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
        return mapActions(getActions().pending());
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return mapActions(getActions().completed());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void cancelAction(final UUID actionId) throws RestException {
        try {
            new CancelActionCommand(getActions(), getAuditLog()).execute(
                currentUser(), new Date(), actionId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void createAction(final ActionDto action) throws RestException {
        try {
            final Action a =
                new Action(
                    action.getCommand(),
                    action.getExecuteAfter(),
                    currentUser(),
                    getResources().find(
                        ccc.domain.Resource.class, action.getResourceId()),
                    action.getParameters());

            new ScheduleActionCommand(getActions(), getAuditLog()).execute(
                currentUser(), new Date(), a);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }



    /* ====================================================================
     * Scheduling implementation.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    public void start() {
        LOG.debug("Starting scheduler.");
        getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started scheduler.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void stop() {
        LOG.debug("Stopping scheduler.");
        final Collection<Timer> c = getTimerService().getTimers();
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
        final Collection<Timer> c = getTimerService().getTimers();
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
    private void configureExecutor() {
        _executor = new ActionExecutorImpl(_resourcesExt);
    }
}
