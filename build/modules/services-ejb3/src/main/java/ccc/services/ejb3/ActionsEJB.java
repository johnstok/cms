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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.Actions;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resources;
import ccc.api.exceptions.CCException;
import ccc.api.types.SortOrder;
import ccc.commands.CancelActionCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.domain.ActionEntity;
import ccc.domain.ResourceEntity;
import ccc.persistence.ActionRepository;
import ccc.rest.extensions.ResourcesExt;


/**
 * EJB implementation of the {@link Actions}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Actions.NAME)
@TransactionAttribute(REQUIRED)
@Local(Actions.class)
@RolesAllowed({})
@RunAs(ACTION_EXECUTE)
public class ActionsEJB
    extends
        AbstractEJB
    implements
        Actions {

    private static final int TIMEOUT_DELAY_SECS = 60*1000;
    private static final int INITIAL_DELAY_SECS = 30*1000;
    private static final String TIMER_NAME = "action_scheduler";
    private static final Logger LOG =
        Logger.getLogger(ActionsEJB.class.getName());

    @javax.annotation.Resource private EJBContext  _context;
    @EJB(name=Resources.NAME) private ResourcesExt _resourcesExt;


    /** Constructor. */
    public ActionsEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_EXECUTE})
    public void executeAll() {
        LOG.debug("Executing scheduled actions.");

        final List<ActionEntity> actions =
            getRepoFactory().createActionRepository().latest(new Date());
        LOG.debug("Actions to execute: "+actions.size());

        for (final ActionEntity action : actions) {
            try {
                _resourcesExt
                    .executeAction(action.getId()); // Executes in nested txn.
                LOG.info("Completed action: "+action.getId());

            } catch (final CCException e) {
                fail(action, e);

            } catch (final EJBException e) {
                final Exception cause = e.getCausedByException();
                if (cause instanceof CCException) {
                    final CCException cce = (CCException) cause;
                    fail(action, cce);
                } else {
                    fail(action, cause);
                }

            } catch (final RuntimeException e) {
                fail(action, e);
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public PagedCollection<ActionSummary> listPendingActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        final ActionRepository actions =
            getRepoFactory().createActionRepository();
        final PagedCollection<ActionSummary> dc =
            new PagedCollection<ActionSummary>(
                actions.countPending(),
                ActionSummary.class,
                ActionEntity.mapActions(
                    actions.pending(sort, sortOrder, pageNo, pageSize)));
        addLinks(dc);

        return dc;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public PagedCollection<ActionSummary> listCompletedActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        final ActionRepository actions =
            getRepoFactory().createActionRepository();
        final PagedCollection<ActionSummary> dc =
            new PagedCollection<ActionSummary>(
                actions.countCompleted(),
                ActionSummary.class,
                ActionEntity.mapActions(actions.completed(
                    sort, sortOrder, pageNo, pageSize)));
        addLinks(dc);

        return dc;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_CANCEL})
    public void cancel(final UUID actionId) {
        new CancelActionCommand(getRepoFactory())
            .execute(currentUser(), new Date(), actionId);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_CREATE})
    public ActionSummary create(final Action action) {
        final ActionEntity a =
            new ActionEntity(
                action.getCommand(),
                action.getExecuteAfter(),
                currentUser(),
                getRepoFactory()
                    .createResourceRepository()
                    .find(ResourceEntity.class, action.getResourceId()),
                action.getParameters());

        new ScheduleActionCommand(getRepoFactory())
            .execute(currentUser(), new Date(), a);

        return a.mapAction();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public ActionSummary retrieve(final UUID actionId) {
        return
            getRepoFactory()
                .createActionRepository()
                .find(actionId).mapAction();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_SCHEDULE})
    public void start() {
        LOG.debug("Starting scheduler.");
        _context.getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started scheduler.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed({ACTION_SCHEDULE})
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
    @RolesAllowed({ACTION_SCHEDULE})
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
        executeAll();
    }


    private void fail(final ActionEntity action, final CCException e) {
        action.fail(e.getFailure());
        LOG.info(
            "Failed action: "
            + action.getId()
            + " exception was "
            + e.getFailure().getExceptionId());
    }


    private void fail(final ActionEntity action, final Exception e) {
        final CCException cce =
            new CCException("Unexpected error.", e);
        LOG.warn(
            "Unexpected error executing action: "
            + cce.getFailure().getExceptionId(),
            e);
        fail(action, cce);
    }


    private void addLinks(final PagedCollection<ActionSummary> actions) {
        actions.addLink(
            "self",
            ccc.api.core.ResourceIdentifiers.Action.COLLECTION
            + "?{-join|&|count,page,sort,order}");
        actions.addLink(
            "pending",
            ccc.api.core.ResourceIdentifiers.Action.COLLECTION
            + "?{-join|&|count,page,sort,order}");
        actions.addLink(
            "completed",
            ccc.api.core.ResourceIdentifiers.Action.COLLECTION
            + ccc.api.core.ResourceIdentifiers.Action.COMPLETED
            + "?{-join|&|count,page,sort,order}");
        actions.addLink(
            "execute_all",
            ccc.api.core.ResourceIdentifiers.Action.COLLECTION
            + ccc.api.core.ResourceIdentifiers.Action.EXECUTE);
        actions.addLink(
            "scheduler",
            ccc.api.core.ResourceIdentifiers.Action.COLLECTION
            + ccc.api.core.ResourceIdentifiers.Scheduler.SCHEDULER);
    }
}
