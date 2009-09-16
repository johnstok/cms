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

import ccc.types.Username;

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
        super(GLOBALS.userActions().checkUniqueUsername());
        _username = username;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/users/"
            + encode(_username.toString())
            + "/exists";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final boolean exists =
            JSONParser.parse(response.getText()).isBoolean().booleanValue();
        execute(exists);
    }

    /**
     * Handle a successful execution.
     *
     * @param usernameExists True if the username exists, false otherwise.
     */
    protected abstract void execute(boolean usernameExists);
}
