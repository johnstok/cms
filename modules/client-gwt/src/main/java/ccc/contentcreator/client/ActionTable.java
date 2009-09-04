/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.contentcreator.actions.ListCompletedActionsAction;
import ccc.contentcreator.actions.ListPendingActionsAction;
import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.binding.DataBinding;
import ccc.rest.dto.ActionSummary;

import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * A panel that displays scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionTable extends TablePanel {

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
     * @param selectedItem The tree item selection.
     */
    public void displayActionsFor(final TreeItem selectedItem) {
        _actionStore.removeAll();
        if (ActionTree.PENDING.equals(selectedItem.getId())) {
            displayPendingActions();
        } else if (ActionTree.COMPLETED.equals(selectedItem.getId())){
            displayCompletedActions();
        }
    }

    private void displayCompletedActions() {
        new ListCompletedActionsAction(this).execute();
    }

    private void displayPendingActions() {
        new ListPendingActionsAction(this).execute();
    }

    /**
     * Update the data display by the table.
     *
     * @param actions The actions to display.
     */
    @SuppressWarnings("unchecked")
    public void updatePagingModel(final Collection<ActionSummary> actions) {

        final List<ActionSummaryModelData> data =
            DataBinding.bindActionSummary(actions);

        final PagingModelMemoryProxy proxy = new PagingModelMemoryProxy(data);
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

}
