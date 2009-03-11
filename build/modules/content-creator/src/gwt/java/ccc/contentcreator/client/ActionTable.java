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

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.services.api.ActionSummary;

import com.extjs.gxt.ui.client.data.ModelData;
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
    private static final int SMALL_COLUMN = 100;
    private final ListStore<ModelData> _actionStore =
        new ListStore<ModelData>();
    private final Grid<ModelData> _grid;

    /**
     * Constructor.
     */
    public ActionTable() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        setId("action-details");
        setHeading("Action Details"); // FIXME: I18n
        setLayout(new FitLayout());

        setTopComponent(new ActionToolBar(this));

        addColumn(configs, "type", "Action", SMALL_COLUMN); // FIXME: I18n
        addColumn(configs, "actor", "Scheduled by", SMALL_COLUMN); // FIXME: I18n
        addColumn(configs, "executeAfter", "Scheduled for", SMALL_COLUMN) // FIXME: I18n
            .setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        addColumn(configs, "status", "Status", SMALL_COLUMN); // FIXME: I18n
        addColumn(configs, "subjectType", "Resource type", SMALL_COLUMN); // FIXME: I18n
        addColumn(configs, "path", "Resource path", MEDIUM_COLUMN); // FIXME: I18n

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<ModelData>(_actionStore, cm);
        _grid.setAutoExpandColumn("path");
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
        if ("Pending".equals(selectedItem.getText())) { // FIXME: Will break with I18n
            qs.listPendingActions(
                new ErrorReportingCallback<Collection<ActionSummary>>() {
                    public void onSuccess(final Collection<ActionSummary> result) {
                        _actionStore.add(DataBinding.bindActionSummary(result));
                    }
                }
            );
        } else if ("Completed".equals(selectedItem.getText())){  // FIXME: Will break with I18n
            qs.listCompletedActions(
                new ErrorReportingCallback<Collection<ActionSummary>>() {
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
    public ModelData getSelectedItem() {
        return _grid.getSelectionModel().getSelectedItem();
    }

    /**
     * Notify the table that a row has been updated.
     *
     * @param action The row that was updated.
     */
    public void update(final ModelData action) {
        _actionStore.update(action);
    }

}
