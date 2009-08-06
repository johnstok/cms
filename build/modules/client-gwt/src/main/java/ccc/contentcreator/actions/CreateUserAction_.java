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

import ccc.api.JsonKeys;
import ccc.api.UserDelta;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create a new user.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateUserAction_
    extends
        RemotingAction {

    private final UserDelta _userDelta;
    private final String _password;

    /**
     * Constructor.
     *
     * @param password The user's password.
     * @param userDelta The user's details.
     */
    public CreateUserAction_(final UserDelta userDelta, final String password) {
        super(GLOBALS.uiConstants().createUser(), RequestBuilder.POST);
        _userDelta = userDelta;
        _password = password;
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
        json.set(JsonKeys.DELTA, _userDelta);
        json.set(JsonKeys.PASSWORD, _password);
        return json.toString();
    }
}
