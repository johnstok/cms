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
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.HistoryToolBar;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.services.api.Action;
import ccc.services.api.LogEntrySummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
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
    private final String _resourceType;

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of
     *  {@link LogEntrySummary}.
     * @param ssm
     */
    public HistoryDialog(final Collection<LogEntrySummary> data,
                         final SingleSelectionModel ssm) {
        super(Globals.uiConstants().resourceHistory(), data, false);

        _ssm = ssm;
        _resourceType = _ssm.tableSelection().<String>get(DataBinding.TYPE);
        _toolBar = new HistoryToolBar(this);
        _toolBar.disable();
        setTopComponent(_toolBar);
        _dataStore.add(DataBinding.bindLogEntrySummary(_data));
        _grid.setAutoExpandColumn(LogEntrySummaryModelData.Property.COMMENT.name());
        _grid.addListener(
            Events.RowClick,
            new Listener<GridEvent>(){
                public void handleEvent(final GridEvent be) {
                    final LogEntrySummaryModelData md =
                        _grid.getSelectionModel().getSelectedItem();
                    if (null==md) {
                        _toolBar.disable();
                    } else {
                        final Action action = md.getAction();
                        if ((Action.CREATE==action || Action.UPDATE==action)
                            && (_resourceType.equals("PAGE") || _resourceType.equals("FILE"))) {
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
            new ColumnConfig(DataBinding.ACTION, _constants.action(), 70);
        configs.add(actionColumn);

        final ColumnConfig userColumn =
            new ColumnConfig(DataBinding.ACTOR, _constants.user(), 100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig(DataBinding.HAPPENED_ON, _constants.time(), 150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig(DataBinding.IS_MAJOR_EDIT, _constants.majorEdit(), 70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId(DataBinding.COMMENT);
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
    public ModelData selectedItem() {
        final ModelData selected = _grid.getSelectionModel().getSelectedItem();
        return selected;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param selectedId
     */
    public void workingCopyCreated() {
        final ModelData selectedInMainWindow = _ssm.tableSelection();
        selectedInMainWindow.set(DataBinding.WORKING_COPY, Boolean.TRUE);
        _ssm.update(selectedInMainWindow);
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean hasLock() {
        return null!=_ssm.tableSelection().get(DataBinding.LOCKED);
    }
}
