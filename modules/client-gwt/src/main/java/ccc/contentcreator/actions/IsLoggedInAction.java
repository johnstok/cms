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
public class IsLoggedInAction
    extends
        RemotingAction {

    /**
     * Constructor.
     */
    public IsLoggedInAction() {
        super(USER_ACTIONS.internalAction(), RequestBuilder.GET, false);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/current";
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        if (parseBoolean(response)) {
            GLOBALS.enableExitConfirmation();
            new GetCurrentUserAction().execute();
        } else {
            new LoginDialog().show();
        }
    }
}
