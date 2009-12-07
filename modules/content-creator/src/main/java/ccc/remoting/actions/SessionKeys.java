/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.remoting.actions;


/**
 * Constants for use with CCC Servlets.
 * We don't use an enum because the Servlet API requires string-based keys.
 *
 * @author Civic Computing Ltd.
 */
public final class SessionKeys {

    private SessionKeys() { super(); }

    /** EXCEPTION_KEY : String. */
    public static final String EXCEPTION_KEY = "ccc.exception";

    /** AUDIT_KEY : String. */
    public static final String AUDIT_KEY = "ccc.audit";

    /** USERS_KEY : String. */
    public static final String USERS_KEY = "ccc.users";

    /** FILES_KEY : String. */
    public static final String FILES_KEY = "ccc.files";

    /** PAGES_KEY : String. */
    public static final String PAGES_KEY = "ccc.pages";

    /** RESOURCES_KEY : String. */
    public static final String RESOURCES_KEY = "ccc.resources";

    /** FOLDERS_KEY : String. */
    public static final String FOLDERS_KEY = "ccc.folders";

    /** ACTIONS_KEY : String. */
    public static final String ACTIONS_KEY = "ccc.actions";

    /** TEMPLATES_KEY : String. */
    public static final String TEMPLATES_KEY = "ccc.templates";

    /** SEARCH_KEY : String. */
    public static final String SEARCH_KEY = "ccc.search";

    /** EXCEPTION_CODE : String. */
    public static final String EXCEPTION_CODE = "ccc.exception_code";
}
