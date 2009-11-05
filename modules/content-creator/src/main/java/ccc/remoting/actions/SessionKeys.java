/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
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

    /** CURRENT_USER : String. */
    public static final String CURRENT_USER = "ccc.current_user";

    /** AUDIT_KEY : String. */
    public static final String AUDIT_KEY = "ccc.audit";

    /** DATA_KEY : String. */
    public static final String DATA_KEY = "ccc.data";

    /** EM_KEY : String. */
    public static final String EM_KEY = "ccc.em";

    /** RESOURCE_KEY : String. */
    public static final String RESOURCE_KEY = "ccc.resource";

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
