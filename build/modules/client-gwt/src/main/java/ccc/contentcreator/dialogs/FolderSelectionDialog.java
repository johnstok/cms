/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.FolderResourceTree;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    if (null==_tree.getSelectedItem()) {
                        return; // No selection made.
                    }
                    hide(ce.getButton());
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
