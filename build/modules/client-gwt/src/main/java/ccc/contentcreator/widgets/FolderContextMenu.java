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
package ccc.contentcreator.widgets;

import ccc.contentcreator.actions.OpenCreateFileAction;
import ccc.contentcreator.actions.OpenCreateFolderAction;
import ccc.contentcreator.actions.OpenCreatePageAction;
import ccc.contentcreator.actions.OpenCreateTemplateAction;
import ccc.contentcreator.core.SingleSelectionModel;



/**
 * Context menu for folder actions.
 *
 * @author Civic Computing Ltd.
 */
public class FolderContextMenu
    extends
        AbstractContextMenu {

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    public FolderContextMenu(final SingleSelectionModel ssm) {
        addMenuItem(
            "upload-file",
            getConstants().uploadFile(),
            new OpenCreateFileAction(ssm));
        addMenuItem(
            "create-folder",
            getConstants().createFolder(),
            new OpenCreateFolderAction(ssm));
        addMenuItem(
            "create-page",
            getConstants().createPage(),
            new OpenCreatePageAction(ssm));
        addMenuItem(
            "create-template",
            getConstants().createTemplate(),
            new OpenCreateTemplateAction(ssm));
    }
}
