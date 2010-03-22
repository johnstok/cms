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
package ccc.persistence;


/**
 * Constants representing CCC queries.
 *
 * @author Civic Computing Ltd.
 */
final class QueryNames {

    private QueryNames() { super(); }

    /** PENDING : String. */
    public static final String PENDING = "pending";

    /** EXECUTED : String. */
    public static final String EXECUTED = "executed";

    /** LOG_ENTRY_BY_ID : String. */
    public static final String LOG_ENTRY_BY_ID = "logEntryById";

    /** IMAGES_FROM_FOLDER : String. */
    public static final String IMAGES_FROM_FOLDER = "imagesFromFolder";

    /** ROOT_BY_NAME : String. */
    public static final String ROOT_BY_NAME = "rootByName";

    /** RESOURCE_BY_LEGACY_ID : String. */
    public static final String RESOURCE_BY_LEGACY_ID = "resourceByLegacyId";

    /** RESOURCE_BY_METADATA_KEY : String. */
    public static final String RESOURCE_BY_METADATA_KEY =
        "resourceByMetadataKey";

    /** ALL_PAGES : String. */
    public static final String ALL_PAGES = "allPages";

    /** ALL_FILES : String. */
    public static final String ALL_FILES = "allFiles";

    /** LATEST_ACTION : String. */
    public static final String LATEST_ACTION = "latest_action";

    /** USER_WITH_MATCHING_USERNAME : String. Case sensitive! */
    public static final String USER_WITH_MATCHING_USERNAME =
        "userWithMatchingUsername";

    /** USERS_WITH_LEGACY_ID : String. */
    public static final String USERS_WITH_LEGACY_ID = "usersWithLegacyId";

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
