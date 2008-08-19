/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.view.contentcreator.commands;

import ccc.view.contentcreator.client.GWTSupport;
import ccc.view.contentcreator.dialogs.UploadFileDialog;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UploadFileCommand implements Command {
    private Tree _tree = null;

    /**
     * Constructor.
     *
     * @param t Tree to be passed as a parameter.
     */
    public UploadFileCommand(final Tree t) {
        _tree = t;
    }

    /**
     * {@inheritDoc}
     */
    public void execute() {
        if (_tree.getSelectedItem() == null) {
            Window.alert("Select a folder from the tree first.");
        }
        final String name = _tree.getSelectedItem().getText();
        final String absolutePath =
            GWTSupport.calculatePathForTreeItem(_tree.getSelectedItem());

        new UploadFileDialog(absolutePath, name).center();
    }
}