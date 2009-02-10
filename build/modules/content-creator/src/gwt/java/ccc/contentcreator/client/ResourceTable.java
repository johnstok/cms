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
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
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
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


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
    private TreeItem _parentFolder = null;
    private final ToolBar _toolBar = new FolderToolBar(this);

    final ResourceSummary _root;
    final FolderResourceTree _tree;
    final Grid<ModelData> _grid;

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
        setHeading("Resource Details"); // TODO: I18n.
        setLayout(new FitLayout());

//        _tbl.setHorizontalScroll(true);

        final Menu contextMenu = new ResourceContextMenu(this);
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        configs.add(gp);

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<ModelData>(_detailsStore, cm);
        _grid.setId("ResourceGrid");

        _grid.setLoadMask(true);
        _grid.setBorders(false);
        final GridViewConfig vc = new GridViewConfig() {
            /** {@inheritDoc} */
            @Override
            public String getRowStyle(ModelData model,
                                      int rowIndex,
                                      ListStore ds) {

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
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);
    }


    /**
     * Updated this table to render the children of the specified TreeItem.
     *
     * @param selectedItem The item whose children we should display.
     */
    public void displayResourcesFor(final TreeItem selectedItem) {
        _parentFolder = selectedItem;
        _detailsStore.removeAll();

        // TODO: handle getSelectedItem() being null.
        final ModelData f = selectedItem.getModel();

        qs.getChildren(
            f.<String>get("id"),
            new ErrorReportingCallback<Collection<ResourceSummary>>() {
                public void onSuccess(
                                  final Collection<ResourceSummary> result) {
                    final List<ModelData> models =
                        DataBinding.bindResourceSummary(result);
                    if (models.isEmpty()) {
                        detailsStore().removeAll();
                    } else {
                        detailsStore().add(models);
                    }
                }
        });
    }


    /**
     * Refresh view.
     */
    public void refreshTable() {
        if (_parentFolder  != null) {
            displayResourcesFor(_parentFolder);
        }
    }


    /**
     * Accessor for the details store.
     *
     * @return This table's details store.
     */
    protected ListStore<ModelData> detailsStore() {
        return _detailsStore;
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


    /** {@inheritDoc} */
    public ModelData getSelectedModel() {
        return _grid.getSelectionModel().getSelectedItem();
    }


    /** {@inheritDoc} */
    public void notifyUpdate(final ModelData model) {
        detailsStore().update(model);
    }

    /** {@inheritDoc} */
    public ModelData getSelectedFolder() {
        return _parentFolder.getModel();
    }

    /** {@inheritDoc} */
    public void refresh() {
        refreshTable();
    }

    /** {@inheritDoc} */
    public void add(final ModelData model) {
        detailsStore().add(model);
    }
}
