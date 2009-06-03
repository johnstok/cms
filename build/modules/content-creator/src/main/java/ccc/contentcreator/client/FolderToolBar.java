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

import ccc.api.UserSummary;
import ccc.contentcreator.actions.CreateFileAction;
import ccc.contentcreator.actions.CreateFolderAction;
import ccc.contentcreator.actions.CreatePageAction;
import ccc.contentcreator.actions.CreateTemplateAction;
import ccc.contentcreator.api.UIConstants;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     * @param user UserSummary currently logged in user.
     */
    FolderToolBar(final SingleSelectionModel ssm, final UserSummary user) {
        addSeparator();
        addButton(
            "uploadFile",
            _constants.uploadFile(),
            new CreateFileAction(ssm));
        addSeparator();
        addButton(
            "Create Folder",
            _constants.createFolder(),
            new CreateFolderAction(ssm));
        addSeparator();
        addButton(
            "Create Page",
            _constants.createPage(),
            new CreatePageAction(ssm));
        addSeparator();
        if (user.getRoles().contains(Globals.ADMINISTRATOR)
                || user.getRoles().contains(Globals.SITE_BUILDER)) {
            addButton(
                "Create Template",
                _constants.createTemplate(),
                new CreateTemplateAction(ssm));
            addSeparator();
        }
    }
}
