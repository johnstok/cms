/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import ccc.api.types.Res;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class API
    extends
        Res {

    /** RESOURCES : String. */
    public static final String RESOURCES = "resources";
    /** SEARCH : String. */
    public static final String SEARCH = "search";
    /** SECURITY : String. */
    public static final String SECURITY = "security";
    /** TEMPLATES : String. */
    public static final String TEMPLATES = "templates";
    /** USERS : String. */
    public static final String USERS = "users";
    /** PAGES : String. */
    public static final String PAGES = "pages";
    /** GROUPS : String. */
    public static final String GROUPS = "groups";
    /** FOLDERS : String. */
    public static final String FOLDERS = "folders";
    /** FILES : String. */
    public static final String FILES = "files";
    /** COMMENTS : String. */
    public static final String COMMENTS = "comments";
    /** ALIASES : String. */
    public static final String ALIASES = "aliases";
    /** ACTIONS : String. */
    public static final String ACTIONS = "actions";

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String actions() { return getLink(ACTIONS); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String aliases() { return getLink(ALIASES); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String comments() { return getLink(COMMENTS); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String files() { return getLink(FILES); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String folders() { return getLink(FOLDERS); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String groups() { return getLink(GROUPS); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String pages() { return getLink(PAGES); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String resources() { return getLink(RESOURCES); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String search() { return getLink(SEARCH); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String security() { return getLink(SECURITY); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String templates() { return getLink(TEMPLATES); }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String users() { return getLink(USERS); }
}
