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
package ccc.client.gwt.views.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.types.ResourceType;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.widgets.HistoryToolBar;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
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
        AbstractTableDialog<Revision, BeanModel> {

    private final ToolBar _toolBar;
    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param data The history to display, as a collection of {@link Revision}s.
     * @param resourceType The type of the resource.
     * @param ssm The selection model.
     */
    public HistoryDialog(final Collection<Revision> data,
                         final ResourceType resourceType,
                         final SingleSelectionModel ssm) {
        super(I18n.UI_CONSTANTS.resourceHistory(),
              new GlobalsImpl(),
              data,
              false);

        _ssm = ssm;
        _toolBar = new HistoryToolBar(this);
        _toolBar.disable();
        setTopComponent(_toolBar);
        getDataStore().add(DataBinding.bindLogEntrySummary(getData()));
        getDataStore().sort(
            DataBinding.RevisionBeanModel.HAPPENED_ON, SortDir.DESC);
        getGrid().setAutoExpandColumn(DataBinding.RevisionBeanModel.COMMENT);
        getGrid().addListener(
            Events.RowClick,
            new Listener<GridEvent<?>>(){
                public void handleEvent(final GridEvent<?> be) {
                    final BeanModel md =
                        getGrid().getSelectionModel().getSelectedItem();
                    if (null==md) {
                        _toolBar.disable();
                    } else {
                        if (ResourceType.PAGE==resourceType
                            || ResourceType.TEMPLATE==resourceType
                            || ResourceType.FILE==resourceType) {
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
                DataBinding.RevisionBeanModel.USERNAME,
                getUiConstants().user(),
                100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig(
                DataBinding.RevisionBeanModel.HAPPENED_ON,
                getUiConstants().time(),
                150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig(
                DataBinding.RevisionBeanModel.IS_MAJOR_EDIT,
                getUiConstants().majorEdit(),
                70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId(
            DataBinding.RevisionBeanModel.COMMENT);
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
    public Revision selectedItem() {
        final Revision selected =
            getGrid().getSelectionModel().getSelectedItem().getBean();
        return selected;
    }


    /**
     * Updates selection model for a working copy.
     *
     */
    public void workingCopyCreated() {
        final ResourceSummary selectedInMainWindow =
            _ssm.tableSelection();
        selectedInMainWindow.setHasWorkingCopy(true);
        _ssm.update(selectedInMainWindow);
    }


    /**
     * Return boolean value of the selected resource's lock status.
     *
     * @return True is selection is locked.
     */
    public boolean hasLock() {
        return null!=_ssm.tableSelection().getLockedBy();
    }


    /**
     * Accessor.
     *
     * @return The id for the resource.
     */
    public ResourceSummary getResource() {
        return _ssm.tableSelection();
    }
}
