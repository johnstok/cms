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
 * Remote action for user updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserAction
    extends
        RemotingAction {

    private final UUID _userId;
    private final UserDto _userDetails;


    /**
     * Constructor.
     * @param userDetails The updated user details.
     * @param userId The user's id.
     */
    public UpdateUserAction(final UUID userId, final UserDto userDetails) {
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
