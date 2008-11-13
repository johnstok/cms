/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ccc.contentcreator.api.JsonModelData;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.CellRenderer;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;


/**
 * Generic dialog for displaying read-only, tabular data.
 *
 * @author Civic Computing Ltd.
 */
public class TableDataDisplayDialog
    extends
        AbstractBaseDialog {

    private final List<JsonModelData> _data;
    private final ListStore<JsonModelData> _dataStore =
        new ListStore<JsonModelData>();

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param data The data the dialog should display.
     */
    public TableDataDisplayDialog(final String title,
                                  final List<JsonModelData> data) {

        super(title);

        _data = data;
        _dataStore.add(_data);

        setLayout(new FitLayout());

        final TableColumnModel cm = defineColumnModel();

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<JsonModelData> binder =
            new TableBinder<JsonModelData>(tbl, _dataStore);
        binder.init();

        add(tbl);
    }

    /*
     * TODO: Should be abstract and overridden by subclasses OR column model
     * passed into constructor.
     */
    private TableColumnModel defineColumnModel() {

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        columns.add(new TableColumn("action", "Action", PERCENT_10));
        columns.add(new TableColumn("actor", "User", PERCENT_10));
        final TableColumn timeColumn =
            new TableColumn("happenedOn", "Time", PERCENT_30);
        timeColumn.setRenderer(new CellRenderer<Component>(){

            public String render(final Component item,
                                 final String property,
                                 final Object value) {

                final Date happenedOn = new Date(((Double) value).longValue());
                return happenedOn.toString(); // TODO: I18n
            }});
        columns.add(timeColumn);
        columns.add(new TableColumn("summary", "Summary", PERCENT_50));

        final TableColumnModel cm = new TableColumnModel(columns);
        return cm;
    }
}
