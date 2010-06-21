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
package ccc.client.gwt.core;

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.API;
import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.ActionStatusConstants;
import ccc.client.i18n.CommandTypeConstants;

import com.google.gwt.core.client.GWT;


/**
 * {@link Globals} implementation.
 *
 * @author Civic Computing Ltd.
 */
public class GlobalsImpl
    implements
        Globals {

    private static final Map<String, String> SETTINGS =
        new HashMap<String, String>();

    private static PagedCollection<User> USERS;
    private static PagedCollection<ActionSummary> ACTIONS;
    private static PagedCollection<Comment> COMMENTS;
    private static PagedCollection<Group> GROUPS;

    private static API API;


    /** {@inheritDoc} */
    @Override
    public String appURL() {
        return GWT.getHostPageBaseURL();
    }

    /** {@inheritDoc} */
    @Override
    public User currentUser() {
        return UserStore.currentUser();
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final User user) {
        UserStore.currentUser(user);
    }


    /** {@inheritDoc} */
    @Override
    public String hostURL() {
        return GWT.getHostPageBaseURL();
    }


    /**
     * Mutator.
     *
     * @param userActions The action names to set.
     */
    public static void setUserActions(final ActionNameConstants userActions) {
        I18n.USER_ACTIONS = userActions;
    }


    /**
     * User Summary shared by all instances of {@link GlobalsImpl}.
     *
     * @author Civic Computing Ltd.
     */
    private static final class UserStore {
        private static User usr;

        private UserStore() {
            // no-op
        }

        public static User currentUser() {
            return usr;
        }

        public static void currentUser(final User user) {
            usr = user;
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getSetting(final String settingName) {
        return SETTINGS.get(settingName);
    }


    /**
     * Provide additional settings for this global object.
     *
     * @param settings The settings to add.
     */
    public void setSettings(final Map<String, String> settings) {
        SETTINGS.putAll(settings);
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<User> users() { return USERS; }


    /**
     * Set the collection for managing users.
     *
     * @param users The user collection to access.
     */
    public static void users(final PagedCollection<User> users) {
        USERS = users;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> actions() { return ACTIONS; }


    /**
     * Set the collection for managing actions.
     *
     * @param actions The action collection to access.
     */
    public static void actions(final PagedCollection<ActionSummary> actions) {
        ACTIONS = actions;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Comment> comments() { return COMMENTS; }


    /**
     * Set the collection for managing comments.
     *
     * @param comments The comment collection to access.
     */
    public static void comments(final PagedCollection<Comment> comments) {
        COMMENTS = comments;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Group> groups() { return GROUPS; }


    /**
     * Set the collection for managing groups.
     *
     * @param groups The group collection to access.
     */
    public static void groups(final PagedCollection<Group> groups) {
        GROUPS = groups;
    }


    /**
     * Mutator.
     *
     * @param create The action statuses.
     */
    public static void setActionConstants(final ActionStatusConstants create) {
        I18n.ACTION_STATUSES = create;
    }


    /**
     * Mutator.
     *
     * @param create The command types.
     */
    public static void setCommandConstants(final CommandTypeConstants create) {
        I18n.COMMAND_TYPES = create;
    }


    /**
     * Mutator.
     *
     * @param api The remote API.
     */
    public static void setAPI(final API api) {
        API = api;
    }


    /**
     * Accessor.
     *
     * @return The remote API.
     */
    public static API getAPI() {
        return API;
    }
}
