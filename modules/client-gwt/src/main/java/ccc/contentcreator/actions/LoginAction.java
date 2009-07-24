/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import ccc.contentcreator.dialogs.LoginDialog;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
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
        super(UI_CONSTANTS.login(), RequestBuilder.POST, false);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/sessions?u="
            +_dialog.getUsername()+"&p="
            +_dialog.getPassword(); // FIXME: Escaping!!
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
