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
package ccc.api.jaxrs;

import java.util.UUID;

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Actions;
import ccc.rest.Scheduler;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.DtoCollection;
import ccc.types.SortOrder;


/**
 * Implementation of the {@link Actions} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/actions")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class ActionsImpl
    extends
        JaxrsCollection
    implements
        Actions,
        Scheduler {


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ActionSummary> listCompletedActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        try {
            return getActions().listCompletedActions(sort,
                sortOrder,
                pageNo,
                pageSize);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ActionSummary> listPendingActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        try {
            return getActions().listPendingActions(sort,
                sortOrder,
                pageNo,
                pageSize);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary createAction(final ActionDto action) {
        try {
            return getActions().createAction(action);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId) {
        try {
            getActions().cancelAction(actionId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        try {
            getActions().executeAll();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary findAction(final UUID actionId) {
        try {
            return getActions().findAction(actionId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public boolean isRunning() {
        try {
            return lookupActionScheduler().isRunning();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void start() {
        try {
            lookupActionScheduler().start();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void stop() {
        try {
            lookupActionScheduler().stop();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }
}
