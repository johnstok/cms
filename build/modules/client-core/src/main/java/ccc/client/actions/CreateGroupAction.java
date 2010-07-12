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

import ccc.api.core.Group;
import ccc.api.types.CommandType;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.events.Event;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.GroupSerializer;


/**
 * Remote action for creating a group.
 *
 * @author Civic Computing Ltd.
 */
public class CreateGroupAction
    extends
        RemotingAction {

    private final Group _group;


    /**
     * Constructor.
     *
     * @param group The updated group.
     */
    public CreateGroupAction(final Group group) {
        super(UI_CONSTANTS.createGroup(), HttpMethod.POST);
        _group = group;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            new Link(InternalServices.GROUPS.getLink("self"))
            .build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = InternalServices.PARSER.newJson();
        new GroupSerializer().write(json, _group);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final Group newGroup =
            new GroupSerializer().read(
                InternalServices.PARSER.parseJson(response.getText()));

        final Event<CommandType> event =
            new Event<CommandType>(CommandType.GROUP_CREATE);
        event.addProperty("group", newGroup);

        fireEvent(event);
    }
}
