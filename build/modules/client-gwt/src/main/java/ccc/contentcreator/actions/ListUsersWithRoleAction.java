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

import ccc.api.UserSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListUsersWithRoleAction
    extends
        RemotingAction {

    private String _role;

    /**
     * Constructor.
     *
     * @param role The role to search on.
     */
    public ListUsersWithRoleAction(final String role) {
        super(USER_ACTIONS.viewUsers());
        _role = role;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/users/role/"+_role; // FIXME: Escape _role.
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<UserSummary> users = new ArrayList<UserSummary>();
        for (int i=0; i<result.size(); i++) {
            users.add(new UserSummary(new GwtJson(result.get(i).isObject())));
        }

        execute(users);
    }

    protected abstract void execute(final Collection<UserSummary> users);
}