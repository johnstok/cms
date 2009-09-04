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
package ccc.contentcreator.client;

import ccc.contentcreator.actions.OpenCreateFileAction;
import ccc.contentcreator.actions.OpenCreateFolderAction;
import ccc.contentcreator.actions.OpenCreatePageAction;
import ccc.contentcreator.actions.OpenCreateTemplateAction;
import ccc.contentcreator.api.UIConstants;
import ccc.rest.dto.UserDto;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = new IGlobalsImpl().uiConstants();

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     * @param user UserSummary currently logged in user.
     */
    FolderToolBar(final SingleSelectionModel ssm, final UserDto user) {
        addSeparator();
        addButton(
            "uploadFile",
            _constants.uploadFile(),
            new OpenCreateFileAction(ssm));
        addSeparator();
        addButton(
            "Create Folder",
            _constants.createFolder(),
            new OpenCreateFolderAction(ssm));
        addSeparator();
        addButton(
            "Create Page",
            _constants.createPage(),
            new OpenCreatePageAction(ssm));
        addSeparator();
        if (user.getRoles().contains(IGlobals.ADMINISTRATOR)
                || user.getRoles().contains(IGlobals.SITE_BUILDER)) {
            addButton(
                "Create Template",
                _constants.createTemplate(),
                new OpenCreateTemplateAction(ssm));
            addSeparator();
        }
    }
}
