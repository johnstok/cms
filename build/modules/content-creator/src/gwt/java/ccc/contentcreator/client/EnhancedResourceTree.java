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
import java.util.Set;

import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
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
     * @param roles Set of roles.
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view,
                         final Set<String> roles) {

        super(root);

        _rt = new ResourceTable(root, this, roles);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        _store.addStoreListener(
            new StoreListener<ModelData>(){

                /** {@inheritDoc} */
                @Override
                public void storeDataChanged(final StoreEvent<ModelData> se) {
                    super.storeDataChanged(se);
                    final TreeStoreEvent<ModelData> te =
                        (TreeStoreEvent<ModelData>) se;
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
                    TreeItem ti = getSelectedItem();

                    // #327. in case root folder is collapsed.
                    if (ti == null) {
                        _rt.displayResourcesFor(null);
                        return;
                    }
                    ModelData selectedModel = ti.getModel();

                    List<ModelData> children = _store.getChildren(selectedModel);
                    _rt.displayResourcesFor(children);


                    int folderCount =
                        selectedModel.<Integer>get("folderCount").intValue();
                    int childCount =
                        selectedModel.<Integer>get("childCount").intValue();

                    if (folderCount > 0) {
                        ti.setExpanded(true);
                    }else if (childCount > 0 && children.size() == 0) { // Data not loaded
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
