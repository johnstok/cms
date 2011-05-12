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

import ccc.api.types.Link;
import ccc.api.types.Username;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.S11nHelper;


/**
 * Determine whether a specified username exists.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UniqueUsernameAction
    extends
        RemotingAction<Boolean> {

    private final Username _username;

    /**
     * Constructor.
     * @param username The username to check.
     */
    public UniqueUsernameAction(final Username username) {
        super(USER_ACTIONS.checkUniqueUsername());
        _username = username;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + new Link(InternalServices.users.getLink("exists"))
                .build(
                    "uname",
                    _username.toString(),
                    InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Boolean> callback) {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new CallbackResponseHandler<Boolean>(
                    USER_ACTIONS.checkUniqueUsername(),
                    callback,
                    new Parser<Boolean>() {
                    @Override public Boolean parse(final Response response) {
                        return new S11nHelper().readBoolean(response);
                    }}));

    }

}
