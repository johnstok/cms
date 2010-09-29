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
package ccc.client.remoting;

import ccc.api.core.API;
import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.Security;
import ccc.api.core.User;
import ccc.api.core.UserCriteria;
import ccc.api.types.ActionStatus;
import ccc.api.types.SortOrder;
import ccc.client.concurrent.SimpleLatch;
import ccc.client.core.DefaultCallback;
import ccc.client.core.Globals;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Determine whether a user is logged in.
 *
 * @author Civic Computing Ltd.
 */
public class IsLoggedInAction
    extends
        RemotingAction<Boolean> {

    private static final int KEEP_ALIVE = 600000;


    /**
     * Constructor.
     */
    public IsLoggedInAction() {
        super(USER_ACTIONS.internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return InternalServices.API.getLink(Security.CURRENT);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Boolean loggedIn) {
        if (loggedIn.booleanValue()) {
            InternalServices.WINDOW.enableExitConfirmation();
            loadServices();
        } else {
            InternalServices.DIALOGS.login().show();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected Boolean parse(final Response response) {
        return readBoolean(response);
    }


    private void loadServices() {

        final API api = InternalServices.API;

        final SimpleLatch l = new SimpleLatch(4) {
            /** {@inheritDoc} */
            @Override protected void complete() {
                new GetCurrentUserAction().execute();
            }
        };


        new ListUsersAction(new UserCriteria(), 1, 1, "", SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() {
                return Globals.API_URL + api.users();
            }
        }.execute(
            new DefaultCallback<PagedCollection<User>>(
                                                USER_ACTIONS.internalAction()) {
                @Override
                public void onSuccess(final PagedCollection<User> users) {
                    InternalServices.USERS = users;
                    l.countDown();
                }});


        new ListActionsAction(ActionStatus.SCHEDULED.name(),
            1,
            1,
            "",
            SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() {
                return Globals.API_URL + api.actions();
            }
        }.execute(
            new DefaultCallback<PagedCollection<ActionSummary>>(
                                                USER_ACTIONS.internalAction()) {
                @Override
                public void onSuccess(
                              final PagedCollection<ActionSummary> actions) {
                    InternalServices.ACTIONS = actions;
                    l.countDown();
                }});


        new ListComments(null, 1, 1, "", SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() { return api.comments(); }

            /** {@inheritDoc} */
            @Override
            protected void execute(final PagedCollection<Comment> comments) {
                InternalServices.COMMENTS = comments;
                l.countDown();
            }
        }.execute();


        new ListGroups(1, 1, "", SortOrder.ASC) {
            /** {@inheritDoc} */
            @Override
            protected String getPath() {
                return Globals.API_URL + api.groups();
            }
        }.execute(
            new DefaultCallback<PagedCollection<Group>>(
                                                USER_ACTIONS.internalAction()) {
                @Override
                public void onSuccess(final PagedCollection<Group> groups) {
                    InternalServices.GROUPS = groups;
                    l.countDown();
                }});

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new GetCurrentUserAction() {
                    @Override
                    protected void onSuccess(final User user) {
                        //NO-OP
                    };
                }.execute();
            }
        };

        InternalServices.TIMERS.scheduleRepeating(runnable, KEEP_ALIVE);
    }
}
