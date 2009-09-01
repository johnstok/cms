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
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.dialogs.EditUserDialog;
import ccc.rest.dto.UserSummary;
import ccc.types.ID;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OpenEditUserDialogAction
    extends
        RemotingAction {

    private final ID _userId;
    private final UserTable _userTable;

    /**
     * Constructor.
     * @param userTable The table displaying the users.
     * @param userId The id of the user to be edited.
     */
    public OpenEditUserDialogAction(final ID userId,
                                    final UserTable userTable) {
        super(UI_CONSTANTS.editUser());
        _userId = userId;
        _userTable = userTable;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users/"+_userId+"/delta";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final UserSummary delta = new UserSummary(new GwtJson(result));
        new EditUserDialog(_userId, delta, _userTable).show();
    }
}
