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

import ccc.api.types.Link;
import ccc.api.types.Username;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.ResponseHandlerAdapter;

import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UniqueUsernameAction
    extends
        RemotingAction {

    private final Username _username;

    /**
     * Constructor.
     * @param username The username to check.
     */
    public UniqueUsernameAction(final Username username) {
        _username = username;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + new Link(GLOBALS.users().getLink("exists"))
                .build("uname", _username.toString(), new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(
                    USER_ACTIONS.checkUniqueUsername()) {

                    /** {@inheritDoc} */
                    @Override public void onOK(final Response response) {
                        final boolean exists =
                            JSONParser
                                .parse(response.getText())
                                .isBoolean()
                                .booleanValue();
                        execute(exists);
                    }
                });

    }

    /**
     * Handle a successful execution.
     *
     * @param usernameExists True if the username exists, false otherwise.
     */
    protected abstract void execute(boolean usernameExists);
}
