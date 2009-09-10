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

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.UserDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListUsersWithEmailAction
    extends
        RemotingAction {

    private final String _email;

    /**
     * Constructor.
     *
     * @param email The email to search on.
     */
    public ListUsersWithEmailAction(final String email) {
        super(USER_ACTIONS.viewUsers());
        _email = email;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/users/email/"
            + encode(_email);
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<UserDto> users = new ArrayList<UserDto>();
        for (int i=0; i<result.size(); i++) {
            users.add(new UserDto(new GwtJson(result.get(i).isObject())));
        }

        execute(users);
    }

    protected abstract void execute(Collection<UserDto> users);
}