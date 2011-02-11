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

import java.util.Map;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.Globals;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;


/**
 * Resource tree with contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class EnhancedResourceTree
    extends
        ResourceTree {

    private final Menu _contextMenu;
    private final LeftRightPane _view;
    private final ResourceTable _rt;
    private final SearchTable _st;

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
        final Map<String, String> usermeta = globals.currentUser().getMetadata();
        final String columnPref = usermeta.get(ColumnConfigSupport.RESOURCE_COLUMNS);
        _rt = new ResourceTable(root, this, columnPref);
        _st = new SearchTable(root, EnhancedResourceTree.this, columnPref);
        _view = view;
        _contextMenu = new FolderContextMenu(_rt);

        addSelectionChangedListener(
            new SelectionChangedListener<BeanModel>(){
                @Override public void selectionChanged(
                     final SelectionChangedEvent<BeanModel> se) {
                    final BeanModel item = se.getSelectedItem();
                    if (item != null) {
                        if (item.<ResourceSummary>getBean().getType() == ResourceType.SEARCH ) {
                            _view.setRightHandPane(_st);
                        } else {
                            _view.setRightHandPane(_rt);
                            _rt.displayResourcesFor(
                                item.<ResourceSummary>getBean());
                        }
                    }
                }
            }
        );

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
