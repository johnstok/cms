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

import ccc.api.core.Security;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.views.gxt.LoginDialog;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Remote action for user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginAction
    extends
        RemotingAction {

    private final LoginDialog _dialog;


    /**
     * Constructor.
     *
     * @param dialog The login dialog to act on.
     */
    public LoginAction(final LoginDialog dialog) {
        super(UI_CONSTANTS.login(), RequestBuilder.POST);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Security.COLLECTION
            +"?u=" + encode(_dialog.getUsername())
            +"&p=" + encode(_dialog.getPassword());
    }

    /** {@inheritDoc} */
    @Override protected void onOK(final Response response) {
        final boolean success = parseBoolean(response);
        if (success) {
            GLOBALS.refresh();
        } else {
            _dialog.loginFailed();
        }
    }
}
