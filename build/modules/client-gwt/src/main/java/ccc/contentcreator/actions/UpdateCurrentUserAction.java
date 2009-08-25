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
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Dialog for updating current user's details.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCurrentUserAction
    extends
        RemotingAction {

    private final String _newEmail;
    private final String _newPassword;
    private final ID _userId;


    /**
     * Constructor.
     * @param userId The user's id.
     * @param newEmail The new email.
     * @param newPassword The user's new password.
     */
    public UpdateCurrentUserAction(final ID userId,
                                final String newEmail,
                                final String newPassword) {
        super(GLOBALS.uiConstants().editUserPw(), RequestBuilder.POST);
        _userId = userId;
        _newEmail = newEmail;
        _newPassword = newPassword;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/users/"+_userId+"/currentuser";
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.EMAIL, _newEmail);
        json.set(JsonKeys.PASSWORD, _newPassword);
        return json.toString();
    }
}
