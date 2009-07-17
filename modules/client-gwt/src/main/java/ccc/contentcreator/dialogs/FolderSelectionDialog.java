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

import ccc.api.ResourceSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.FolderResourceTree;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * A dialog used to choose a single folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSelectionDialog extends Window {

    private static final int HEIGHT = 225;
    private static final int WIDTH = 400;

    private final FolderResourceTree _tree;
    private final IGlobals _globals = new IGlobalsImpl();

    /**
     * Constructor.
     *
     * @param root ResourceSummary root.
     */
    FolderSelectionDialog(final ResourceSummary root) {
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_globals.uiConstants().selectFolder());
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setMinWidth(IGlobals.MIN_WIDTH);
        setLayout(new FitLayout());
        _tree = new FolderResourceTree(root, _globals);
        add(_tree);
        final Button save = new Button(
            _globals.uiConstants().ok(),
            new SelectionListener<ComponentEvent>() {
                @Override
                public void componentSelected(final ComponentEvent ce) {
                    if (null==_tree.getSelectedItem()) {
                        return; // No selection made.
                    }
                    close();
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
