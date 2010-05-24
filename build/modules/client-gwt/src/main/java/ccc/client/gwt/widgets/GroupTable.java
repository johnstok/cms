/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.GroupModelData;
import ccc.client.gwt.events.GroupUpdated;
import ccc.client.gwt.events.GroupUpdated.GroupUpdatedHandler;
import ccc.client.gwt.presenters.UpdateGroupPresenter;
import ccc.client.gwt.remoting.ListGroups;
import ccc.client.gwt.views.gxt.GroupViewImpl;
import ccc.plugins.s11n.JsonKeys;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays users using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class GroupTable
    extends
        TablePanel
    implements
        GroupUpdatedHandler {

    private ListStore<GroupModelData> _detailsStore =
        new ListStore<GroupModelData>();

    private final Grid<GroupModelData> _grid;
    private final PagingToolBar _pagerBar;

    private static final int COLUMN_WIDTH = 400;


    /**
     * Constructor.
     */
    GroupTable() {
        ContentCreator.EVENT_BUS.addHandler(GroupUpdated.TYPE, this);

        setHeading(UI_CONSTANTS.groups());
        setLayout(new FitLayout());

        final Menu contextMenu = new Menu();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new ContextMenuRenderer<GroupModelData>());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<GroupModelData>(_detailsStore, cm);

        contextMenu.add(createUpdateGroupItem(_grid));

        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }


    private MenuItem createUpdateGroupItem(final Grid<GroupModelData> grid) {
        final MenuItem updateGroup = new MenuItem(UI_CONSTANTS.updateGroup());
        updateGroup.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final GroupModelData groupModel =
                        grid.getSelectionModel().getSelectedItem();

                    new UpdateGroupPresenter(
                        new GroupViewImpl(GLOBALS),
                        groupModel.getDelegate());
                }
            }
        );
        return updateGroup;
    }


    private List<ColumnConfig> createColumnConfigs(
        final ContextActionGridPlugin gp) {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(gp);

        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId(JsonKeys.NAME);
        nameColumn.setHeader(UI_CONSTANTS.name());
        nameColumn.setWidth(COLUMN_WIDTH);
        configs.add(nameColumn);

        return configs;
    }


    /**
     *  Displays all groups.
     */
    public void displayGroups() {
        _detailsStore.removeAll();

        final DataProxy<PagingLoadResult<GroupModelData>> proxy =
            new RpcProxy<PagingLoadResult<GroupModelData>>() {

                @Override
                protected void load(final Object loadConfig,
                                    final AsyncCallback<PagingLoadResult<GroupModelData>> callback) {

                    if (null==loadConfig
                        || !(loadConfig instanceof BasePagingLoadConfig)) {
                        final PagingLoadResult<GroupModelData> plr =
                           new BasePagingLoadResult<GroupModelData>(null);
                        callback.onSuccess(plr);

                    } else {
                        final BasePagingLoadConfig config =
                            (BasePagingLoadConfig) loadConfig;

                        final int page =
                            config.getOffset()/ config.getLimit()+1;
                        final SortOrder order = (
                            config.getSortDir() == Style.SortDir.ASC
                            ? SortOrder.ASC : SortOrder.DESC);

                        new ListGroups(page, config.getLimit(),
                            config.getSortField(),
                            order) {

                            /** {@inheritDoc} */
                            @Override
                            protected void onFailure(final Throwable t) {
                                callback.onFailure(t);
                            }

                            @Override
                            protected void execute(
                                       final PagedCollection<Group> groups) {

                                final List<GroupModelData> results =
                                    DataBinding.bindGroupSummary(
                                        groups.getElements());

                                final PagingLoadResult<GroupModelData> plr =
                                    new BasePagingLoadResult<GroupModelData>(
                                       results,
                                       config.getOffset(),
                                       (int) groups.getTotalCount());
                                callback.onSuccess(plr);
                            }
                        }.execute();
                    }
                }
            };

        updatePager(proxy);
    }

    private void updatePager(final DataProxy proxy){
        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<GroupModelData>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }


    /** {@inheritDoc} */
    @Override
    public void onUpdate(final GroupUpdated event) {
        final GroupModelData gMD =
            _detailsStore.findModel(JsonKeys.ID, event.getGroup().getId());
        if (null!=gMD) {
            gMD.setDelegate(event.getGroup());
            _detailsStore.update(gMD);
        }
    }
}
