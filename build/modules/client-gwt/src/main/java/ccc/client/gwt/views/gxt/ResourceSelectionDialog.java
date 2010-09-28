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
package ccc.client.gwt.views.gxt;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.widgets.ResourceTree;
import ccc.client.gwt.widgets.SearchResourceTable;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Dialog for resource selection.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSelectionDialog extends Window {

    private static final int DIALOG_HEIGHT = 396;
    private static final int DIALOG_WIDTH = 400;
    private final ResourceTree _tree;

    private final TabPanel tabPanel = new TabPanel();
    private final TabItem treeTab = new TabItem(I18n.UI_CONSTANTS.tree());
    private final TabItem searchTab = new TabItem(I18n.UI_CONSTANTS.search());
    private final SearchResourceTable tt;


    /**
     * Constructor.
     *
     * @param targetRoot The root resource containing resources.
     */
    public ResourceSelectionDialog(final ResourceSummary targetRoot,
                                   final ResourceType type) {
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setHeading(I18n.UI_CONSTANTS.selectResource());
        setWidth(DIALOG_WIDTH);
        setMinWidth(Globals.MIN_WIDTH);
        setHeight(DIALOG_HEIGHT);
        setLayout(new FitLayout());

        treeTab.setHeight(200);
        treeTab.setScrollMode(Scroll.AUTOY);
        tabPanel.add(treeTab);

        tt = new SearchResourceTable(type);
        searchTab.add(tt);
        searchTab.setLayout(new FitLayout());
        searchTab.setScrollMode(Scroll.AUTOY);
        tabPanel.add(searchTab);

        _tree = new ResourceTree(targetRoot, type);
        treeTab.add(_tree.asComponent());

        final Button save = new Button(
            I18n.UI_CONSTANTS.save(),
            new SelectionListener<ButtonEvent>() {
                @Override public void componentSelected(final ButtonEvent ce) {
                    hide();
                }
            }
        );

        add(tabPanel);
        save.setId("ResourceSelectSave");
        addButton(save);
    }

    /**
     * Accessor for selected resource.
     *
     * @return Returns the selected resource as {@link ResourceSummary}
     */
    public ResourceSummary selectedResource() {
        if (tabPanel.getSelectedItem() == treeTab) {
            return _tree.getSelectedItem();
        }
        return tt.tableSelection();
    }
}
