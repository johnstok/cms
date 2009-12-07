/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A tree control for navigating scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionTree {

    /** ACTIONS : String. */
    public static final String ACTIONS = "actions";
    /** PENDING : String. */
    public static final String PENDING = "pending";
    /** COMPLETED : String. */
    public static final String COMPLETED = "completed";
    
    /** TODO read from a properties file **/
    private static final String BACKGROUND_COLOUR = "white";
    private static final String BACKGROUND_ATTRIBUTE = "background";
    private static final boolean EXPANDED = true;
    private static final boolean NOT_EXPANDED = false;
    private static final boolean ADD_ALL_CHILDREN = true;
    private static final String NULL_ICON_PATH = null;
    
    /** ACTION_TREE_HEIGHT : int. 
        TODO read from a properties file */
    private static final int ACTION_TREE_HEIGHT = 300;
    
    private final ActionTable _actionTable = new ActionTable();
    private final UIConstants _uiConstants = new IGlobalsImpl().uiConstants();
    private final LeftRightPane _view;

    private final TreeStore<ModelData> _store = new TreeStore<ModelData>();
    private final TreePanel<ModelData> _tree = new TreePanel<ModelData>(_store);
    
    /**
     * Constructor.
     *
     * @param view The split pane used to render this tree.
     */
    public ActionTree(final LeftRightPane view) {
        _view = view;
        _tree.setDisplayProperty("name");
        
        _tree.setIconProvider(new ModelIconProvider<ModelData>() {
            public AbstractImagePrototype getIcon(final ModelData model) {
                if (model.get("icon") != null) {
                    return IconHelper.createPath((String) model.get("icon"));
                }
                return null;
            }
        });
        
        _tree.setHeight(ACTION_TREE_HEIGHT);
        
        final ModelData actions = getNewItem(_uiConstants.actions(), ACTIONS);
        _store.add(actions, ADD_ALL_CHILDREN);
        _tree.setLeaf(actions, false);
        _tree.setExpanded(actions, EXPANDED);
        
        final ModelData pending = 
            getNewItem(_uiConstants.pending(), PENDING, ImagePaths.HOURGLASS);
        _store.add(actions, pending, ADD_ALL_CHILDREN);
        _tree.setLeaf(pending, true);
        _tree.setExpanded(pending, NOT_EXPANDED);
        
        final ModelData completed = 
            getNewItem(_uiConstants.completed(), COMPLETED, ImagePaths.ACCEPT);
        _store.add(actions, completed, ADD_ALL_CHILDREN);
        _tree.setLeaf(completed, true);
        _tree.setExpanded(completed, NOT_EXPANDED);

        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute(BACKGROUND_ATTRIBUTE, BACKGROUND_COLOUR);
        
        _tree.getSelectionModel().addSelectionChangedListener(
            new ActionSelectedListener());
    }
    
    /**
     * Selection listener for {@link ActionTree}.
     *
     * @author Civic Computing Ltd.
     */
    public class ActionSelectedListener
        extends
            SelectionChangedListener<ModelData> {

        /** {@inheritDoc} */
        @Override
        public void selectionChanged(final SelectionChangedEvent<ModelData> 
                                         selectionChangedEvent) {
            final ModelData selectedItem =
                selectionChangedEvent.getSelectedItem();
            _actionTable.displayActionsFor(selectedItem);
        }
    }

    /**
     * Notify the split pane that the tree's table should be displayed.
     */
    public void showTable() {
        _view.setRightHandPane(_actionTable);
    }
    
    /**
     * Accessor.
     *
     * @return The tree.
     */
    public TreePanel<ModelData> getTree() {
        return _tree;
    }
    
    private ModelData getNewItem(final String name, final String id) {
        return getNewItem(name, id, NULL_ICON_PATH);
    }
    
    private ModelData getNewItem(final String name,
                                 final String id,
                                 final String iconPath) {
        final ModelData baseModelData = new BaseModelData();
        
        baseModelData.set("name", name);
        baseModelData.set("id", id);
        
        if (iconPath != null) {
            baseModelData.set("icon", iconPath);
        }
        
        return baseModelData;
    }
}

