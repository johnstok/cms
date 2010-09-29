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
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.widgets.HistoryToolBar;
import ccc.client.views.HistoryView;

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
        AbstractTableDialog<Revision, BeanModel>
    implements
        HistoryView {

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
            InternalServices.GLOBALS,
              data,
              false);

        _ssm = ssm;
        _toolBar = new HistoryToolBar(this);
        _toolBar.disable();
        setTopComponent(_toolBar);
        getDataStore().add(DataBinding.bindLogEntrySummary(getData()));
        getDataStore().sort(Revision.HAPPENED_ON, SortDir.DESC);
        getGrid().setAutoExpandColumn(Revision.COMMENT);
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
                Revision.USERNAME,
                getUiConstants().user(),
                100);
        configs.add(userColumn);

        final ColumnConfig timeColumn =
            new ColumnConfig(
                Revision.HAPPENED_ON,
                getUiConstants().time(),
                150);
        timeColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(timeColumn);

        final ColumnConfig majorEditColumn =
            new ColumnConfig(
                Revision.IS_MAJOR_EDIT,
                getUiConstants().majorEdit(),
                70);
        configs.add(majorEditColumn);

        final ColumnConfig commentColumn = new ColumnConfig();
        commentColumn.setId(Revision.COMMENT);
        commentColumn.setHeader(getUiConstants().comment());
        configs.add(commentColumn);

        final ColumnModel cm = new ColumnModel(configs);
        return cm;
    }


    /** {@inheritDoc} */
    @Override
    public Revision selectedItem() {
        final Revision selected =
            getGrid().getSelectionModel().getSelectedItem().getBean();
        return selected;
    }


    /** {@inheritDoc} */
    @Override
    public void workingCopyCreated() {
        final ResourceSummary selectedInMainWindow =
            _ssm.tableSelection();
        selectedInMainWindow.setHasWorkingCopy(true);
        _ssm.update(selectedInMainWindow);
    }


    /** {@inheritDoc} */
    @Override
    public boolean hasLock() {
        return null!=_ssm.tableSelection().getLockedBy();
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary getResource() {
        return _ssm.tableSelection();
    }
}
