/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.api.dto.RevisionDto;
import ccc.api.types.CommandType;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.core.SingleSelectionModel;
import ccc.contentcreator.widgets.HistoryToolBar;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.Events;
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
        AbstractTableDialog<RevisionDto, LogEntrySummaryModelData> {

    private final ToolBar _toolBar;
    private final SingleSelectionModel _ssm;
    private final UUID _id;

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of
     *  {@link RevisionDto}.
     * @param resourceId The UUID.
     * @param ssm The selection model.
     */
    public HistoryDialog(final Collection<RevisionDto> data,
                         final UUID resourceId,
                         final SingleSelectionModel ssm) {
        super(new GlobalsImpl().uiConstants().resourceHistory(),
              new GlobalsImpl(),
              data,
              false);

        _id = resourceId;
        _ssm = ssm;
        _toolBar = new HistoryToolBar(this);
        _toolBar.disable();
        setTopComponent(_toolBar);
        getDataStore().add(DataBinding.bindLogEntrySummary(getData()));
        getDataStore().sort(
            LogEntrySummaryModelData.Property.HAPPENED_ON.name(), SortDir.DESC);
        getGrid().setAutoExpandColumn(LogEntrySummaryModelData.EXPAND_PROPERTY);
        getGrid().addListener(
            Events.RowClick,
            new Listener<GridEvent<?>>(){
                public void handleEvent(final GridEvent<?> be) {
                    final LogEntrySummaryModelData md =
                        getGrid().getSelectionModel().getSelectedItem();
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

        final ColumnConfig userColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.USERNAME.name(),
                getUiConstants().user(),
                100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.HAPPENED_ON.name(),
                getUiConstants().time(),
                150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig(
                LogEntrySummaryModelData.Property.IS_MAJOR_EDIT.name(),
                getUiConstants().majorEdit(),
                70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId(
            LogEntrySummaryModelData.Property.COMMENT.name());
        commentColumn.setHeader(getUiConstants().comment());
        configs.add(commentColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }


    /**
     * Returns selected {@link LogEntrySummaryModelData}.
     *
     * @return The selected item.
     */
    public LogEntrySummaryModelData selectedItem() {
        final LogEntrySummaryModelData selected =
            getGrid().getSelectionModel().getSelectedItem();
        return selected;
    }


    /**
     * Updates selection model for a working copy.
     *
     */
    public void workingCopyCreated() {
        final ResourceSummaryModelData selectedInMainWindow =
            _ssm.tableSelection();
        selectedInMainWindow.setWorkingCopy(true);
        _ssm.update(selectedInMainWindow);
    }


    /**
     * Return boolean value of the selected resource's lock status.
     *
     * @return True is selection is locked.
     */
    public boolean hasLock() {
        return null!=_ssm.tableSelection().getLocked();
    }


    /**
     * Accessor.
     *
     * @return The id for the resource.
     */
    public UUID getResourceId() {
        return _id;
    }
}
