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
class GWTGlobals
    implements
        Globals {

    private static final Map<String, String> SETTINGS =
        new HashMap<String, String>();


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
     * User Summary shared by all instances of {@link GWTGlobals}.
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
}
