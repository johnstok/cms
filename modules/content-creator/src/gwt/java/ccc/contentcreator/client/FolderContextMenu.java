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

import ccc.contentcreator.actions.ChooseTemplateAction;
import ccc.contentcreator.actions.CreateFileAction;
import ccc.contentcreator.actions.CreateFolderAction;
import ccc.contentcreator.actions.CreatePageAction;
import ccc.contentcreator.actions.CreateTemplateAction;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderContextMenu
    extends
        AbstractContextMenu {

    /**
     * Constructor.
     *
     * @param ssm
     */
    public FolderContextMenu(final SingleSelectionModel ssm) {
        super(ssm);

        addMenuItem(
            "upload-file",
            Globals.uiConstants().uploadFile(),
            new CreateFileAction(ssm));
        addMenuItem(
            "create-folder",
            Globals.uiConstants().createFolder(),
            new CreateFolderAction(ssm));
        addMenuItem(
            "create-page",
            Globals.uiConstants().createPage(),
            new CreatePageAction(ssm));
        addMenuItem(
            "create-template",
            Globals.uiConstants().createTemplate(),
            new CreateTemplateAction(ssm));
        addMenuItem(
            "choose-template",
            Globals.uiConstants().chooseTemplate(),
            new ChooseTemplateAction(ssm));
    }
}
