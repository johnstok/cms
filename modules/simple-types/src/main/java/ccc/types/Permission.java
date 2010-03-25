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
package ccc.types;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Group permissions.
 *
 * @author Civic Computing Ltd.
 */
public final class Permission {

    private Permission() { super(); }


    /** RESOURCE_READ : String. */
    public static final String RESOURCE_READ = "RESOURCE_READ";
    /** RESOURCE_UPDATE : String. */
    public static final String RESOURCE_UPDATE = "RESOURCE_UPDATE";
    /** RESOURCE_DELETE : String. */
    public static final String RESOURCE_DELETE = "RESOURCE_DELETE";
    /** RESOURCE_CACHE_UPDATE : String. */
    public static final String RESOURCE_CACHE_UPDATE = "RESOURCE_CACHE_UPDATE";
    /** RESOURCE_ACL_UPDATE : String. */
    public static final String RESOURCE_ACL_UPDATE = "RESOURCE_ACL_UPDATE";
    /** RESOURCE_MM : String. */
    public static final String RESOURCE_MM = "RESOURCE_MM";
    /** RESOURCE_PUBLISH : String. */
    public static final String RESOURCE_PUBLISH = "RESOURCE_PUBLISH";
    /** RESOURCE_UNPUBLISH : String. */
    public static final String RESOURCE_UNPUBLISH = "RESOURCE_UNPUBLISH";
    /** RESOURCE_LOCK : String. */
    public static final String RESOURCE_LOCK = "RESOURCE_LOCK";
    /** RESOURCE_UNLOCK : String. */
    public static final String RESOURCE_UNLOCK = "RESOURCE_UNLOCK";
    /** RESOURCE_RENAME : String. */
    public static final String RESOURCE_RENAME = "RESOURCE_RENAME";
    /** RESOURCE_MOVE : String. */
    public static final String RESOURCE_MOVE = "RESOURCE_MOVE";


    /** ACTION_EXECUTE : String. */
    public static final String ACTION_EXECUTE = "ACTION_EXECUTE";
    /** ACTION_LIST : String. */
    public static final String ACTION_LIST = "ACTION_LIST";
    /** ACTION_CANCEL : String. */
    public static final String ACTION_CANCEL = "ACTION_CANCEL";
    /** ACTION_CREATE : String. */
    public static final String ACTION_CREATE = "ACTION_CREATE";
    /** ACTION_SCHEDULE : String. */
    public static final String ACTION_SCHEDULE = "ACTION_SCHEDULE";


    /** SEARCH_REINDEX : String. */
    public static final String SEARCH_REINDEX = "SEARCH_REINDEX";
    /** SEARCH_SCHEDULE : String. */
    public static final String SEARCH_SCHEDULE = "SEARCH_SCHEDULE";
    /** SEARCH_CREATE : String. */
    public static final String SEARCH_CREATE = "SEARCH_CREATE";


    /** ALIAS_UPDATE : String. */
    public static final String ALIAS_UPDATE = "ALIAS_UPDATE";
    /** ALIAS_CREATE : String. */
    public static final String ALIAS_CREATE = "ALIAS_CREATE";
    /** ALIAS_READ : String. */
    public static final String ALIAS_READ = "ALIAS_READ";


    /** COMMENT_DELETE : String. */
    public static final String COMMENT_DELETE = "COMMENT_DELETE";
    /** COMMENT_CREATE : String. */
    public static final String COMMENT_CREATE = "COMMENT_CREATE";
    /** COMMENT_UPDATE : String. */
    public static final String COMMENT_UPDATE = "COMMENT_UPDATE";
    /** COMMENT_READ : String. */
    public static final String COMMENT_READ = "COMMENT_READ";


    /** FILE_CREATE : String. */
    public static final String FILE_CREATE = "FILE_CREATE";
    /** FILE_UPDATE : String. */
    public static final String FILE_UPDATE = "FILE_UPDATE";
    /** FILE_READ : String. */
    public static final String FILE_READ = "FILE_READ";


    /** FOLDER_READ : String. */
    public static final String FOLDER_READ = "FOLDER_READ";
    /** FOLDER_CREATE : String. */
    public static final String FOLDER_CREATE = "FOLDER_CREATE";
    /** FOLDER_UPDATE : String. */
    public static final String FOLDER_UPDATE = "FOLDER_UPDATE";
    /** ROOT_CREATE : String. */
    public static final String ROOT_CREATE = "ROOT_CREATE";


