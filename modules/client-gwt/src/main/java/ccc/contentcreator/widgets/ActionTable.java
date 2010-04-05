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
package ccc.contentcreator.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.events.ActionCancelled;
import ccc.contentcreator.events.ActionCancelled.ActionCancelledHandler;
import ccc.contentcreator.remoting.ListCompletedActionsAction;
import ccc.contentcreator.remoting.ListPendingActionsAction;
import ccc.rest.dto.ActionSummary;
import ccc.types.ActionStatus;
import ccc.types.SortOrder;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
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
        ActionCancelledHandler {

    private static final int MEDIUM_COLUMN = 200;
    private static final int TYPE_COLUMN = 150;
    private static final int SMALL_COLUMN = 100;
    private ListStore<ActionSummaryModelData> _actionStore =
        new ListStore<ActionSummaryModelData>();
    private final Grid<ActionSummaryModelData> _grid;
    private final PagingToolBar _pagerBar;

    /**
     * Constructor.
     */
    public ActionTable() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        setId("action-details");
        setHeading(UI_CONSTANTS.actionDetails());
        setLayout(new FitLayout());

        setTopComponent(new ActionToolBar(this));

        createColumnConfigs(configs);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<ActionSummaryModelData>(_actionStore, cm);
        _grid.setAutoExpandColumn(ActionSummaryModelData.Property.PATH.name());
        _grid.setId("action-grid");

        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }

    private void createColumnConfigs(final List<ColumnConfig> configs) {
        addColumn(
            configs,
            ActionSummaryModelData.Property.LOCALISED_TYPE.name(),
            UI_CONSTANTS.action(),
            TYPE_COLUMN);
        addColumn(
            configs,
            ActionSummaryModelData.Property.USERNAME.name(),
            UI_CONSTANTS.scheduledBy(),
            SMALL_COLUMN);
        addColumn(
            configs,
            ActionSummaryModelData.Property.EXECUTE_AFTER.name(),
            UI_CONSTANTS.scheduledFor(),
            SMALL_COLUMN)
            .setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        addColumn(
            configs,
            ActionSummaryModelData.Property.LOCALISED_STATUS.name(),
            UI_CONSTANTS.status(),
            SMALL_COLUMN);
        addColumn(
            configs,
            ActionSummaryModelData.Property.FAILURE_CODE.name(),
            UI_CONSTANTS.failureCode(),
            SMALL_COLUMN);

        final ColumnConfig subjectTypeColumn = addColumn(
            configs,
            ActionSummaryModelData.Property.SUBJECT_TYPE.name(),
            UI_CONSTANTS.resourceType(),
            SMALL_COLUMN);
        subjectTypeColumn.setRenderer(
            ResourceTypeRendererFactory.rendererForActionSummary());

        addColumn(
            configs,
            ActionSummaryModelData.Property.PATH.name(),
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

        final DataProxy<PagingLoadResult<ActionSummaryModelData>> proxy =
            new RpcProxy<PagingLoadResult<ActionSummaryModelData>>() {

                @Override
                protected void load(final Object loadConfig,
                                    final AsyncCallback<PagingLoadResult<ActionSummaryModelData>> callback) {

                    if (null==loadConfig
                        || !(loadConfig instanceof BasePagingLoadConfig)) {
                        final PagingLoadResult<ActionSummaryModelData> plr =
                           new BasePagingLoadResult<ActionSummaryModelData>(null);
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
                        if (ActionTree.PENDING.equals(selected.get("id"))) {
                            getPendingActions(callback, config, page, order)
                                .execute();
                        } else if (ActionTree.COMPLETED.equals(selected.get("id"))){
                            getCompletedActions(callback, config, page, order)
                                .execute();
                        }
                    }
                }
            };

        updatePager(proxy);
    }


    private ListCompletedActionsAction getCompletedActions(
       final AsyncCallback<PagingLoadResult<ActionSummaryModelData>> callback,
       final BasePagingLoadConfig config,
       final int page,
       final SortOrder order) {

        return new ListCompletedActionsAction(page,
                         config.getLimit(),
                         config.getSortField(),
                         order) {

            /** {@inheritDoc} */
            @Override
            protected void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            protected void execute(
                       final Collection<ActionSummary> comments,
                       final int totalCount) {

                final List<ActionSummaryModelData> results =
                    DataBinding.bindActionSummary(comments);

                final PagingLoadResult<ActionSummaryModelData> plr =
                    new BasePagingLoadResult<ActionSummaryModelData>
                (results, config.getOffset(), totalCount);
                callback.onSuccess(plr);
            }
        };
    }

    private ListPendingActionsAction getPendingActions(
         final AsyncCallback<PagingLoadResult<ActionSummaryModelData>> callback,
         final BasePagingLoadConfig config,
         final int page,
         final SortOrder order) {

        return new ListPendingActionsAction(page,
            config.getLimit(),
            config.getSortField(),
            order) {

            /** {@inheritDoc} */
            @Override
            protected void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            protected void execute(
                                   final Collection<ActionSummary> comments,
                                   final int totalCount) {
                final List<ActionSummaryModelData> results =
                    DataBinding.bindActionSummary(comments);

                final PagingLoadResult<ActionSummaryModelData> plr =
                    new BasePagingLoadResult<ActionSummaryModelData>
                (results, config.getOffset(), totalCount);
                callback.onSuccess(plr);
            }
        };
    }


    @SuppressWarnings("unchecked")
    private void updatePager(final DataProxy proxy){
        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _actionStore = new ListStore<ActionSummaryModelData>(loader);
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
    public ActionSummaryModelData getSelectedItem() {
        return _grid.getSelectionModel().getSelectedItem();
    }

    /**
     * Notify the table that a row has been updated.
     *
     * @param action The row that was updated.
     */
    public void update(final ActionSummaryModelData action) {
        _actionStore.update(action);
    }

    /** {@inheritDoc} */
    @Override
    public void onCancel(final ActionCancelled event) {
        final ActionSummaryModelData action = event.getAction();
        action.setStatus(ActionStatus.CANCELLED);
        update(action);
    }
}
