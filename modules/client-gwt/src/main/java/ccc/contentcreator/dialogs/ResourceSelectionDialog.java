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
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ResourceTree;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
    private final IGlobals _globals = new IGlobalsImpl();

    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     */
    public ResourceSelectionDialog(final ResourceSummary targetRoot) {
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_globals.uiConstants().selectResource());
        setWidth(DIALOG_WIDTH);
        setMinWidth(IGlobals.MIN_WIDTH);
        setHeight(DIALOG_HEIGHT);
        setLayout(new FitLayout());

        _tree = new ResourceTree(targetRoot, _globals);
        add(_tree.getTree());

        final Button save = new Button(
            _globals.uiConstants().save(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    hide();
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
            (null==_tree.getTree().getSelectionModel().getSelectedItem())
                ? null
                : (ResourceSummaryModelData) _tree.getTree().getSelectionModel().getSelectedItem();
    }
}
