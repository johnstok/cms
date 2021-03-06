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
package ccc.client.actions;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.SingleSelectionModel;
import ccc.client.events.Event;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteResourceAction
    extends
        RemotingAction<Void> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public DeleteResourceAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.delete(), HttpMethod.DELETE);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            new Link(
                _selectionModel
                    .tableSelection()
                    .getLink(Resource.Links.DELETE))
            .build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Void v) {
        final ResourceSummary item = _selectionModel.tableSelection();
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.RESOURCE_DELETE);
        event.addProperty("resource", item.getId());
        InternalServices.remotingBus.fireEvent(event);
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        return InternalServices.window.confirm(
            "Are sure you want to delete the selected resource?");
    }
}
