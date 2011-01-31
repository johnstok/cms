/*-----------------------------------------------------------------------------
 * Copyright (c) 2011 Civic Computing Ltd.
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

import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.types.SortOrder;
import ccc.client.core.InternalServices;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.GetResourcesPagedAction;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A search panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class SearchTable
    extends
        AbstractResourceTable {

    private final ResourceSummary _root;
    private final PagingToolBar _pagerBar;
    private final ToolBar _toolBar;
    
    private final Button _searchButton;
    private final CheckBox _locked;
    private final TextField<String> _searchString;
    
    private final String _preferences;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    SearchTable(final ResourceSummary root,
                  final ResourceTree tree,
                  final String preferences) {

        InternalServices.REMOTING_BUS.registerHandler(this);
        _preferences = preferences;
        _root = root;
        _tree = tree;
        _toolBar = new ToolBar();

        _searchString = new TextField<String>();
        _searchString.setToolTip(UI_CONSTANTS.searchToolTip());
        
        _locked = new CheckBox();
        _locked.setBoxLabel(UI_CONSTANTS.locked());
        _locked.setFieldLabel(UI_CONSTANTS.locked());
        _locked.setValue(Boolean.FALSE);
        
        _searchButton = new Button(UI_CONSTANTS.search());
        _searchButton.addListener(Events.Select, new SearchListener());
        
        _toolBar.add(_searchString);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_locked);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_searchButton);
        
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

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
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


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceSummary model) {
        _tree.addResource(model);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary currentFolder() {
        throw new UnsupportedOperationException("Method not implemented.");
    }
    

    /**
     * Listener for resource search.
     *
     * @author Civic Computing Ltd.
     */
    private final class SearchListener implements Listener<ComponentEvent> {

        public void handleEvent(final ComponentEvent be) {
            _detailsStore.removeAll();
            updatePager();
        }
    }
    

    private void updatePager(){

        final RpcProxy<PagingLoadResult<BeanModel>> proxy =
            new RpcProxy<PagingLoadResult<BeanModel>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <BeanModel>> callback) {
                if (null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<BeanModel> plr =
                       new BasePagingLoadResult<BeanModel>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/config.getLimit()+1;
                    final SortOrder order = (
                        config.getSortDir() == Style.SortDir.ASC
                        ? SortOrder.ASC : SortOrder.DESC);

                    final ResourceCriteria criteria = new ResourceCriteria();
                    String term = "%";
                    if (_searchString.getValue() != null) {
                        term = _searchString.getValue().replace('*', '%')+"%";
                    }
                    criteria.setName(term);
                    criteria.setSortField(config.getSortField());
                    criteria.setSortOrder(order);
                    if (_locked.getValue().booleanValue()) {
                        criteria.setLocked(true);
                    }

                    new GetResourcesPagedAction(criteria,
                        page,
                        config.getLimit()) {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                        @Override protected void execute(
                                 final Collection<ResourceSummary> children,
                                 final int totalCount) {
                            final List<BeanModel> results =
                                DataBinding.bindResourceSummary(children);

                            final PagingLoadResult<BeanModel> plr =
                                new BasePagingLoadResult<BeanModel>(
                            results, config.getOffset(), totalCount);
                            callback.onSuccess(plr);
                        }
                    }.execute();
                }
            }
        };


        final PagingLoader<PagingLoadResult<BeanModel>> loader = 
            new BasePagingLoader<PagingLoadResult<BeanModel>>(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }
}
