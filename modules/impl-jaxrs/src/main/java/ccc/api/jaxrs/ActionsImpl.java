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

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.ActionScheduler;
import ccc.api.Actions;
import ccc.api.Scheduler;
import ccc.api.dto.ActionDto;
import ccc.api.dto.ActionSummary;
import ccc.api.dto.DtoCollection;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;


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

    private final Actions _delegate;
    private final ActionScheduler _schedulerDelegate;


    /**
     * Constructor.
     *
     * @param actions
     * @param actionScheduler
     */
    public ActionsImpl(final Actions actions,
                       final ActionScheduler actionScheduler) {
        _delegate = DBC.require().notNull(actions);
        _schedulerDelegate = DBC.require().notNull(actionScheduler);
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ActionSummary> listCompletedActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        return _delegate.listCompletedActions(sort,
            sortOrder,
            pageNo,
            pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ActionSummary> listPendingActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        return _delegate.listPendingActions(sort,
            sortOrder,
            pageNo,
            pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary createAction(final ActionDto action) {
        return _delegate.createAction(action);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId) {
        _delegate.cancelAction(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        _delegate.executeAll();
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary findAction(final UUID actionId) {
        return _delegate.findAction(actionId);
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public boolean isRunning() {
        return _schedulerDelegate.isRunning();
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void start() {
        _schedulerDelegate.start();
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void stop() {
        _schedulerDelegate.stop();
    }
}
