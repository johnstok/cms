/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.ActionDao;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.ActionNew;
import ccc.rest.dto.ActionSummary;


/**
 * Implementation of the {@link ActionDao} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class ActionsImpl
    extends
        JaxrsCollection
    implements
        ActionDao {


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return getActions().listCompletedActions();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return getActions().listPendingActions();
    }


    /** {@inheritDoc} */
    @Override
    public void createAction(final ActionNew action)
    throws CommandFailedException {
        getActions().createAction(action);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId)
    throws CommandFailedException {
        getActions().cancelAction(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void executeAction() {
        getActions().executeAction();
    }
}
