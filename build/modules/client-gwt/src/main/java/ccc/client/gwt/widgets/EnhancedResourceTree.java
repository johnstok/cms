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
package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.Globals;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;


/**
 * Resource tree with contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class EnhancedResourceTree extends ResourceTree {

    private final Menu _contextMenu;
    private final LeftRightPane _view;
    private final ResourceTable _rt;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param view LeftRightPane of the surrounding view.
     * @param globals An instance of the globals interface.
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view,
                         final Globals globals) {

        super(root, ResourceType.FOLDER);

        _rt = new ResourceTable(root, this);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        addSelectionChangedListener(
            new SelectionChangedListener<BeanModel>(){
                @Override public void selectionChanged(
                     final SelectionChangedEvent<BeanModel> se) {
                    final BeanModel item = se.getSelectedItem();
                    if (item != null) {
                        _rt.displayResourcesFor(
                            item.<ResourceSummary>getBean());
                    }
                }
            }
        );

        final Listener<TreePanelEvent<BeanModel>> listener =
            new Listener<TreePanelEvent<BeanModel>>() {
                @Override public void handleEvent(
                           final TreePanelEvent<BeanModel> be) {
                    _rt.displayResourcesFor(
                        getSelectedItem());
                }
            };

        addListener(Events.SelectionChange, listener);

        setContextMenu(_contextMenu);
        setStyleAttribute("overflow", "hidden");
    }


    /**
     * Displays {@link ResourceTable} in the right hand pane.
     */
    void showTable() {
        _view.setRightHandPane(_rt);
    }
}
