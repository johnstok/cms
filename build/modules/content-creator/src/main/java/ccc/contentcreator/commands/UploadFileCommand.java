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
package ccc.contentcreator.commands;

import ccc.contentcreator.client.Application;
import ccc.contentcreator.client.GWTSupport;
import ccc.contentcreator.dialogs.UploadFileDialog;

import com.google.gwt.user.client.ui.Tree;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UploadFileCommand extends ApplicationCommand {
    private Tree _tree = null;

    /**
     * Constructor.
     *
     * @param t Tree to be passed as a parameter.
     * @param application The application used by this command.
     */
    public UploadFileCommand(final Application application, final Tree t) {
        super(application);
        _tree = t;
    }

    /**
     * {@inheritDoc}
     */
    public void execute() {
        if (_tree.getSelectedItem() == null) {
            _app.alert("Select a folder from the tree first.");
            return;
        }
        final String name = _tree.getSelectedItem().getText();
        final String absolutePath =
            GWTSupport.calculatePathForTreeItem(_tree.getSelectedItem());

        new UploadFileDialog(_app, absolutePath, name).center();
    }
}
