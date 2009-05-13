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
package ccc.contentcreator.client;

import java.util.List;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.services.api.ResourceSummary;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.Events;
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
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view,
                         final UserSummary user) {

        super(root);

        _rt = new ResourceTable(root, this, user);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        _store.addStoreListener(
            new StoreListener<ResourceSummaryModelData>(){

                /** {@inheritDoc} */
                @Override
                public void storeDataChanged(final StoreEvent<ResourceSummaryModelData> se) {
                    super.storeDataChanged(se);
                    final TreeStoreEvent<ResourceSummaryModelData> te =
                        (TreeStoreEvent<ResourceSummaryModelData>) se;
                    final boolean itemSelected = null!=getSelectedItem();
                     if (itemSelected
                         && te.parent == getSelectedItem().getModel()) {
                         _rt.displayResourcesFor(te.children);
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
                        _rt.displayResourcesFor(null);
                        return;
                    }
                    final ResourceSummaryModelData selectedModel =
                        (ResourceSummaryModelData) ti.getModel();

                    final List<ResourceSummaryModelData> children =
                        _store.getChildren(selectedModel);
                    _rt.displayResourcesFor(children);


                    final int folderCount = selectedModel.getFolderCount();
                    final int childCount = selectedModel.getChildCount();

                    if (folderCount > 0) {         // Children are loaded.
                        ti.setExpanded(true);
                    } else if (childCount > 0
                        && children.size() == 0) { // Children not loaded.
                        _binder.loadChildren(ti);
                    }
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
     * TODO: Add a description of this method.
     */
    void showTable() {
        _view.setRightHandPane(_rt);
    }
}
