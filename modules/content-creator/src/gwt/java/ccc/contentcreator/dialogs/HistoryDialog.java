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

import ccc.api.CommandType;
import ccc.api.LogEntrySummary;
import ccc.api.ResourceType;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.HistoryToolBar;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * Read only view of a resoure's history.
 *
 * @author Civic Computing Ltd.
 */
public class HistoryDialog
    extends
        AbstractTableDialog<LogEntrySummary, LogEntrySummaryModelData> {

    private final ToolBar _toolBar;
    private final SingleSelectionModel _ssm;
    private final ResourceType _resourceType;

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of
     *  {@link LogEntrySummary}.
     * @param ssm The selection model.
     */
    public HistoryDialog(final Collection<LogEntrySummary> data,
                         final SingleSelectionModel ssm) {
        super(Globals.uiConstants().resourceHistory(), data, false);

        _ssm = ssm;
        _resourceType = _ssm.tableSelection().getType();
        _toolBar = new HistoryToolBar(this);
        _toolBar.disable();
        setTopComponent(_toolBar);
        _dataStore.add(DataBinding.bindLogEntrySummary(_data));
        _grid.setAutoExpandColumn(LogEntrySummaryModelData.EXPAND_PROPERTY);
        _grid.addListener(
            Events.RowClick,
            new Listener<GridEvent>(){
                public void handleEvent(final GridEvent be) {
                    final LogEntrySummaryModelData md =
                        _grid.getSelectionModel().getSelectedItem();
                    if (null==md) {
                        _toolBar.disable();
                    } else {
                        final CommandType action = md.getAction();
                        if (CommandType.PAGE_CREATE==action
                            || CommandType.PAGE_UPDATE==action
                            || CommandType.FILE_CREATE==action
                            || CommandType.FILE_UPDATE==action) {
                            _toolBar.enable();
                        } else {
                            _toolBar.disable();
                        }
                    }
                }
            }
        );
    }


    /** {@inheritDoc} */
    @Override
    protected ColumnModel defineColumnModel() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig actionColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.ACTION.name(),
                _constants.action(),
                70);
        configs.add(actionColumn);

        final ColumnConfig userColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.ACTOR.name(),
                _constants.user(),
                100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.HAPPENED_ON.name(),
                _constants.time(),
                150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.IS_MAJOR_EDIT.name(),
                _constants.majorEdit(),
                70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId(
            LogEntrySummaryModelData.Property.COMMENT.name());
        commentColumn.setHeader(_constants.comment());
        configs.add(commentColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public LogEntrySummaryModelData selectedItem() {
        final LogEntrySummaryModelData selected = _grid.getSelectionModel().getSelectedItem();
        return selected;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param selectedId
     */
    public void workingCopyCreated() {
        final ResourceSummaryModelData selectedInMainWindow = _ssm.tableSelection();
        selectedInMainWindow.setWorkingCopy(true);
        _ssm.update(selectedInMainWindow);
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean hasLock() {
        return null!=_ssm.tableSelection().getLocked();
    }
}
