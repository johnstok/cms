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

import ccc.api.core.User;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GetCurrentUserAction
    extends RemotingAction {

    /**
     * Constructor.
     */
    public GetCurrentUserAction() {
        super(GLOBALS.userActions().internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final User user = new User(new GwtJson(result));
        GLOBALS.currentUser(user);
        new DrawMainWindowAction(user).execute();
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return User.ME;
    }
}
