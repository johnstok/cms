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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.remoting.GetChildrenPagedAction;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
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
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable
    extends
        TablePanel
    implements
        EventHandler<CommandType>,
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
     */
    ResourceTable(final ResourceSummary root,
        final FolderResourceTree tree) {

        InternalServices.REMOTING_BUS.registerHandler(this);

        _root = root;
        _tree = tree;
        final ToolBar toolBar = new FolderToolBar(this);
        setTopComponent(toolBar);
        setHeading(UI_CONSTANTS.resourceDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new ResourceContextMenu(this);
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
     * @param folder The parent folder for the records to display in the table.
     */
    public void displayResourcesFor(final ResourceSummaryModelData folder) {

        final RpcProxy<PagingLoadResult<ResourceSummaryModelData>> proxy =
            new RpcProxy<PagingLoadResult<ResourceSummaryModelData>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <ResourceSummaryModelData>> callback) {
                if (folder == null
                    || null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<ResourceSummaryModelData> plr =
                       new BasePagingLoadResult<ResourceSummaryModelData>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/ config.getLimit()+1;
                    final SortOrder order =
                        (config.getSortDir() == Style.SortDir.ASC)
                            ? SortOrder.ASC
                            : SortOrder.DESC;

                    new GetChildrenPagedAction(
                        folder.getDelegate(),
                        page,
                        config.getLimit(),
                        config.getSortField(),
                        order,
                        null) {
                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                        @Override protected void execute(
                                 final Collection<ResourceSummary> children,
                                 final int totalCount) {
                            final List<ResourceSummaryModelData> results =
                                DataBinding.bindResourceSummary(children);

                            final PagingLoadResult<ResourceSummaryModelData> plr =
                                new BasePagingLoadResult<ResourceSummaryModelData>(
                            results, config.getOffset(), totalCount);
                            callback.onSuccess(plr);
                        }
                    }.execute();
                }
            }

        };

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
        workingCopyColumn.setSortable(false);
        workingCopyColumn.setMenuDisabled(true);
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
        _grid.setLoadMask(true);
        _grid.setBorders(false);

        // Assign a CSS style for each row with GridViewConfig
        final GridViewConfig vc = new GridViewConfig() {
            /** {@inheritDoc} */
            @Override
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
        final ResourceSummaryModelData item =
            _tree.treePanel().getSelectionModel().getSelectedItem();
        if (item == null) {
            return null;
        }
        return item;
    }


    /** {@inheritDoc} */
    public void update(final ResourceSummaryModelData model) {
        final String uuidPropertyName =
            ResourceSummaryModelData.Property.UUID.name();
        final ResourceSummaryModelData parent =
            _tree.store().findModel(uuidPropertyName, model.getParent());
        _detailsStore.update(model);
        _tree.store().update(model);
        if (null!=parent) {
            _tree.treePanel().setExpanded(parent, false);
            _tree.treePanel().setExpanded(parent, true);
        }
    }


    /**
     * Remove a resource from the data store.
     *
     * @param item The model to remove.
     */
    public void delete(final UUID item) {
        final String uuidPropertyName =
            ResourceSummaryModelData.Property.UUID.name();

        // Handle tree
        final ResourceSummaryModelData treeData =
            _tree.store().findModel(uuidPropertyName, item);
        if (null!=treeData) {
            final ResourceSummaryModelData parent =
                _tree.store().findModel(uuidPropertyName, treeData.getParent());
            _tree.store().remove(treeData);

            if (null!=parent) {
                if (ResourceType.FOLDER==treeData.getType()) {
                    parent.decrementFolderCount();
                }
                _tree.treePanel().setExpanded(parent, false);
                if (parent.getFolderCount()<1) {
                    _tree.treePanel().setLeaf(parent, true);
                } else {
                    _tree.treePanel().setExpanded(parent, true);
                }
            }
            _tree.store().update(parent);
        }

        // Handle table
        final ResourceSummaryModelData tableData =
            _detailsStore.findModel(uuidPropertyName, item);
        if (null!=tableData) {
            _detailsStore.remove(tableData);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceSummaryModelData model) {
        final ResourceSummaryModelData np =
            _tree.store().findModel(
                ResourceSummaryModelData.Property.UUID.name(),
                model.getParent());
        if (null!=np) { // May not exist in the store
            if (model.getType() == ResourceType.FOLDER) {
                // Add to the left-hand tree
                final TreeStore<ResourceSummaryModelData> store = _tree.store();
                store.add(np, model, false);
                final String uuidPropertyName =
                    ResourceSummaryModelData.Property.UUID.name();
                final ResourceSummaryModelData destinationFolder =
                    store.findModel(uuidPropertyName, model.getParent());
                destinationFolder.incrementFolderCount();
                _tree.store().update(model);
                _tree.store().update(destinationFolder);
            }
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

        final TreeStore<ResourceSummaryModelData> store = _tree.store();
        store.remove(oldParent, model);

        final String uuidPropertyName =
            ResourceSummaryModelData.Property.UUID.name();
        final ResourceSummaryModelData destinationFolder =
            store.findModel(uuidPropertyName, newParent.getId());

        if (null!=destinationFolder) { // May not exist in other store
            final String newPath = newParent.getAbsolutePath();
            final String oldPath = oldParent.getAbsolutePath();
            final String currentPath = model.getAbsolutePath();
            final String newModelPath =
                currentPath.replaceFirst(oldPath, newPath);
            model.setAbsolutePath(newModelPath);
            if (ResourceType.FOLDER==model.getType()) {
                destinationFolder.incrementFolderCount();
                store.add(destinationFolder, model, false);
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case RESOURCE_DELETE:
                delete(event.<UUID>getProperty("resource"));
                break;

            case FILE_CREATE:
            case PAGE_CREATE:
            case FOLDER_CREATE:
            case ALIAS_CREATE:
                final ResourceSummaryModelData resourceModelData =
                    new ResourceSummaryModelData(
                        event.<ResourceSummary>getProperty("resource"));
                create(resourceModelData);
                break;

            case RESOURCE_RENAME:
                final ResourceSummaryModelData md1 =
                    _detailsStore.findModel("UUID", event.getProperty("id"));
                md1.setAbsolutePath(
                    event.<ResourcePath>getProperty("path").toString());
                md1.setName(
                    event.<String>getProperty("name"));
                update(md1);
                break;

            case RESOURCE_CHANGE_TEMPLATE:
                final ResourceSummaryModelData md2 =
                    _detailsStore.findModel(
                        "UUID", event.getProperty("resource"));
                if (null==md2) { return; } // Not present in table.
                md2.setTemplateId(event.<UUID>getProperty("template"));
                update(md2);
                break;

            case RESOURCE_CLEAR_WC:
                final ResourceSummaryModelData item1 =
                    event.getProperty("resource");
                item1.setWorkingCopy(false);
                update(item1);
                break;

            case RESOURCE_APPLY_WC:
                final ResourceSummaryModelData item2 =
                    event.getProperty("resource");
                item2.setWorkingCopy(false);
                update(item2);
                break;

            default:
                break;
        }
    }
}
