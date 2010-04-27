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

import java.util.UUID;

import ccc.api.core.UserDto;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Dialog for updating current user's details.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCurrentUserAction
    extends
        RemotingAction {

    private final UserDto _userDetails;
    private final UUID _userId;


    /**
     * Constructor.
     * @param userId The user's id.
     * @param userDetails The updated user details.
     */
    public UpdateCurrentUserAction(final UUID userId,
                                   final UserDto userDetails) {
        super(GLOBALS.uiConstants().editUserPw(), RequestBuilder.POST);
        _userId = userId;
        _userDetails = userDetails;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/users/"+_userId+"/currentuser";
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        _userDetails.toJson(json);
        return json.toString();
    }
}
