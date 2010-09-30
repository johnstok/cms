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
import ccc.api.types.Duration;
import ccc.client.core.InternalServices;
import ccc.client.core.LegacyView;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;


/**
 * Edit resource's cache setting.
 *
 * @author Civic Computing Ltd.
 */
public class OpenEditCacheAction
    extends
        RemotingAction<Duration> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenEditCacheAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.editCacheDuration());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Duration duration) {
        final LegacyView dialog =
            InternalServices.DIALOGS.editCaching(
                _selectionModel.tableSelection(), duration);
        dialog.show();
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummary delegate =
            _selectionModel.tableSelection();
        return delegate.duration().build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected Duration parse(final Response response) {
        return readDuration(response);
    }
}
