/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.Actions;
import ccc.api.core.Actions2;
import ccc.api.core.PagedCollection;
import ccc.api.core.Scheduler;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;


/**
 * Converts an {@link Actions2} object to the {@link Actions} API.
 *
 * @author Civic Computing Ltd.
 */
public class Actions2Impl
    implements
        Actions {

    private final Actions2 _actions;
    private final Scheduler _scheduler;


    /**
     * Constructor.
     *
     * @param actions The actions implementation to wrap.
     * @param scheduler The scheduler that will call the actions API.
     */
    public Actions2Impl(final Actions2 actions, final Scheduler scheduler) {
        _actions = DBC.require().notNull(actions);
        _scheduler = DBC.require().notNull(scheduler);
    }


    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        return _scheduler.isRunning();
    }


    /** {@inheritDoc} */
    @Override
    public void start() {
        _scheduler.start();
    }


    /** {@inheritDoc} */
    @Override
    public void stop() {
        _scheduler.stop();
    }


    /** {@inheritDoc} */
    @Override
    public void cancel(final UUID actionId) {
        _actions.cancel(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary create(final Action action) {
        return _actions.create(action);
    }


    /** {@inheritDoc} */
    @Override
    public void executeAll() {
        _actions.executeAll();
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> listCompletedActions(
                                                   final String sort,
                                                   final SortOrder sortOrder,
                                                   final int pageNo,
                                                   final int pageSize) {
        return _actions.listCompletedActions(sort, sortOrder, pageNo, pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> listPendingActions(
                                                   final String sort,
                                                   final SortOrder sortOrder,
                                                   final int pageNo,
                                                   final int pageSize) {
        return _actions.listPendingActions(sort, sortOrder, pageNo, pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public ActionSummary retrieve(final UUID actionId) {
        return _actions.retrieve(actionId);
    }
}
