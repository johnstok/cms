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
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.Permission;
import ccc.api.types.SortOrder;
import ccc.client.core.HasSelection;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.remoting.ListActionsAction;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionTable
    extends
        TablePanel
    implements
        EventHandler<CommandType>,
        HasSelection<ActionSummary> {

    private static final int MEDIUM_COLUMN = 200;
    private static final int TYPE_COLUMN = 150;
    private static final int SMALL_COLUMN = 100;
    private static final int V_SMALL_COLUMN = 40;
    private ListStore<BeanModel> _actionStore = new ListStore<BeanModel>();
    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;

    /**
     * Constructor.
     */
    public ActionTable() {

        InternalServices.REMOTING_BUS.registerHandler(this);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        setId("action-details");
        setHeading(UI_CONSTANTS.actionDetails());
        setLayout(new FitLayout());

        if (GLOBALS.currentUser().hasPermission(Permission.ACTION_CANCEL)) {
            setTopComponent(new ActionToolBar(this));
        }

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<BeanModel>(_actionStore, cm);
        _grid.setAutoExpandColumn(ActionSummary.PATH);
        _grid.setId("action-grid");

        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }

    private void createColumnConfigs(final List<ColumnConfig> configs) {
        addColumn(
            configs,
            ActionSummary.TYPE,
            UI_CONSTANTS.action(),
            TYPE_COLUMN).setRenderer(
                ResourceTypeRendererFactory.rendererForActionSummary());
        addColumn(
            configs,
            ActionSummary.USERNAME,
            UI_CONSTANTS.scheduledBy(),
            SMALL_COLUMN);
        addColumn(
            configs,
            ActionSummary.EXECUTE_AFTER,
            UI_CONSTANTS.scheduledFor(),
            SMALL_COLUMN)
            .setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        addColumn(
            configs,
            ActionSummary.STATUS,
            UI_CONSTANTS.status(),
            SMALL_COLUMN)
            .setRenderer(
                ResourceTypeRendererFactory.rendererForActionSummary());
        addColumn(
            configs,
            ActionSummary.FAILURE_CODE,
            UI_CONSTANTS.failureCode(),
            SMALL_COLUMN);
        addColumn(
            configs,
            ActionSummary.SUBJECT_TYPE,
            UI_CONSTANTS.type(),
            V_SMALL_COLUMN)
            .setRenderer(
                ResourceTypeRendererFactory.rendererForActionSummary());
        addColumn(
            configs,
            ActionSummary.PATH,
            UI_CONSTANTS.resourcePath(),
            MEDIUM_COLUMN);
    }

    private ColumnConfig addColumn(final List<ColumnConfig> configs,
                                   final String id,
                                   final String header,
                                   final int columnWidth) {
        final ColumnConfig column = new ColumnConfig();
        column.setId(id);
        column.setHeader(header);
        column.setWidth(columnWidth);
        configs.add(column);
        return column;
    }

    /**
     * Change the action data that is to be displayed.
     *
     * @param selected The tree item selection.
     */
    public void displayActionsFor(final ModelData selected) {
        _actionStore.removeAll();

        final DataProxy<PagingLoadResult<BeanModel>> proxy =
            new RpcProxy<PagingLoadResult<BeanModel>>() {

                @Override
                protected void load(
                    final Object loadConfig,
                    final AsyncCallback<PagingLoadResult<BeanModel>> callback) {

                    if (null==loadConfig
                        || !(loadConfig instanceof BasePagingLoadConfig)) {
                        final PagingLoadResult<BeanModel> plr =
                           new BasePagingLoadResult<BeanModel>(
                               null);
                        callback.onSuccess(plr);

                    } else {
                        final BasePagingLoadConfig config =
                            (BasePagingLoadConfig) loadConfig;

                        final int page =
                            config.getOffset()/ config.getLimit()+1;
                        final SortOrder order =
                            (Style.SortDir.ASC==config.getSortDir())
                                ? SortOrder.ASC
                                : SortOrder.DESC;
                        if (selected.get("id") != null) {
                            String id = (String) selected.get("id");
                            if (ActionTree.ACTIONS.equals(id)) {
                                id = null;
                            }
                            getActions(callback,
                                config,
                                id,
                                page,
                                order)
                                .execute();
                        }
                    }
                }
            };

        updatePager(proxy);
    }


    private ListActionsAction getActions(
         final AsyncCallback<PagingLoadResult<BeanModel>> callback,
         final BasePagingLoadConfig config,
         final String status,
         final int page,
         final SortOrder order) {

        return new ListActionsAction(status,
            page,
            config.getLimit(),
            config.getSortField(),
            order) {

            /** {@inheritDoc} */
            @Override
            protected void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            protected void execute(final PagedCollection<ActionSummary> actions) {
                final List<BeanModel> results =
                    DataBinding.bindActionSummary(actions.getElements());

                final PagingLoadResult<BeanModel> plr =
                    new BasePagingLoadResult<BeanModel>(
                            results, config.getOffset(), (int) actions.getTotalCount());
                callback.onSuccess(plr);
            }
        };
    }


    private void updatePager(final DataProxy proxy){
        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _actionStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_actionStore, cm);
    }

    /**
     * Query the currently selected row.
     *
     * @return The selected row, or NULL if no row is selected.
     */
    public ActionSummary getSelectedItem() {
        final BeanModel selected = _grid.getSelectionModel().getSelectedItem();
        return (null==selected) ? null : selected.<ActionSummary>getBean();
    }

    /**
     * Notify the table that a row has been updated.
     *
     * @param action The row that was updated.
     */
    public void update(final BeanModel action) {
        _actionStore.update(action);
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case ACTION_CANCEL:
                final ActionSummary as = event.getProperty("action");
                final BeanModel bm =
                    _actionStore.findModel(ActionSummary.ID, as.getId());
                if (null!=bm) {
                    bm.set(ActionSummary.STATUS, ActionStatus.CANCELLED);
                    update(bm);
                }
                break;

            default:
                break;
        }
    }
}
