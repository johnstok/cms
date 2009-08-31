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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.ResourceSummary;
import ccc.rest.UserSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


/**
 * A panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable
    extends
        TablePanel
    implements
        SingleSelectionModel {

    private ListStore<ResourceSummaryModelData> _detailsStore =
        new ListStore<ResourceSummaryModelData>();

    private final ResourceSummary _root;
    private final FolderResourceTree _tree;
    private final Grid<ResourceSummaryModelData> _grid;
    private final PagingToolBar _pagerBar;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     * @param user UserSummary of currently logged in user.
     */
    ResourceTable(final ResourceSummary root,
        final FolderResourceTree tree,
        final UserSummary user) {

        _root = root;
        _tree = tree;
        final ToolBar toolBar = new FolderToolBar(this, user);
        setTopComponent(toolBar);
        setHeading(UI_CONSTANTS.resourceDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new ResourceContextMenu(this, user);
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new ResourceContextRenderer());
        configs.add(gp);

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<ResourceSummaryModelData>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
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
    @SuppressWarnings("unchecked")
    public void displayResourcesFor(final List<ResourceSummaryModelData> data) {
        final PagingModelMemoryProxy proxy = new PagingModelMemoryProxy(data);
        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<ResourceSummaryModelData>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }


    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final ColumnConfig typeColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.TYPE.name(),
                UI_CONSTANTS.type(),
                40);
        typeColumn.setRenderer(
            ResourceTypeRendererFactory.rendererForResourceSummary());
        configs.add(typeColumn);

        final ColumnConfig workingCopyColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.WORKING_COPY.name(),
                UI_CONSTANTS.draft(),
                40);
        workingCopyColumn.setRenderer(new ResourceWorkingCopyRenderer());
        configs.add(workingCopyColumn);

        final ColumnConfig mmIncludeColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.MM_INCLUDE.name(),
                UI_CONSTANTS.menu(),
                40);
        mmIncludeColumn.setRenderer(new ResourceIncludedInMainMenuRenderer());
        configs.add(mmIncludeColumn);

        final ColumnConfig lockedColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.LOCKED.name(),
                UI_CONSTANTS.lockedBy(),
                80);
        configs.add(lockedColumn);

        final ColumnConfig publishedByColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.PUBLISHED.name(),
                UI_CONSTANTS.publishedBy(),
                80);
        configs.add(publishedByColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.NAME.name(),
                UI_CONSTANTS.name(),
                250);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig(
                ResourceSummaryModelData.Property.TITLE.name(),
                UI_CONSTANTS.title(),
                250);
        configs.add(titleColumn);
    }


    private void setUpGrid() {
        _grid.setId("ResourceGrid");

        _grid.setBorders(false);

        // Assign a CSS style for each row with GridViewConfig
        final GridViewConfig vc = new GridViewConfig() {
            /** {@inheritDoc} */
            @Override @SuppressWarnings("unchecked")
            public String getRowStyle(final ModelData model,
                                      final int rowIndex,
                                      final ListStore ds) {
                final ResourceSummaryModelData rs =
                    (ResourceSummaryModelData) model;
                return rs.getName()+"_row";
            }
        };
        final GridView view = _grid.getView();
        view.setViewConfig(vc);
        _grid.setView(view);

        final GridSelectionModel<ResourceSummaryModelData> gsm =
            new GridSelectionModel<ResourceSummaryModelData>();
        gsm.setSelectionMode(SelectionMode.SINGLE);
        _grid.setSelectionModel(gsm);
        _grid.setAutoExpandColumn(
            ResourceSummaryModelData.Property.TITLE.name());
    }



    /** {@inheritDoc} */
    public ResourceSummaryModelData tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        return _grid.getSelectionModel().getSelectedItem();
    }


    /** {@inheritDoc} */
    public ResourceSummaryModelData treeSelection() {
        final TreeItem item = _tree.getSelectedItem();
        if (item == null) {
            return null;
        }
        return (ResourceSummaryModelData) item.getModel();
    }


    /** {@inheritDoc} */
    public void update(final ResourceSummaryModelData model) {
        _detailsStore.update(model);
        _tree._store.update(model);
    }


    /** {@inheritDoc} */
    public void create(final ResourceSummaryModelData model,
                       final ResourceSummaryModelData newParent) {
        final ResourceSummaryModelData np =
            _tree._store.findModel(ResourceSummaryModelData.Property.ID.name(),
                                   newParent.getId());
        if (null!=np) { // May not exist in the store
            _tree._store.add(np, model, false); // Add to the left-hand tree

            if (np.equals(treeSelection())) {   // Add to the right-hand table
                _detailsStore.add(model);
            }
        }
    }


    /** {@inheritDoc} */
    public void move(final ResourceSummaryModelData model,
                     final ResourceSummaryModelData newParent,
                     final ResourceSummaryModelData oldParent) {
        _detailsStore.remove(model);
        _tree._store.remove(oldParent, model);

        final ResourceSummaryModelData np =
            _tree._store.findModel(ResourceSummaryModelData.Property.ID.name(),
                                   newParent.getId());
        if (null!=np) { // May not exist in other store
            _tree._store.add(np, model, false);
        }
    }
}
