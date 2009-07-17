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

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LoginAction
    extends
        RemotingAction {

    private final String _password;
    private final String _username;


    /**
     * Constructor.
     *
     * @param password The user's password.
     * @param username The user's username.
     */
    public LoginAction(final String username, final String password) {
        super(UI_CONSTANTS.login(), RequestBuilder.POST);
        _username = username;
        _password = password;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions?u="+_username+"&p="+_password; // FIXME: Escaping!!
    }
}
