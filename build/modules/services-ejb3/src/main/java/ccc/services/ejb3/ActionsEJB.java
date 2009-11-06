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

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;

import ccc.commands.CancelActionCommand;
import ccc.commands.ScheduleActionCommand;
import ccc.domain.Action;
import ccc.domain.CccCheckedException;
import ccc.rest.Actions;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.extensions.ResourcesExt;


/**
 * EJB implementation of the {@link Actions}.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Actions.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Actions.class)
@RolesAllowed({ADMINISTRATOR})
@RunAs(CONTENT_CREATOR)
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
    public void executeAll() {
        LOG.debug("Executing scheduled actions.");

        final List<Action> actions = getActions().latest(new Date());
        LOG.debug("Actions to execute: "+actions.size());

        for (final Action action : actions) {
            try {
                _resourcesExt
                    .executeAction(action.id()); // Executes in nested txn.
                LOG.info("Completed action: "+action.id());

            } catch (final RestException e) {
                fail(action, e);

            } catch (final RuntimeException e) {
                LOG.warn("Error executing action.", e);
                throw e;

            }
        }
    }


    private void fail(final Action action, final RestException e) {
        action.fail(e.getFailure());
        LOG.info(
            "Failed action: "+action.id()
            +" [CommandFailedException was "
            +e.getFailure().getExceptionId()+"]");
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
    public ActionSummary createAction(final ActionDto action)
    throws RestException {
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

            return mapAction(a);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionSummary findAction(final UUID actionId) throws RestException {
        try {
            return mapAction(getActions().find(actionId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
