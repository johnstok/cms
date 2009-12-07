/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.Actions;
import ccc.rest.RestException;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;


/**
 * Implementation of the {@link Actions} interface.
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
        Actions {


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
    public ActionSummary createAction(final ActionDto action)
    throws RestException {
        return getActions().createAction(action);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId)
    throws RestException {
        getActions().cancelAction(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        getActions().executeAll();
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary findAction(final UUID actionId) throws RestException {
        return getActions().findAction(actionId);
    }
}
