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
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.json.UserSerializer;


/**
 * Updates a user's password..
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateUserPasswordAction
    extends
        RemotingAction {

    private final User _user;


    /**
     * Constructor.
     *
     * @param user The user with an updated password.
     */
    public UpdateUserPasswordAction(final User user) {
        super(UI_CONSTANTS.editUserPw(), HttpMethod.PUT);
        _user = user;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return _user.uriPassword();
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        new UserSerializer().write(json, _user);
        return json.toString();
    }
}
