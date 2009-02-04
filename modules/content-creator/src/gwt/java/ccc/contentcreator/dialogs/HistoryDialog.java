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
package ccc.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.binding.DataBinding;
import ccc.services.api.LogEntrySummary;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class HistoryDialog
    extends
        TableDataDisplayDialog<LogEntrySummary> {

    /**
     * Constructor.
     *
     * @param title
     * @param data
     */
    public HistoryDialog(final String title,
                         final Collection<LogEntrySummary> data) {
        super(title, data);
        _dataStore.add(DataBinding.bindLogEntrySummary(_data));
    }


    /** {@inheritDoc} */
    @Override
    protected ColumnModel defineColumnModel() {

//        #     column = new ColumnConfig();
//        #     column.setId("available");
//        #     column.setHeader("Available");
//        #     column.setWidth(95);
//        #     column.setEditor(new CellEditor(dateField));
//        #     column.setDateTimeFormat(DateTimeFormat.getMediumDateFormat());
//        #     configs.add(column);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig actionColumn = new ColumnConfig();
        actionColumn.setId("action");
        actionColumn.setHeader("Action");
        actionColumn.setWidth(100);
        configs.add(actionColumn);

        final ColumnConfig userColumn = new ColumnConfig();
        userColumn.setId("actor");
        userColumn.setHeader("User");
        configs.add(userColumn);

//        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());

//        final TableColumn timeColumn =
//            new TableColumn("happenedOn", "Time", PERCENT_30);
//        timeColumn.setRenderer(new CellRenderer<Component>(){
//
//            public String render(final Component item,
//                                 final String property,
//                                 final Object value) {
//
//                final Date happenedOn =
//                    new Date(((Long) value).longValue());
//                return happenedOn.toString(); // TODO: I18n
//            }});
//        columns.add(timeColumn);

        final ColumnConfig summaryColumn = new ColumnConfig();
        summaryColumn.setId("summary");
        summaryColumn.setHeader("Summary");
        configs.add(summaryColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }
}
