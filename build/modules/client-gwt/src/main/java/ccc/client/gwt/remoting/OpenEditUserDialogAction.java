/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.remoting;

import java.util.Collection;

import ccc.api.core.Group;
import ccc.api.core.User;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.views.gxt.EditUserDialog;
import ccc.client.gwt.widgets.UserTable;


/**
 * Action to open the user editing dialog.
 *
 * @author Civic Computing Ltd.
 */
public class OpenEditUserDialogAction
    extends
        RemotingAction<User> {

    private final User _user;
    private final UserTable _userTable;
    private Collection<Group> _groups;


    /**
     * Constructor.
     *
     * @param user The selected user.
     * @param userTable he table displaying the users.
     * @param groups Collection of groups.
     */
    public OpenEditUserDialogAction(final User user,
                                    final UserTable userTable,
                                    final Collection<Group> groups) {
        super(UI_CONSTANTS.editUser());
        _user = user;
        _userTable = userTable;
        _groups = groups;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _user.self();
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final User delta) {
        new EditUserDialog(delta, _userTable, _groups).show();
    }


    /** {@inheritDoc} */
    @Override
    protected User parse(final Response response) {
        return readUser(response);
    }
}
