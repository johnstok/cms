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
package ccc.persistence;


/**
 * Constants representing CCC queries.
 *
 * @author Civic Computing Ltd.
 */
public final class QueryNames {

    private QueryNames() { super(); }

    /** PENDING : String. */
    public static final String PENDING = "pending";

    /** EXECUTED : String. */
    public static final String EXECUTED = "executed";

    /** LOG_ENTRY_BY_ID : String. */
    public static final String LOG_ENTRY_BY_ID = "logEntryById";

    /** ALL_IMAGES : String. */
    public static final String ALL_IMAGES = "allImages";

    /** ROOT_BY_NAME : String. */
    public static final String ROOT_BY_NAME = "rootByName";

    /** RESOURCE_BY_LEGACY_ID : String. */
    public static final String RESOURCE_BY_LEGACY_ID = "resourceByLegacyId";

    /** ALL_PAGES : String. */
    public static final String ALL_PAGES = "allPages";

    /** ALL_FILES : String. */
    public static final String ALL_FILES = "allFiles";

    /** LATEST_ACTION : String. */
    public static final String LATEST_ACTION = "latest_action";

    /** USERS : String. */
    public static final String USERS = "users";

    /** USERS_WITH_USERNAME : String. */
    public static final String USERS_WITH_USERNAME = "usersWithUsername";

    /** USERS_WITH_EMAIL : String. */
    public static final String USERS_WITH_EMAIL = "usersWithEmail";

    /** USERS_WITH_ROLE : String. */
    public static final String USERS_WITH_ROLE = "usersWithRole";

    /** PASSWORD_FOR_USER : String. */
    public static final String PASSWORD_FOR_USER = "passwordForUser";

    /** RESOURCES_LOCKED_BY_USER : String. */
    public static final String RESOURCES_LOCKED_BY_USER =
        "resourcesLockedByUser";

    /** LOCKED_RESOURCES : String. */
    public static final String LOCKED_RESOURCES = "lockedResources";

    /** RESOURCE_HISTORY : String. */
    public static final String RESOURCE_HISTORY = "resourceHistory";

    /** ALL_TEMPLATES : String. */
    public static final String ALL_TEMPLATES = "allTemplates";

    /** TEMPLATE_BY_NAME : String. */
    public static final String TEMPLATE_BY_NAME = "templateByName";

    /** ROOTS : String. */
    public static final String ROOTS = "roots";

    /** SETTING_WITH_NAME : String. */
    public static final String SETTINGS_WITH_NAME = "settings-with-name";
}
