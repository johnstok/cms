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
import ccc.rest.UserSummary;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for user updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserAction
    extends
        RemotingAction {

    private final ID _userId;
    private final UserSummary _userDetails;


    /**
     * Constructor.
     * @param userDetails The updated user details.
     * @param userId The user's id.
     */
    public UpdateUserAction(final ID userId, final UserSummary userDetails) {
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
