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
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * Read only view of a resoure's history.
 *
 * @author Civic Computing Ltd.
 */
public class HistoryDialog
    extends
        TableDataDisplayDialog<LogEntrySummary> {

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of
     *  {@link LogEntrySummary}.
     */
    public HistoryDialog(final Collection<LogEntrySummary> data) {
        super("Resource History", data); // TODO: I18n

        _dataStore.add(DataBinding.bindLogEntrySummary(_data));
        _grid.setAutoExpandColumn("summary");
    }


    /** {@inheritDoc} */
    @Override
    protected ColumnModel defineColumnModel() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig actionColumn =
            new ColumnConfig("action", "Action", 70);
        configs.add(actionColumn);

        final ColumnConfig userColumn =
            new ColumnConfig("actor", "User", 100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig("happenedOn", "Time", 150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig summaryColumn = new ColumnConfig();
        summaryColumn.setId("summary");
        summaryColumn.setHeader("Summary");
        configs.add(summaryColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }
}
