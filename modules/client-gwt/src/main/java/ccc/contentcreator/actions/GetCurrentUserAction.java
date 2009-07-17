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

import ccc.api.UserSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetCurrentUserAction
    extends RemotingAction {

    /**
     * Constructor.
     */
    public GetCurrentUserAction() {
        super(GLOBALS.userActions().internalAction());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final UserSummary user = new UserSummary(new GwtJson(result));
        execute(user);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users/me";
    }


    /**
     * Execute this action.
     *
     * @param user The user returned by the server.
     */
    protected abstract void execute(final UserSummary user);
}
