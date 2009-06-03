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

import ccc.api.ActionSummary;
import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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
    private final ListStore<ActionSummaryModelData> _actionStore =
        new ListStore<ActionSummaryModelData>();
    private final Grid<ActionSummaryModelData> _grid;

    /**
     * Constructor.
     */
    public ActionTable() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        setId("action-details");
        setHeading(UI_CONSTANTS.actionDetails());
        setLayout(new FitLayout());

        setTopComponent(new ActionToolBar(this));

        addColumn(
            configs,
            ActionSummaryModelData.Property.LOCALISED_TYPE.name(),
            UI_CONSTANTS.action(),
            TYPE_COLUMN);
        addColumn(
            configs,
            ActionSummaryModelData.Property.ACTOR.name(),
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
            ActionSummaryModelData.Property.SUBJECT_TYPE.name(),
            UI_CONSTANTS.resourceType(),
            SMALL_COLUMN);
        addColumn(
            configs,
            ActionSummaryModelData.Property.PATH.name(),
            UI_CONSTANTS.resourcePath(),
            MEDIUM_COLUMN);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<ActionSummaryModelData>(_actionStore, cm);
        _grid.setAutoExpandColumn(ActionSummaryModelData.Property.PATH.name());
        _grid.setLoadMask(true);
        _grid.setId("action-grid");

        add(_grid);
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
     * TODO: replace with displayPendingActions(), displayCompletedActions()...
     *
     * @param selectedItem The tree item selection.
     */
    public void displayActionsFor(final TreeItem selectedItem) {
        _actionStore.removeAll();
        if (ActionTree.PENDING.equals(selectedItem.getId())) {
            qs.listPendingActions(
                new ErrorReportingCallback<Collection<ActionSummary>>(USER_ACTIONS.viewActions()) {
                    public void onSuccess(final Collection<ActionSummary> result) {
                        _actionStore.add(DataBinding.bindActionSummary(result));
                    }
                }
            );
        } else if (ActionTree.COMPLETED.equals(selectedItem.getText())){
            qs.listCompletedActions(
                new ErrorReportingCallback<Collection<ActionSummary>>(USER_ACTIONS.viewActions()) {
                    public void onSuccess(final Collection<ActionSummary> result) {
                        _actionStore.add(DataBinding.bindActionSummary(result));
                    }
                }
            );
        }
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
