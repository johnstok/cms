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
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    FolderToolBar(final SingleSelectionModel ssm) {
        addSeparator();
        addButton(
            "uploadFile",
            "Upload File",
            new CreateFileAction(ssm)); // TODO: I18n
        addSeparator();
        addButton(
            "Create Folder",
            "Create Folder",
            new CreateFolderAction(ssm)); // TODO: I18n
        addSeparator();
        addButton(
            "Create Page",
            "Create Page",
            new CreatePageAction(ssm)); // TODO: I18n
        addSeparator();
        addButton(
            "Create Template",
            "Create Template",
            new CreateTemplateAction(ssm)); // TODO: I18n
        addSeparator();
        addButton(
            "Choose Template",
            "Choose Template",
            new ChooseTemplateAction(ssm)); // TODO: I18n
        addSeparator();
    }
}
