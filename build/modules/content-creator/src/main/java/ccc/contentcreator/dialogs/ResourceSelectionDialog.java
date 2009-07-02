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
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ResourceTree;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Dialog for resource selection.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSelectionDialog extends Window {

    /** DIALOG_HEIGHT : int. */
    private static final int DIALOG_HEIGHT = 225;
    /** DIALOG_WIDTH : int. */
    private static final int DIALOG_WIDTH = 400;
    private final ResourceTree _tree;
    private final UIConstants _constants = new IGlobalsImpl().uiConstants();

    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     */
    public ResourceSelectionDialog(final ResourceSummary targetRoot) {
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_constants.selectResource());
        setWidth(DIALOG_WIDTH);
        setMinWidth(IGlobals.MIN_WIDTH);
        setHeight(DIALOG_HEIGHT);
        setLayout(new FitLayout());

        _tree = new ResourceTree(targetRoot);
        add(_tree);

        final Button save = new Button(
            _constants.save(),
            new SelectionListener<ComponentEvent>() {
                @Override
                public void componentSelected(final ComponentEvent ce) {
                    close();
                }
            }
        );
        save.setId("ResourceSelectSave");
        addButton(save);
    }

    /**
     * Accessor for selected folder.
     *
     * @return Returns the selected folder as {@link FolderDTO}
     */
    public ResourceSummaryModelData selectedResource() {
        return
            (null==_tree.getSelectedItem())
                ? null
                : (ResourceSummaryModelData) _tree.getSelectedItem().getModel();
    }
}
