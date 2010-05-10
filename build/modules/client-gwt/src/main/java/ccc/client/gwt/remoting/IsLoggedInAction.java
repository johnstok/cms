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

import ccc.api.core.API;
import ccc.api.core.Security;
import ccc.api.core.UserCriteria;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.ActionCollection;
import ccc.client.gwt.binding.CommentCollection;
import ccc.client.gwt.binding.GroupCollection;
import ccc.client.gwt.binding.UserCollection;
import ccc.client.gwt.concurrent.SimpleLatch;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.views.gxt.LoginDialog;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class IsLoggedInAction
    extends
        RemotingAction {

    /**
     * Constructor.
     */
    public IsLoggedInAction() {
        super(USER_ACTIONS.internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return GlobalsImpl.getAPI().getLink(Security.CURRENT);
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        if (parseBoolean(response)) {
            GLOBALS.enableExitConfirmation();
            loadServices();
        } else {
            new LoginDialog().show();
        }
    }


    private void loadServices() {

        final API api = GlobalsImpl.getAPI();

        final SimpleLatch l = new SimpleLatch(4) {
            /** {@inheritDoc} */
            @Override protected void complete() {
                new GetCurrentUserAction().execute();
            }
        };


        new ListUsersAction(new UserCriteria(), 1, 1, "", "ASC") {
            /** {@inheritDoc} */
            @Override
            protected String getPath() {
                return Globals.API_URL + api.users();
            }

            @Override
            protected void execute(final UserCollection users) {
                GlobalsImpl.users(users);
                l.countDown();
            }
        }.execute();


        new ListPendingActionsAction(1, 1, "", SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() { return api.actions(); }

            /** {@inheritDoc} */
            @Override
            protected void execute(final ActionCollection actions) {
                GlobalsImpl.actions(actions);
                l.countDown();
            }
        }.execute();


        new ListComments(null, 1, 1, "", SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() { return api.comments(); }

            /** {@inheritDoc} */
            @Override
            protected void execute(final CommentCollection comments) {
                GlobalsImpl.comments(comments);
                l.countDown();
            }
        }.execute();


        new ListGroups(1, 1, "", "ASC") {
            /** {@inheritDoc} */
            @Override
            protected String getPath() {
                return Globals.API_URL + api.groups();
            }

            /** {@inheritDoc} */
            @Override
            protected void execute(final GroupCollection groups) {
                GlobalsImpl.groups(groups);
                l.countDown();
            }
        }.execute();
    }
}
