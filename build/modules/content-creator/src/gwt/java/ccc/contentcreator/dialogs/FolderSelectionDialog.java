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

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.FolderResourceTree;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.Scroll;
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
    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     * @param root ResourceSummary root.
     */
    FolderSelectionDialog(final ResourceSummary root) {
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_constants.selectFolder());
        setWidth(400);
        setHeight(225);
        setLayout(new FitLayout());
        _tree = new FolderResourceTree(root);
        add(_tree);
        final Button save = new Button(
            _constants.ok(),
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
    ResourceSummaryModelData selectedFolder() {
        return
            (null==_tree.getSelectedItem())
                ? null
                : (ResourceSummaryModelData) _tree.getSelectedItem().getModel();
    }
}
