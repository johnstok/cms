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
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.Actions;
import ccc.api.types.DBC;
import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;


/**
 * Implementation of the {@link Actions} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class ActionsImpl
    extends
        JaxrsCollection
    implements
        Actions {

    private final Actions _delegate;


    /**
     * Constructor.
     *
     * @param actions The actions delegate.
     */
    public ActionsImpl(final Actions actions) {
        _delegate = DBC.require().notNull(actions);
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> listCompletedActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        try {
            return _delegate.listCompletedActions(
                sort,
                sortOrder,
                pageNo,
                pageSize);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> listPendingActions(
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int pageNo,
                                                    final int pageSize) {
        try {
            return _delegate.listPendingActions(
                sort,
                sortOrder,
                pageNo,
                pageSize);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary createAction(final Action action) {
        try {
            return _delegate.createAction(action);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId) {
        try {
             _delegate.cancelAction(actionId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        try {
            _delegate.executeAll();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary findAction(final UUID actionId) {
        try {
            return _delegate.findAction(actionId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public boolean isRunning() {
        try {
            return _delegate.isRunning();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void start() {
        try {
            _delegate.start();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void stop() {
        try {
            _delegate.stop();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
