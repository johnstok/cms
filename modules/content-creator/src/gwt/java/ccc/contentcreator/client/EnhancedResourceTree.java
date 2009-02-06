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

import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;


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
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view) {

        super(root);

        _rt = new ResourceTable(root, this);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        final Listener<TreeEvent> treeSelectionListener =
            new Listener<TreeEvent>() {
                public void handleEvent(final TreeEvent te) {
                    _rt.displayResourcesFor(te.tree.getSelectedItem());
                    _view.setRightHandPane(_rt);
                }
            };

        addListener(
            Events.SelectionChange,
            treeSelectionListener
        );

        _contextMenu.setId("navigator-menu");
        setContextMenu(_contextMenu);
    }
}
