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
import ccc.api.UserDelta;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for user updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserAction_
    extends
        RemotingAction {

    private final ID _userId;
    private final UserDelta _userDetails;


    /**
     * Constructor.
     * @param userDetails The updated user details.
     * @param userId The user's id.
     */
    public UpdateUserAction_(final ID userId, final UserDelta userDetails) {
        super(UI_CONSTANTS.editUser(), RequestBuilder.POST);
        _userId = userId;
        _userDetails = userDetails;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users/"+_userId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _userDetails.toJson(json);
        return json.toString();
    }
}
