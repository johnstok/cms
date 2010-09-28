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
import java.util.List;
import java.util.UUID;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;


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

    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

    final PagingLoader<PagingLoadResult<BeanModel>> loader;

    private final ResourceSummary _root;
    private final ResourceTree _tree;
    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;
    private final FolderToolBar toolBar;

    private final GridFilters _filters;

    private final ResourceProxy _proxy;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    ResourceTable(final ResourceSummary root,
        final ResourceTree tree) {

        InternalServices.REMOTING_BUS.registerHandler(this);
        _root = root;
        _tree = tree;
        toolBar = new FolderToolBar(this);
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

        _proxy = new ResourceProxy(null, null);

        loader = new BasePagingLoader<PagingLoadResult<BeanModel>>(_proxy) {
            @Override
            protected Object newLoadConfig() {
                     final BasePagingLoadConfig config = new BaseFilterPagingLoadConfig();
                     return config;
            }
        };
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
        _pagerBar.bind(loader);

        _filters = new GridFilters();
        final StringFilter nameFilter = new StringFilter("name");
        nameFilter.getMenu().setAutoWidth(false);
        nameFilter.getMenu().setWidth(190);
        _filters.addFilter(nameFilter);
        _filters.setLocal(false);
        _grid.addPlugin(_filters);

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
     * @param folder The parent folder for the records to display in the table.
     */
    public void displayResourcesFor(final ResourceSummary folder) {
        _proxy.setFolder(folder);
        final ColumnModel cm = _grid.getColumnModel();
        _filters.clearFilters();
        _grid.reconfigure(_detailsStore, cm);
        loader.load(0, PAGING_ROW_COUNT);
    }


    /**
     * Reloads the detail store.
     *
     */
    public void reload() {
        if (_detailsStore.getLoader() != null) {
            _detailsStore.getLoader().load();
        }
    }


    /**
     * Config method for the columns.
     *
     * @param configs list of configurations.
     */
    public void createColumnConfigs(final List<ColumnConfig> configs) {

        final GridCellRenderer<BeanModel> rsRenderer =
            ResourceTypeRendererFactory.rendererForResourceSummary();

        final ColumnConfig typeColumn =
            new ColumnConfig(
                ResourceSummary.TYPE,
                UI_CONSTANTS.type(),
                40);
        typeColumn.setRenderer(rsRenderer);
        configs.add(typeColumn);

        final ColumnConfig workingCopyColumn =
            new ColumnConfig(
                ResourceSummary.WORKING_COPY,
                UI_CONSTANTS.draft(),
                40);
        workingCopyColumn.setSortable(false);
        workingCopyColumn.setMenuDisabled(true);
        workingCopyColumn.setRenderer(rsRenderer);
        configs.add(workingCopyColumn);

        final ColumnConfig mmIncludeColumn =
            new ColumnConfig(
                ResourceSummary.MM_INCLUDE,
                UI_CONSTANTS.menu(),
                40);
        mmIncludeColumn.setRenderer(rsRenderer);
        configs.add(mmIncludeColumn);

        final ColumnConfig createdColumn =
            new ColumnConfig(
                ResourceSummary.DATE_CREATED,
                UI_CONSTANTS.dateCreated(),
                100);
        createdColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        createdColumn.setHidden(true);
        configs.add(createdColumn);

        final ColumnConfig updatedColumn =
            new ColumnConfig(
                ResourceSummary.DATE_CHANGED,
                UI_CONSTANTS.dateChanged(),
                100);
        updatedColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        updatedColumn.setHidden(true);
        configs.add(updatedColumn);

        final ColumnConfig visibleColumn =
            new ColumnConfig(
                ResourceSummary.VISIBLE,
                UI_CONSTANTS.visible(),
                45);
        visibleColumn.setRenderer(rsRenderer);
        visibleColumn.setHidden(true);
        configs.add(visibleColumn);

        final ColumnConfig lockedColumn =
            new ColumnConfig(
                ResourceSummary.LOCKED,
                UI_CONSTANTS.lockedBy(),
                80);
        configs.add(lockedColumn);

        final ColumnConfig publishedByColumn =
            new ColumnConfig(
                ResourceSummary.PUBLISHED,
                UI_CONSTANTS.publishedBy(),
                80);
        configs.add(publishedByColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig(
                ResourceSummary.NAME,
                UI_CONSTANTS.name(),
                250);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig(
                ResourceSummary.TITLE,
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
                                      final ListStore<ModelData> ds) {
                final ResourceSummary rs =
                    ((BeanModel) model).<ResourceSummary>getBean();
                return rs.getName()+"_row";
            }
        };
        final GridView view = _grid.getView();
        view.setViewConfig(vc);
        _grid.setView(view);

        final GridSelectionModel<BeanModel> gsm =
            new GridSelectionModel<BeanModel>();
        gsm.setSelectionMode(SelectionMode.SINGLE);
        _grid.setSelectionModel(gsm);
        _grid.setAutoExpandColumn(
            ResourceSummary.TITLE);
    }


    /** {@inheritDoc} */
    public ResourceSummary tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        final BeanModel selected = _grid.getSelectionModel().getSelectedItem();
        return (null==selected) ? null : selected.<ResourceSummary>getBean();
    }


    /** {@inheritDoc} */
    public ResourceSummary treeSelection() {
        final ResourceSummary item = _tree.getSelectedItem();
        if (item != null && item.getType() == ResourceType.RANGE_FOLDER) {
            return null;
        }
        return item;
    }


    /** {@inheritDoc} */
    public void update(final ResourceSummary model) {
        updateResource(model.getId());
        _tree.updateResource(model);
    }


    /**
     * Remove a resource from the data store.
     *
     * @param item The model to remove.
     */
    public void delete(final UUID item) {
        removeResource(item);
        _tree.removeResource(item);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceSummary model) {
        final boolean addedToSelectedTreeNode = _tree.addResource(model);
        if (addedToSelectedTreeNode) { addResource(model); }
    }


    private void updateResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, id);
        if (null!=tBean) {
            _detailsStore.update(tBean);
        }
    }


    private void removeResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, id);
        if (null!=tBean) {
            _detailsStore.remove(tBean);
        }
    }


    private void addResource(final ResourceSummary model) {
        final BeanModel tBean = DataBinding.bindResourceSummary(model);
        _detailsStore.add(tBean);
    }


    /** {@inheritDoc} */
    public void move(final ResourceSummary model,
                     final ResourceSummary newParent,
                     final ResourceSummary oldParent) {
        removeResource(model.getId());
        _tree.move(oldParent, newParent, model);
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case RESOURCE_PUBLISH:
                mergeAndUpdate(event.<ResourceSummary>getProperty("resource"));
                break;

            case RESOURCE_DELETE:
                delete(event.<UUID>getProperty("resource"));
                break;

            case FILE_CREATE:
            case PAGE_CREATE:
            case FOLDER_CREATE:
            case ALIAS_CREATE:
                create(event.<ResourceSummary>getProperty("resource"));
                break;

            case RESOURCE_RENAME:
                final BeanModel bm1 =
                    _detailsStore.findModel(
                        ResourceSummary.UUID, event.getProperty("id"));
                final ResourceSummary md1 = bm1.<ResourceSummary>getBean();
                md1.setAbsolutePath(
                    event.<ResourcePath>getProperty("path").toString());
                md1.setName(
                    new ResourceName(event.<String>getProperty("name")));
                update(md1);
                break;

            case RESOURCE_CHANGE_TEMPLATE:
                final BeanModel bm2 =
                    _detailsStore.findModel(
                        ResourceSummary.UUID, event.getProperty("resource"));
                if (null==bm2) { return; } // Not present in table.
                final ResourceSummary md2 = bm2.<ResourceSummary>getBean();
                md2.setTemplateId(event.<UUID>getProperty("template"));
                update(md2);
                break;

            case RESOURCE_CLEAR_WC:
                final ResourceSummary item1 =
                    event.getProperty("resource");
                item1.setHasWorkingCopy(false);
                update(item1);
                break;

            case RESOURCE_APPLY_WC:
                final ResourceSummary item2 =
                    event.getProperty("resource");
                item2.setHasWorkingCopy(false);
                update(item2);
                break;

            default:
                break;
        }
    }


    private void mergeAndUpdate(final ResourceSummary rs) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, rs.getId());
        if (null!=tBean) {
            tBean.setProperties(
                DataBinding.bindResourceSummary(rs).getProperties());
            update(tBean.<ResourceSummary>getBean());
        }
    }


    @Override
    public void create(final Resource model) {
        create((ResourceSummary) model);
    }
}
