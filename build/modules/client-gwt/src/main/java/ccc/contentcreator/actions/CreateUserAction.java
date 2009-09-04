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

import ccc.contentcreator.client.GwtJson;
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
