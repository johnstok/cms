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

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.UserDto;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Dialog for updating current user's details.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCurrentUserAction
    extends
        RemotingAction {

    private final UserDto _userDetails;
    private final UUID _userId;


    /**
     * Constructor.
     * @param userId The user's id.
     * @param userDetails The updated user details.
     */
    public UpdateCurrentUserAction(final UUID userId,
                                   final UserDto userDetails) {
        super(GLOBALS.uiConstants().editUserPw(), RequestBuilder.POST);
        _userId = userId;
        _userDetails = userDetails;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/users/"+_userId+"/currentuser";
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        _userDetails.toJson(json);
        return json.toString();
    }
}
