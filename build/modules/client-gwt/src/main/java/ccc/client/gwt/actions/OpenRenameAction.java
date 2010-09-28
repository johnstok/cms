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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.actions;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;
import ccc.client.presenters.RenameResourcePresenter;

/**
 * Rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenRenameAction
    extends
        RemotingAction<Resource> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenRenameAction(final SingleSelectionModel selectionModel) {
        super(I18n.UI_CONSTANTS.rename());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        final ResourceSummary item = _selectionModel.tableSelection();

        if (item == null) {
            InternalServices.WINDOW.alert(UI_CONSTANTS.noResourceSelected());
            return false;
        }

        return true;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + _selectionModel.tableSelection().self().build(
                InternalServices.ENCODER);
    }



    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Resource r) {
        new RenameResourcePresenter(
            InternalServices.DIALOGS.renameResource(), r);
    }


    /** {@inheritDoc} */
    @Override
    protected Resource parse(final Response response) {
        return readResource(response);
    }
}
