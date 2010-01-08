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
package ccc.contentcreator.client;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


/**
 * Resource tree with contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class EnhancedResourceTree extends FolderResourceTree {

    private final Menu _contextMenu;
    private final LeftRightPane _view;
    private final ResourceTable _rt;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param view LeftRightPane of the surrounding view.
     * @param user UserSummary of currently logged in user.
     * @param globals An instance of the globals interface.
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view,
                         final UserDto user,
                         final IGlobals globals) {

        super(root, globals);

        _rt = new ResourceTable(root, this, user);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        store().addStoreListener(
            new StoreListener<ResourceSummaryModelData>(){

                /** {@inheritDoc} */
                @Override
                public void storeDataChanged(
                             final StoreEvent<ResourceSummaryModelData> se) {
                    super.storeDataChanged(se);
                    final TreeStoreEvent<ResourceSummaryModelData> te =
                        (TreeStoreEvent<ResourceSummaryModelData>) se;
                    final boolean itemSelected = null!=getSelectedItem();
                     if (itemSelected
                         && te.getParent() == getSelectedItem().getModel()) {
                         _rt.displayResourcesFor(te.getModel());
                     }
                }

            }
        );

        final Listener<TreeEvent> treeSelectionListener =
            new Listener<TreeEvent>() {
                public void handleEvent(final TreeEvent te) {
                    final TreeItem ti = getSelectedItem();

                    // #327. in case root folder is collapsed.
                    if (ti == null) {
//                        _rt.displayResourcesFor(
//                            new ArrayList<ResourceSummaryModelData>());
                        return;
                    }
                    final ResourceSummaryModelData selectedModel =
                        (ResourceSummaryModelData) ti.getModel();

                    _rt.displayResourcesFor(selectedModel);


                    final int folderCount = selectedModel.getFolderCount();
                    final int childCount = selectedModel.getChildCount();

                    // FIXME is this needed??
//                    if (folderCount > 0) {         // Children are loaded.
//                        ti.setExpanded(true);
//                    } else if (childCount > 0
//                        && children.size() == 0) { // Children not loaded.
//                        getBinder().loadChildren(ti);
//                    }
                }
            };

        addListener(
            Events.SelectionChange,
            treeSelectionListener
        );

        _contextMenu.setId("navigator-menu");
        setContextMenu(_contextMenu);
    }


    /**
     * Displays {@link ResourceTable} in the right hand pane.
     */
    void showTable() {
        _view.setRightHandPane(_rt);
    }
}
