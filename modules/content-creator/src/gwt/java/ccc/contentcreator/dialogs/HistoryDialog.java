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
import ccc.contentcreator.client.Globals;
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
        AbstractTableDialog<LogEntrySummary> {

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of
     *  {@link LogEntrySummary}.
     */
    public HistoryDialog(final Collection<LogEntrySummary> data) {
        super(Globals.uiConstants().resourceHistory(), data);

        _dataStore.add(DataBinding.bindLogEntrySummary(_data));
        _grid.setAutoExpandColumn("comment");
    }


    /** {@inheritDoc} */
    @Override
    protected ColumnModel defineColumnModel() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig actionColumn =
            new ColumnConfig("action", _constants.action(), 70);
        configs.add(actionColumn);

        final ColumnConfig userColumn =
            new ColumnConfig("actor", _constants.user(), 100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig("happenedOn", _constants.time(), 150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig("isMajorEdit", _constants.majorEdit(), 70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId("comment");
        commentColumn.setHeader(_constants.comment());
        configs.add(commentColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }
}
