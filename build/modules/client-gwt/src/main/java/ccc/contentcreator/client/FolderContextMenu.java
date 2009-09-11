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
