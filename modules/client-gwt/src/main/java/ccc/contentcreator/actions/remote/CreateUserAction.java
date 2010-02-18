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

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.UserDto;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create a new user.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateUserAction
    extends
        RemotingAction {

    private final UserDto _userDelta;

    /**
     * Constructor.
     *
     * @param userDelta The user's details.
     */
    public CreateUserAction(final UserDto userDelta) {
        super(GLOBALS.uiConstants().createUser(), RequestBuilder.POST);
        _userDelta = userDelta;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _userDelta.toJson(json);
        return json.toString();
    }
}
