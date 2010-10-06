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

import java.util.HashMap;
import java.util.Map;

import ccc.api.synchronous.Security;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.views.LoginView;


/**
 * Remote action for user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginAction
    extends
        RemotingAction<Boolean> {

    private final LoginView _dialog;


    /**
     * Constructor.
     *
     * @param dialog The login dialog to act on.
     */
    public LoginAction(final LoginView dialog) {
        super(UI_CONSTANTS.login(), HttpMethod.POST);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("u", new String[] {_dialog.getUsername()});
        params.put("p", new String[] {_dialog.getPassword()});
        return
            new Link(InternalServices.API.getLink(Security.COLLECTION))
            .build(params, InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override protected void onSuccess(final Boolean success) {
        if (success.booleanValue()) {
            InternalServices.WINDOW.refresh();
        } else {
            _dialog.loginFailed();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected Boolean parse(final Response response) {
        return readBoolean(response);
    }
}
