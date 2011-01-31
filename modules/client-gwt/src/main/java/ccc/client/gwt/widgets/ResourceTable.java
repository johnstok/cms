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

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.InternalServices;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;


/**
 * A panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable
    extends
    AbstractResourceTable {

    private static final int FILTER_MENU_WIDTH = 190;

    private final PagingLoader<PagingLoadResult<BeanModel>> _loader;

    private final ResourceSummary _root;

    private final PagingToolBar _pagerBar;
    private final FolderToolBar _toolBar;

    private final GridFilters _filters;

    private final ResourceProxy _proxy;
    
    private final String _preferences;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    ResourceTable(final ResourceSummary root,
                  final ResourceTree tree,
                  final String preferences) {

        InternalServices.REMOTING_BUS.registerHandler(this);
        _preferences = preferences;
        _root = root;
        _tree = tree;
        _toolBar = new FolderToolBar(this);
        setTopComponent(_toolBar);
        setHeading(UI_CONSTANTS.resourceDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new ResourceContextMenu(this, root);
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu, this);
        gp.setRenderer(new ResourceContextRenderer());
        configs.add(gp);
        createColumnConfigs(configs);
        applyPreferences(configs);
        
        _proxy = new ResourceProxy(null, null);

        _loader = new BasePagingLoader<PagingLoadResult<BeanModel>>(_proxy) {
            @Override
            protected Object newLoadConfig() {
                     final BasePagingLoadConfig config =
                         new BaseFilterPagingLoadConfig();
                     return config;
            }
        };
        _loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(_loader);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        _grid.addListener(
            Events.CellDoubleClick,
            new Listener<GridEvent<BeanModel>>(){
                public void handleEvent(final GridEvent<BeanModel> ge) {
                    final ResourceSummary rs = ge.getModel().getBean();
                    if (ResourceType.FOLDER==rs.getType()) {
                        _tree.clearSelection();
                        displayResourcesFor(rs);
                    }
                }
            });

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
        _pagerBar.bind(_loader);

        _filters = new GridFilters();
        final StringFilter nameFilter = new StringFilter("name");
        nameFilter.getMenu().setAutoWidth(false);
        nameFilter.getMenu().setWidth(FILTER_MENU_WIDTH);
        _filters.addFilter(nameFilter);
        _filters.setLocal(false);
        _grid.addPlugin(_filters);

        add(_grid);
    }


    private void applyPreferences(List<ColumnConfig> configs) {
    	if (_preferences != null) {
    		for (ColumnConfig config : configs) {
    			if (_preferences.indexOf(config.getId()) == -1 
    					&& !config.getHeader().equals("")) {
    				config.setHidden(true);
    			} else {
    				config.setHidden(false);
    			}
    		}
    	}
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
        if (null==folder) { return; }
        if (_proxy.isDisplaying(folder.getId())) {
            return; // Same folder.
        }
        _proxy.setFolder(folder);
        final ColumnModel cm = _grid.getColumnModel();
        _filters.clearFilters();
        _grid.reconfigure(_detailsStore, cm);
        _loader.load(0, PAGING_ROW_COUNT);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceSummary model) {
        _tree.addResource(model);
        addResource(model);
    }


    private void addResource(final ResourceSummary model) {
        if (_proxy.isDisplaying(model.getParent())) {
            final BeanModel tBean = DataBinding.bindResourceSummary(model);
            _detailsStore.add(tBean);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary currentFolder() {
        return _proxy.getFolder();
    }
    
}
