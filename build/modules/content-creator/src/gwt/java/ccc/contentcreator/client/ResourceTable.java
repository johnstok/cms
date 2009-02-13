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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable
    extends
        TablePanel
    implements
        SingleSelectionModel {

    private final UIConstants _constants = Globals.uiConstants();
    private final ListStore<ModelData> _detailsStore =
        new ListStore<ModelData>();
    private final ToolBar _toolBar = new FolderToolBar(this);

    private final ResourceSummary _root;
    private final FolderResourceTree _tree;
    private final Grid<ModelData> _grid;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    ResourceTable(final ResourceSummary root, final FolderResourceTree tree) {

        _root = root;
        _tree = tree;

        setTopComponent(_toolBar);
        setHeading(_constants.resourceDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new ResourceContextMenu(this);
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        configs.add(gp);

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<ModelData>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);
    }


    /**
     * Accessor for this table's root.
     *
     * @return The resource summary for the root.
     */
    public ResourceSummary root() {
        return _root;
    }


    /**
     * Updated this table to render the children of the specified TreeItem.
     *
     * @param data A list of records to display in the table.
     */
    public void displayResourcesFor(final List<ModelData> data) {
        _detailsStore.removeAll();
        if (data.size() > 0) { // Grid throws exception with empty list.
            _detailsStore.add(data);
        }
    }


    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final ColumnConfig typeColumn =
            new ColumnConfig("type", _constants.type(), 70);
        configs.add(typeColumn);

        final ColumnConfig lockedColumn =
            new ColumnConfig("locked", _constants.lockedBy(), 80);
        configs.add(lockedColumn);

        final ColumnConfig mmIncludeColumn =
            new ColumnConfig("mmInclude", _constants.mainMenu(), 70);
        configs.add(mmIncludeColumn);

        final ColumnConfig publishedByColumn =
            new ColumnConfig("published", _constants.publishedBy(), 80);
        configs.add(publishedByColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig("name", _constants.name(), 250);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig("title", _constants.title(), 250);
        configs.add(titleColumn);
    }


    private void setUpGrid() {
        _grid.setId("ResourceGrid");

        _grid.setLoadMask(true);
        _grid.setBorders(false);

        // Assign a CSS style for each row with GridViewConfig
        final GridViewConfig vc = new GridViewConfig() {
            /** {@inheritDoc} */
            @Override @SuppressWarnings("unchecked")
            public String getRowStyle(final ModelData model,
                                      final int rowIndex,
                                      final ListStore ds) {

                return model.<String>get("name")+"_row";
            }
        };
        final GridView view = _grid.getView();
        view.setViewConfig(vc);
        _grid.setView(view);

        final GridSelectionModel<ModelData> gsm =
            new GridSelectionModel<ModelData>();
        gsm.setSelectionMode(SelectionMode.SINGLE);
        _grid.setSelectionModel(gsm);
        _grid.setAutoExpandColumn("title");
    }



    /** {@inheritDoc} */
    public ModelData tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        return _grid.getSelectionModel().getSelectedItem();
    }


    /** {@inheritDoc} */
    public ModelData treeSelection() {
        return _tree.getSelectedItem().getModel();
    }


    /** {@inheritDoc} */
    public void update(final ModelData model) {
        _detailsStore.update(model);
        _tree._store.update(model);
    }


    /** {@inheritDoc} */
    public void create(final ModelData model, final ModelData newParent) {
        final ModelData np = _tree._store.findModel("id", newParent.get("id"));

        if (newParent.equals(treeSelection())) {
            _detailsStore.add(model);
        }

        if (null!=np) { // May not exist in other store
            _tree._store.add(np, model, false);
        }
    }


    /** {@inheritDoc} */
    public void move(final ModelData model,
                     final ModelData newParent,
                     final ModelData oldParent) {
        _detailsStore.remove(model);
        _tree._store.remove(oldParent, model);

        final ModelData np = _tree._store.findModel("id", newParent.get("id"));
        if (null!=np) { // May not exist in other store
            _tree._store.add(np, model, false);
        }
    }
}
