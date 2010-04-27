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

import ccc.api.core.User;
import ccc.api.types.Permission;
import ccc.client.gwt.actions.OpenCreateFileAction;
import ccc.client.gwt.actions.OpenCreateFolderAction;
import ccc.client.gwt.actions.OpenCreatePageAction;
import ccc.client.gwt.actions.OpenCreateTemplateAction;
import ccc.client.gwt.actions.OpenCreateTextFileAction;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.i18n.UIConstants;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = new GlobalsImpl().uiConstants();

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     * @param user UserSummary currently logged in user.
     */
    FolderToolBar(final SingleSelectionModel ssm, final User user) {
        addSeparator();
        addButton(
            "uploadFile",
            _constants.uploadFile(),
            new OpenCreateFileAction(ssm));
        addSeparator();
        addButton(
            "Create folder",
            _constants.createFolder(),
            new OpenCreateFolderAction(ssm));
        addSeparator();
        addButton(
            "Create page",
            _constants.createPage(),
            new OpenCreatePageAction(ssm));
        addSeparator();
        if (user.hasPermission(Permission.TEMPLATE_CREATE)) {
            addButton(
                "Create template",
                _constants.createTemplate(),
                new OpenCreateTemplateAction(ssm));
            addSeparator();
            addButton(
                "Create text file",
                _constants.createTextFile(),
                new OpenCreateTextFileAction(ssm));
            addSeparator();
        }
    }
}