    /** GROUP_UPDATE : String. */
    public static final String GROUP_UPDATE = "GROUP_UPDATE";
    /** GROUP_READ : String. */
    public static final String GROUP_READ = "GROUP_READ";
    /** GROUP_CREATE : String. */
    public static final String GROUP_CREATE = "GROUP_CREATE";


    /** PAGE_CREATE : String. */
    public static final String PAGE_CREATE = "PAGE_CREATE";
    /** PAGE_UPDATE : String. */
    public static final String PAGE_UPDATE = "PAGE_UPDATE";


    /** TEMPLATE_READ : String. */
    public static final String TEMPLATE_READ = "TEMPLATE_READ";
    /** TEMPLATE_CREATE : String. */
    public static final String TEMPLATE_CREATE = "TEMPLATE_CREATE";
    /** TEMPLATE_UPDATE : String. */
    public static final String TEMPLATE_UPDATE = "TEMPLATE_UPDATE";


    /** USER_CREATE : String. */
    public static final String USER_CREATE = "USER_CREATE";
    /** USER_UPDATE : String. */
    public static final String USER_UPDATE = "USER_UPDATE";
    /** USER_READ : String. */
    public static final String USER_READ = "USER_READ";
    /** SELF_UPDATE : String. */
    public static final String SELF_UPDATE = "SELF_UPDATE";

    /** LOG_ENTRY_CREATE : String. */
    public static final String LOG_ENTRY_CREATE = "LOG_ENTRY_CREATE";


    /** MIGRATE : String. */
    public static final String MIGRATE = "MIGRATE";


    /** API_ACCESS : String. */
    public static final String API_ACCESS = "API_ACCESS";


    /** ALL : Set. */
    public static final Set<String> ALL;


    static {
        final SortedSet<String> allPerms = new TreeSet<String>();

        allPerms.add(RESOURCE_READ);
        allPerms.add(RESOURCE_UPDATE);
        allPerms.add(RESOURCE_DELETE);
        allPerms.add(RESOURCE_CACHE_UPDATE);
        allPerms.add(RESOURCE_ACL_UPDATE);
        allPerms.add(RESOURCE_MM);
        allPerms.add(RESOURCE_PUBLISH);
        allPerms.add(RESOURCE_UNPUBLISH);
        allPerms.add(RESOURCE_LOCK);
        allPerms.add(RESOURCE_UNLOCK);
        allPerms.add(RESOURCE_RENAME);
        allPerms.add(RESOURCE_MOVE);
        allPerms.add(ACTION_EXECUTE);
        allPerms.add(ACTION_LIST);
        allPerms.add(ACTION_CANCEL);
        allPerms.add(ACTION_CREATE);
        allPerms.add(ACTION_SCHEDULE);
        allPerms.add(SEARCH_REINDEX);
        allPerms.add(SEARCH_SCHEDULE);
        allPerms.add(SEARCH_CREATE);
        allPerms.add(ALIAS_UPDATE);
        allPerms.add(ALIAS_CREATE);
        allPerms.add(ALIAS_READ);
        allPerms.add(COMMENT_DELETE);
        allPerms.add(COMMENT_CREATE);
        allPerms.add(COMMENT_UPDATE);
        allPerms.add(COMMENT_READ);
        allPerms.add(FILE_CREATE);
        allPerms.add(FILE_UPDATE);
        allPerms.add(FILE_READ);
        allPerms.add(FOLDER_READ);
        allPerms.add(FOLDER_CREATE);
        allPerms.add(FOLDER_UPDATE);
        allPerms.add(ROOT_CREATE);
        allPerms.add(GROUP_UPDATE);
        allPerms.add(GROUP_READ);
        allPerms.add(GROUP_CREATE);
        allPerms.add(PAGE_CREATE);
        allPerms.add(PAGE_UPDATE);
        allPerms.add(TEMPLATE_READ);
        allPerms.add(TEMPLATE_CREATE);
        allPerms.add(TEMPLATE_UPDATE);
        allPerms.add(USER_CREATE);
        allPerms.add(USER_UPDATE);
        allPerms.add(USER_READ);
        allPerms.add(SELF_UPDATE);
        allPerms.add(LOG_ENTRY_CREATE);
        allPerms.add(API_ACCESS);

        ALL = Collections.unmodifiableSortedSet(allPerms);
    }
}
