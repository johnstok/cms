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
package ccc.client.gwt.widgets;

import ccc.api.types.Permission;
import ccc.client.actions.OpenCreateFileAction;
import ccc.client.actions.OpenCreateFolderAction;
import ccc.client.actions.OpenCreatePageAction;
import ccc.client.actions.OpenCreateTemplateAction;
import ccc.client.actions.OpenCreateTextFileAction;
import ccc.client.core.I18n;
import ccc.client.core.SingleSelectionModel;
import ccc.client.i18n.UIConstants;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = I18n.uiConstants;


    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    FolderToolBar(final SingleSelectionModel ssm) {

        addSeparator(null);
        addButton(Permission.FILE_CREATE,
            "uploadFile",
            _constants.uploadFile(),
            new OpenCreateFileAction(ssm));
        addSeparator(Permission.FILE_CREATE);

        addButton(Permission.FOLDER_CREATE,
            "Create folder",
            _constants.createFolder(),
            new OpenCreateFolderAction(ssm));
        addSeparator(Permission.FOLDER_CREATE);

        addButton(Permission.PAGE_CREATE,
            "Create page",
            _constants.createPage(),
            new OpenCreatePageAction(ssm));
        addSeparator(Permission.PAGE_CREATE);

        addButton(Permission.TEMPLATE_CREATE,
            "Create template",
            _constants.createTemplate(),
            new OpenCreateTemplateAction(ssm));
        addSeparator(Permission.TEMPLATE_CREATE);

        addButton(Permission.FILE_CREATE,
            "Create text file",
            _constants.createTextFile(),
            new OpenCreateTextFileAction(ssm));
        addSeparator(Permission.FILE_CREATE);
    }
}
