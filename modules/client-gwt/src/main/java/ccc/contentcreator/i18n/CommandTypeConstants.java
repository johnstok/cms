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
package ccc.contentcreator.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * UI constants describing available commands.
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
    String resourceRename();

    /**
     * "Move".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Move")
    String resourceMove();

    /**
     * "Delete".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Delete")
    String resourceDelete();

    /**
     * "Publish".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Publish")
    String resourcePublish();

    /**
     * "Unpublish".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Unpublish")
    String resourceUnpublish();

    /**
     * "Lock".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Lock")
    String resourceLock();

    /**
     * "Unlock".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Unlock")
    String resourceUnlock();

    /**
     * "Change template".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change template")
    String resourceChangeTemplate();

    /**
     * "Update tags".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update tags")
    String resourceUpdateTags();

    /**
     * "Include in main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Include in main menu")
    String resourceIncludeInMm();

    /**
     * "Remove from main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Remove from main menu")
    String resourceRemoveFromMm();

    /**
     * "Update metadata".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update metadata")
    String resourceUpdateMetadata();

    /**
     * "Change roles".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change roles")
    String resourceChangeRoles();

    /**
     * "Update cache".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update cache")
    String resourceUpdateCache();

    /**
     * "Clear working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Clear working copy")
    String resourceClearWc();

    /**
     * "Update working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update working copy")
    String resourceUpdateWc();


    // Folder
    /**
     * "Reorder".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Reorder")
    String folderReorder();

    /**
     * "Update sort order".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update sort order")
    String folderUpdateSortOrder();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String folderUpdate();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String folderCreate();

    // User
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String userCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String userUpdate();

    /**
     * "Change password".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Change password")
    String userChangePassword();

    // Page
    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String pageUpdate();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String pageCreate();

    // Template
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String templateCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String templateUpdate();

    // File
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String fileCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String fileUpdate();

     // Search
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String searchCreate();

    // Alias
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String aliasCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Update")
    String aliasUpdate();

    // Action
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Create")
    String actionCreate();

    /**
     * "Cancel".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Cancel")
    String actionCancel();
}
