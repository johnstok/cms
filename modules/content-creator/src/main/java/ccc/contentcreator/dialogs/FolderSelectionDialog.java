/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.client.FolderResourceTree;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSelectionDialog extends Window {

    private final FolderResourceTree _tree;

    /**
     * Constructor.
     */
    FolderSelectionDialog(final ResourceSummary root) {
        setBodyBorder(false);
        setScrollMode(Scroll.AUTOY);
        setHeading("Select a folder"); // TODO: Move to UIConstants
        setWidth(400);
        setHeight(225);
        setLayout(new FitLayout());
        _tree = new FolderResourceTree(root);
        add(_tree);
        final Button save = new Button(
            "OK", // TODO: Move to UIConstants
            new SelectionListener<ComponentEvent>() {
                @Override
                public void componentSelected(final ComponentEvent ce) {
                    if (null==_tree.getSelectedItem()) {
                        // Error!
                    } else {
                        close();
                    }
                }});
        save.setId("FolderSelectSave");
        addButton(save);
    }

    /**
     * Accessor for selected folder.
     *
     * @return Returns the selected folder as {@link FolderDTO}
     */
    ModelData selectedFolder() {
        return
            (null==_tree.getSelectedItem())
                ? null
                : _tree.getSelectedItem().getModel();
    }
}
