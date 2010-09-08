/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import ccc.api.core.ResourceSummary;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.GetResourcesPagedAction;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class SearchResourceTable
    extends
        TablePanel {

    protected static final int ENTER_KEY = 13;

    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

    private final ResourceSummary _root;
    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;
    private final ToolBar _toolBar;
    private final TextField<String> _filterString = new TextField<String>();

    /**
     * Constructor.
     *
     * @param root ResourceSummary
     */
    public SearchResourceTable(final ResourceSummary root) {

        _root = root;
        _toolBar = new ToolBar();
        _filterString.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == ENTER_KEY) {
                    _detailsStore.getLoader().load();
                }
            }
        });
        _toolBar.add(_filterString);

        setTopComponent(_toolBar);
        setHeading(UI_CONSTANTS.resourceDetails());
        setLayout(new FitLayout());

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, cm);
        setUpGrid();

        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
        displayResourcesFor();
    }


    /**
     * Updated this table to render the children of the specified TreeItem.
     *
     */
    public void displayResourcesFor() {

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

                    final int page =  config.getOffset()/ config.getLimit()+1;
                    final SortOrder order =
                        (config.getSortDir() == Style.SortDir.ASC)
                            ? SortOrder.ASC
                            : SortOrder.DESC;

                    String name = getFilter();
                    new GetResourcesPagedAction(
                        _root.list(),
                        name,
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

        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }


    private void createColumnConfigs(final List<ColumnConfig> configs) {

        final GridCellRenderer<BeanModel> rsRenderer =
            ResourceTypeRendererFactory.rendererForResourceSummary();

        final ColumnConfig typeColumn =
            new ColumnConfig(
                ResourceSummary.TYPE,
                UI_CONSTANTS.type(),
                40);
        typeColumn.setRenderer(rsRenderer);
        configs.add(typeColumn);

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



    public ResourceSummary tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        final BeanModel selected = _grid.getSelectionModel().getSelectedItem();
        return (null==selected) ? null : selected.<ResourceSummary>getBean();
    }

    /**
     * Return the filter string value, appended with % if necessary.
     *
     * @return The filter string.
     */
    public String getFilter() {
        String value =  _filterString.getValue();
        if (value == null) {
            return "%";
        }
        if (value.endsWith("%")) {
            return value;
        }
        return value+"%";
    }

}
