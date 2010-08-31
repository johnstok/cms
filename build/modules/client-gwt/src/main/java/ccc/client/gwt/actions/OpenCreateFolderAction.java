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
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.CreateFolderDialog;
import ccc.client.presenters.CreateFolderPresenter;

/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateFolderAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateFolderAction(final SingleSelectionModel selectionModel) {
        super(I18n.UI_CONSTANTS.createFolder());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        final ResourceSummary item = _selectionModel.treeSelection();

        if (item == null) {
            InternalServices.WINDOW.alert(UI_CONSTANTS.noFolderSelected());
            return false;
        }

        return true;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            _selectionModel.treeSelection().delete().build(
                InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    public void onOK(final Response response) {
        final Resource item = readResource(response);
        new CreateFolderPresenter(new CreateFolderDialog(), item);
    }
}
