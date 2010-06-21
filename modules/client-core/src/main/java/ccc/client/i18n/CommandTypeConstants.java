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
package ccc.client.i18n;


/**
 * UI constants describing available commands.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandTypeConstants
    extends
        ConstantMap {

    // Resource
    /**
     * "Rename".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceRename();

    /**
     * "Move".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceMove();

    /**
     * "Delete".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceDelete();

    /**
     * "Publish".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourcePublish();

    /**
     * "Unpublish".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUnpublish();

    /**
     * "Lock".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceLock();

    /**
     * "Unlock".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUnlock();

    /**
     * "Change template".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceChangeTemplate();

    /**
     * "Update tags".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUpdateTags();

    /**
     * "Include in main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceIncludeInMm();

    /**
     * "Remove from main menu".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceRemoveFromMm();

    /**
     * "Update metadata".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUpdateMetadata();

    /**
     * "Change roles".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceChangeRoles();

    /**
     * "Update cache".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUpdateCache();

    /**
     * "Clear working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceClearWc();

    /**
     * "Update working copy".
     *
     * @return The constant, in the appropriate locale.
     */
    String resourceUpdateWc();


    // Folder
    /**
     * "Reorder".
     *
     * @return The constant, in the appropriate locale.
     */
    String folderReorder();

    /**
     * "Update sort order".
     *
     * @return The constant, in the appropriate locale.
     */
    String folderUpdateSortOrder();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String folderUpdate();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String folderCreate();

    // User
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String userCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String userUpdate();

    /**
     * "Change password".
     *
     * @return The constant, in the appropriate locale.
     */
    String userChangePassword();

    // Page
    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String pageUpdate();

    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String pageCreate();

    // Template
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String templateCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String templateUpdate();

    // File
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String fileCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String fileUpdate();

     // Search
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String searchCreate();

    // Alias
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String aliasCreate();

    /**
     * "Update".
     *
     * @return The constant, in the appropriate locale.
     */
    String aliasUpdate();

    // Action
    /**
     * "Create".
     *
     * @return The constant, in the appropriate locale.
     */
    String actionCreate();

    /**
     * "Cancel".
     *
     * @return The constant, in the appropriate locale.
     */
    String actionCancel();
}
