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
package ccc.client.gwt.remoting;

import ccc.api.core.Resource;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.events.ResourceDeleted;
import ccc.client.gwt.widgets.ContentCreator;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteResourceAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public DeleteResourceAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.delete(), HttpMethod.POST);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            _selectionModel.tableSelection().getDelegate().uriDelete().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        ContentCreator.EVENT_BUS.fireEvent(new ResourceDeleted(item.getId()));
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        return GLOBALS.confirm(
            "Are sure you want to delete the selected resource?");
    }
}
