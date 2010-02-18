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
package ccc.contentcreator.actions.remote;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.GroupDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Retrieve a list of groups from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListGroups
    extends
        RemotingAction {


    /**
     * Constructor.
     *
     */
    public ListGroups() {
        super(USER_ACTIONS.unknownAction());
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final String path = "/groups";
        return path;
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<GroupDto> groups = new ArrayList<GroupDto>();
        for (int i=0; i<result.size(); i++) {
            groups.add(new GroupDto(new GwtJson(result.get(i).isObject())));
        }

        execute(groups);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param groups The groups returned.
     */
    protected abstract void execute(Collection<GroupDto> groups);
}
