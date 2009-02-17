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
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTree;
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
public class ResourceSelectionDialog extends Window {

    private final ResourceTree _tree;
    private final UIConstants _constants = Globals.uiConstants();

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
        setWidth(400);
        setHeight(225);
        setLayout(new FitLayout());

        _tree = new ResourceTree(targetRoot);
        add(_tree);

        final Button save = new Button(
            Globals.uiConstants().save(),
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
    public ModelData selectedResource() {
        return
            (null==_tree.getSelectedItem())
                ? null
                : _tree.getSelectedItem().getModel();
    }
}
