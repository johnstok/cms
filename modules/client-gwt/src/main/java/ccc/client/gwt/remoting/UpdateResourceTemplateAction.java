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
package ccc.client.gwt.remoting;

import ccc.api.core.Resource;
import ccc.api.types.CommandType;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.Response;
import ccc.client.events.Event;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.TempSerializer;


/**
 * Remote action for resource's template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceTemplateAction
    extends
        RemotingAction {

    private final Resource _resource;


    /**
     * Constructor.
     *
     * @param resource The resource to update.
     */
    public UpdateResourceTemplateAction(final Resource resource) {
        super(UI_CONSTANTS.chooseTemplate(), HttpMethod.PUT);
        _resource = resource;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _resource.uriTemplate().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        new TempSerializer().write(json, _resource);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.RESOURCE_CHANGE_TEMPLATE);
        event.addProperty("resource", _resource.getId());
        event.addProperty("template", _resource.getTemplate());
        InternalServices.REMOTING_BUS.fireEvent(event);
    }
}
