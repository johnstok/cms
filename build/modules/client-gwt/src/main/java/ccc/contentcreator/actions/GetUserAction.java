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
package ccc.contentcreator.actions;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.UserDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Fetch a user.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetUserAction
    extends RemotingAction {

    private final UUID _id;

    /**
     * Constructor.
     *
     * @param id The user id.
     */
    public GetUserAction(final UUID id) {
        super(GLOBALS.userActions().internalAction());
        _id = id;
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final UserDto user = new UserDto(new GwtJson(result));
        execute(user);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param user The user returned.
     */
    protected abstract void execute(UserDto user);


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users/"+_id+"/delta";
    }
}
