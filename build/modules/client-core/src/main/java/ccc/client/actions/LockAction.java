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
package ccc.client.actions;

import ccc.api.core.ResourceSummary;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;


/**
 * Lock a resource.
 *
 * @author Civic Computing Ltd.
 */
public class LockAction
    extends
        RemotingAction<Void> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public LockAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.lock(), HttpMethod.POST);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummary delegate =
            _selectionModel.tableSelection();
        return delegate.lock().build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Void v) {
        final ResourceSummary item = _selectionModel.tableSelection();
        item.setLockedBy(
            InternalServices.globals.currentUser().getUsername());
        _selectionModel.update(item);
    }


    /** {@inheritDoc} */
    @Override
    protected Void parse(final Response response) { return null; }
}
