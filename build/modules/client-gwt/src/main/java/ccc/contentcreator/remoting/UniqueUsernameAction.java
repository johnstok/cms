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
package ccc.contentcreator.remoting;

import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.core.Request;
import ccc.contentcreator.core.ResponseHandlerAdapter;
import ccc.types.Username;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
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

    @Override
    protected String getPath() {
        return "api/secure/users/" + encode(_username.toString()) + "/exists";
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                RequestBuilder.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(
                    GLOBALS.userActions().checkUniqueUsername()) {

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
