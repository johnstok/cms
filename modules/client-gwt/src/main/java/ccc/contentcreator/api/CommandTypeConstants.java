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
package ccc.contentcreator.api;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandTypeConstants extends ConstantsWithLookup {

    // Resource
    /**
     * "Rename".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Rename")
    String RESOURCE_RENAME();

    /**
     * "Move".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Move")
    String RESOURCE_MOVE();

    /**
     * "Publish".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Publish")
    String RESOURCE_PUBLISH();

    /**
     * "Unpublish".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Unpublish")
    String RESOURCE_UNPUBLISH();

    /**
     * "Lock".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Lock")
    String RESOURCE_LOCK();

    /**
     * "Unlock".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Unlock")
    String RESOURCE_UNLOCK();

    /**
     * "Change template".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change template")
    String RESOURCE_CHANGE_TEMPLATE();

    /**
     * "Update tags".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update tags")
    String RESOURCE_UPDATE_TAGS();

    /**
     * "Include in main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Include in main menu")
    String RESOURCE_INCLUDE_IN_MM();

    /**
     * "Remove from main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Remove from main menu")
    String RESOURCE_REMOVE_FROM_MM();

    /**
     * "Update metadata".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update metadata")
    String RESOURCE_UPDATE_METADATA();

    /**
     * "Change roles".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change roles")
    String RESOURCE_CHANGE_ROLES();

    /**
     * "Update cache".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update cache")
    String RESOURCE_UPDATE_CACHE();

    /**
     * "Clear working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Clear working copy")
    String RESOURCE_CLEAR_WC();

    /**
     * "Update working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update working copy")
    String RESOURCE_UPDATE_WC();


    // Folder
    /**
     * "Reorder".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Reorder")
    String FOLDER_REORDER();

    /**
     * "Update sort order".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update sort order")
    String FOLDER_UPDATE_SORT_ORDER();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String FOLDER_UPDATE();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String FOLDER_CREATE();

    // User
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String USER_CREATE();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String USER_UPDATE();

    /**
     * "Change password".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change password")
    String USER_CHANGE_PASSWORD();

    // Page
    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String PAGE_UPDATE();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String PAGE_CREATE();

    // Template
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String TEMPLATE_CREATE();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String TEMPLATE_UPDATE();

    // File
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String FILE_CREATE();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String FILE_UPDATE();

     // Search
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String SEARCH_CREATE();

    // Alias
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String ALIAS_CREATE();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String ALIAS_UPDATE();

    // Action
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String ACTION_CREATE();

    /**
     * "Cancel".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Cancel")
    String ACTION_CANCEL();
}
