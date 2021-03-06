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

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.synchronous.Actions;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.api.types.FailureCode;
import ccc.api.types.SortOrder;


/**
 * Implementation of the {@link Actions} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path(ccc.api.synchronous.ResourceIdentifiers.Action.COLLECTION)
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
    public PagedCollection<ActionSummary> listActions(
                                                final String username,
                                                final CommandType commandType,
                                                final FailureCode failureCode,
                                                final ActionStatus status,
                                                final Date executeAfter,
                                                final UUID subject,
                                                final String sort,
                                                final SortOrder sortOrder,
                                                final int pageNo,
                                                final int pageSize) {
        try {
            return _delegate.listActions(
                username,
                commandType,
                failureCode,
                status,
                executeAfter,
                subject,
                sort,
                sortOrder,
                pageNo,
                pageSize);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Action create(final Action action) {
        try {
            return _delegate.create(action);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void cancel(final UUID actionId) {
        try {
             _delegate.cancel(actionId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        try {
            _delegate.executeAll();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Action retrieve(final UUID actionId) {
        try {
            return _delegate.retrieve(actionId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public boolean isRunning() {
        try {
            return _delegate.isRunning();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void start() {
        try {
            _delegate.start();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Produces({"text/html", "application/json"})
    public void stop() {
        try {
            _delegate.stop();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }
}
