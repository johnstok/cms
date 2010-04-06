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

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.commands.CancelActionCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.domain.Action;
import ccc.domain.Resource;
import ccc.persistence.ActionRepository;
import ccc.rest.Actions;
import ccc.rest.InternalError;
import ccc.rest.Resources;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.DtoCollection;
import ccc.rest.exceptions.RestException;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.SortOrder;


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

    private static final Logger LOG =
        Logger.getLogger(ActionsEJB.class.getName());

    @EJB(name=Resources.NAME) private ResourcesExt _resourcesExt;


    /** Constructor. */
    public ActionsEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_EXECUTE})
    public void executeAll() {
        LOG.debug("Executing scheduled actions.");

        final List<Action> actions =
            getRepoFactory().createActionRepository().latest(new Date());
        LOG.debug("Actions to execute: "+actions.size());

        for (final Action action : actions) {
            try {
                _resourcesExt
                    .executeAction(action.getId()); // Executes in nested txn.
                LOG.info("Completed action: "+action.getId());

            } catch (final RestException e) {
                fail(action, e);

            } catch (final RuntimeException e) {
                final Failure f = new Failure(FailureCode.UNEXPECTED);
                LOG.warn("Error executing action: "+f.getExceptionId(), e);
                fail(action, new InternalError(f));
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public DtoCollection<ActionSummary> listPendingActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        final ActionRepository actions =
            getRepoFactory().createActionRepository();
        final DtoCollection<ActionSummary> dc =
            new DtoCollection<ActionSummary>(
                actions.countPending(),
                Action.mapActions(
                    actions.pending(sort, sortOrder, pageNo, pageSize)));
        return dc;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public DtoCollection<ActionSummary> listCompletedActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        final ActionRepository actions =
            getRepoFactory().createActionRepository();
        final DtoCollection<ActionSummary> dc =
            new DtoCollection<ActionSummary>(
                actions.countCompleted(),
                Action.mapActions(actions.completed(
                    sort, sortOrder, pageNo, pageSize)));
        return dc;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_CANCEL})
    public void cancelAction(final UUID actionId) {
        new CancelActionCommand(getRepoFactory())
            .execute(currentUser(), new Date(), actionId);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_CREATE})
    public ActionSummary createAction(final ActionDto action) {
        final Action a =
            new Action(
                action.getCommand(),
                action.getExecuteAfter(),
                currentUser(),
                getRepoFactory()
                    .createResourceRepository()
                    .find(Resource.class, action.getResourceId()),
                action.getParameters());

        new ScheduleActionCommand(getRepoFactory())
            .execute(currentUser(), new Date(), a);

        return a.mapAction();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ACTION_LIST})
    public ActionSummary findAction(final UUID actionId) {
        return
            getRepoFactory()
                .createActionRepository()
                .find(actionId).mapAction();
    }


    private void fail(final Action action, final RestException e) {
        action.fail(e.getFailure());
        LOG.info(
            "Failed action: "+action.getId()
            +" [CommandFailedException was "
            +e.getFailure().getExceptionId()+"]");
    }
}
