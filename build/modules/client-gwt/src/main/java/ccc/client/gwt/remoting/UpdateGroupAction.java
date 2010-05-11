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

import ccc.api.core.Group;
import ccc.api.types.DBC;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.events.GroupUpdated;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONParser;


/**
 * Remote action for creating a group.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateGroupAction
    extends
        RemotingAction {

    private final Group _group;


    /**
     * Constructor.
     *
     * @param group The updated group.
     */
    public UpdateGroupAction(final Group group) {
        super(UI_CONSTANTS.createGroup(), HttpMethod.PUT);
        DBC.require().notNull(group);
        DBC.require().notNull(group.getId());
        _group = group;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _group.self();
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _group.toJson(json);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final Group newGroup =
            new Group(
                new GwtJson(JSONParser.parse(response.getText()).isObject()));
        final GwtEvent<?> event = new GroupUpdated(newGroup);
        fireEvent(event);
    }
}
