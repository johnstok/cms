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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.types;


/** Valid actions for a log entry. */
public enum CommandType {

    // Resource
    /** RESOURCE_RENAME : CommandType. */
    RESOURCE_RENAME,
    /** RESOURCE_MOVE : CommandType. */
    RESOURCE_MOVE,
    /** RESOURCE_PUBLISH : CommandType. */
    RESOURCE_PUBLISH,
    /** RESOURCE_UNPUBLISH : CommandType. */
    RESOURCE_UNPUBLISH,
    /** RESOURCE_LOCK : CommandType. */
    RESOURCE_LOCK,
    /** RESOURCE_UNLOCK : CommandType. */
    RESOURCE_UNLOCK,
    /** RESOURCE_CHANGE_TEMPLATE : CommandType. */
    RESOURCE_CHANGE_TEMPLATE,
    /** RESOURCE_INCLUDE_IN_MM : CommandType. */
    RESOURCE_INCLUDE_IN_MM,
    /** RESOURCE_REMOVE_FROM_MM : CommandType. */
    RESOURCE_REMOVE_FROM_MM,
    /** RESOURCE_UPDATE_METADATA : CommandType. */
    RESOURCE_UPDATE_METADATA,
    /** RESOURCE_CHANGE_ROLES : CommandType. */
    RESOURCE_CHANGE_ROLES,
    /** RESOURCE_UPDATE_CACHE : CommandType. */
    RESOURCE_UPDATE_CACHE,
    /** RESOURCE_CLEAR_WC : CommandType. */
    RESOURCE_CLEAR_WC,
    /** RESOURCE_UPDATE_WC : CommandType. */
    RESOURCE_UPDATE_WC,
    /** RESOURCE_DELETE : CommandType. */
    RESOURCE_DELETE,
    /** RESOURCE_APPLY_WC : CommandType. */
    RESOURCE_APPLY_WC,

    // Folder
    /** FOLDER_UPDATE : CommandType. */
    FOLDER_UPDATE,
    /** FOLDER_CREATE : CommandType. */
    FOLDER_CREATE,

    // User
    /** USER_CREATE : CommandType. */
    USER_CREATE,
    /** USER_UPDATE : CommandType. */
    USER_UPDATE,
    /** USER_CHANGE_PASSWORD : CommandType. */
    USER_CHANGE_PASSWORD,

    // Page
    /** PAGE_UPDATE : CommandType. */
    PAGE_UPDATE,
    /** PAGE_CREATE : CommandType. */
    PAGE_CREATE,

    // Template
    /** TEMPLATE_CREATE : CommandType. */
    TEMPLATE_CREATE,
    /** TEMPLATE_UPDATE : CommandType. */
    TEMPLATE_UPDATE,

    // File
    /** FILE_CREATE : CommandType. */
    FILE_CREATE,
    /** FILE_UPDATE : CommandType. */
    FILE_UPDATE,

    // Search
    /** SEARCH_CREATE : CommandType. */
    SEARCH_CREATE,

    // Alias
    /** ALIAS_CREATE : CommandType. */
    ALIAS_CREATE,
    /** ALIAS_UPDATE : CommandType. */
    ALIAS_UPDATE,

    // Action
    /** ACTION_CANCEL : CommandType. */
    ACTION_CANCEL,
    /** ACTION_CREATE : CommandType. */
    ACTION_CREATE,

    /** GROUP_CREATE : CommandType. */
    GROUP_CREATE,
    /** GROUP_UPDATE : CommandType. */
    GROUP_UPDATE,

    /** COMMENT_CREATE : CommandType. */
    COMMENT_CREATE,
    /** COMMENT_UPDATE : CommandType. */
    COMMENT_UPDATE,
    /** COMMENT_DELETE : CommandType. */
    COMMENT_DELETE;
}
