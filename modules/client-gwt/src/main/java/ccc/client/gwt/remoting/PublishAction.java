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

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.events.Event;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.ResourceSummarySerializer;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class PublishAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public PublishAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.publish(), HttpMethod.POST);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            _selectionModel
                .tableSelection()
                .uriPublish()
                .build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final Json json = InternalServices.PARSER.parseJson(response.getText());
        final ResourceSummary rs = new ResourceSummarySerializer().read(json);
        InternalServices.REMOTING_BUS.fireEvent(
            new Event<CommandType>(CommandType.RESOURCE_PUBLISH)
                .addProperty("resource", rs));
    }
}
