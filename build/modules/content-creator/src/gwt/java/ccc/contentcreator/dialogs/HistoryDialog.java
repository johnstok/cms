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
import java.util.Date;
import java.util.List;

import ccc.contentcreator.binding.DataBinding;
import ccc.services.api.LogEntrySummary;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.table.CellRenderer;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;


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
    protected TableColumnModel defineColumnModel() {

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        columns.add(new TableColumn("action", "Action", PERCENT_10));
        columns.add(new TableColumn("actor", "User", PERCENT_10));
        final TableColumn timeColumn =
            new TableColumn("happenedOn", "Time", PERCENT_30);
        timeColumn.setRenderer(new CellRenderer<Component>(){

            public String render(final Component item,
                                 final String property,
                                 final Object value) {

                final Date happenedOn =
                    new Date(((Long) value).longValue());
                return happenedOn.toString(); // TODO: I18n
            }});
        columns.add(timeColumn);
        columns.add(new TableColumn("summary", "Summary", PERCENT_50));

        final TableColumnModel cm = new TableColumnModel(columns);
        return cm;
    }
}
