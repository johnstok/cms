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

import ccc.api.ID;
import ccc.api.UserSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateUserPasswordAction
    extends
        RemotingAction {

    private final UserSummary _newPassword;
    private final ID _userId;


    /**
     * Constructor.
     * @param newPassword The user's new password.
     * @param userId The user's id.
     */
    public UpdateUserPasswordAction(final ID userId, final UserSummary newPassword) {
        super(GLOBALS.uiConstants().editUserPw(), RequestBuilder.POST);
        _userId = userId;
        _newPassword = newPassword;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/users/"+_userId+"/password";
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        _newPassword.toJson(json);
        return json.toString();
    }
}
