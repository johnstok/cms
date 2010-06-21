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
package ccc.client.core;

import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.client.i18n.ActionStatusConstants;
import ccc.client.i18n.CommandTypeConstants;


/**
 * API for creating application scope objects.
 *
 * @author Civic Computing Ltd.
 */
public interface Globals {

    /**
     * Determine the URL for the application's host.
     *
     * @return The host url as a string.
     */
    String hostURL();

    /**
     * Determine the URL for the application.
     *
     * @return The host url as a string.
     */
    String appURL();


    /**
     * Accessor.
     *
     * @return The current logged in user.
     */
    User currentUser();

    /**
     * Mutator.
     *
     * @param user The current logged in user.
     */
    void currentUser(final User user);


    /** DEFAULT_WIDTH : int. */
    int DEFAULT_WIDTH = 640;
    /** MIN_WIDTH : int. */
    int MIN_WIDTH = 375;
    /** DEFAULT_HEIGHT : int. */
    int DEFAULT_HEIGHT = 480;
    /** DEFAULT_UPLOAD_HEIGHT : int. */
    int DEFAULT_UPLOAD_HEIGHT = 250;
    /** DEFAULT_MIN_HEIGHT : int. */
    int DEFAULT_MIN_HEIGHT = 150;
    /** APP_URL : String. */
    String APP_URL = "client";
    /** API_URL : String. */
    String API_URL = "api";
    /** MIN_USER_NAME_LENGTH : int. */
    int MIN_USER_NAME_LENGTH = 4;
    /** MAX_FETCH : int. */
    int MAX_FETCH = 999;

    /**
     * Retrieve an instance of the action status constants.
     *
     * @return An ActionStatusConstants object.
     */
    ActionStatusConstants actionStatusConstants();

    /**
     * Retrieve an instance of the command type constants.
     *
     * @return An CommandTypeConstants object.
     */
    CommandTypeConstants commandTypeConstants();

    /**
     * Retrieve a setting value.
     *
     * @param settingName The name of the setting to retrieve.
     *
     * @return The value of the setting as a string or NULL, if the setting
     *  doesn't exist.
     */
    String getSetting(String settingName);

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    PagedCollection<User> users();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    PagedCollection<ActionSummary> actions();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    PagedCollection<Comment> comments();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    PagedCollection<Group> groups();
}
