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
import ccc.api.types.CommandType;
import ccc.api.types.SortOrder;
import ccc.client.actions.ListGroups;
import ccc.client.core.Callback;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.views.gxt.GroupViewImpl;
import ccc.client.presenters.UpdateGroupPresenter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
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
        EventHandler<CommandType> {

    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;

    private static final int COLUMN_WIDTH = 400;


    /**
     * Constructor.
     */
    GroupTable() {
        InternalServices.REMOTING_BUS.registerHandler(this);

        setHeading(UI_CONSTANTS.groups());
        setLayout(new FitLayout());

        final Menu contextMenu = new Menu();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new ContextMenuRenderer());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<BeanModel>(_detailsStore, cm);

        contextMenu.add(createUpdateGroupItem(_grid));

        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }


    private MenuItem createUpdateGroupItem(final Grid<BeanModel> grid) {
        final MenuItem updateGroup = new MenuItem(UI_CONSTANTS.updateGroup());
        updateGroup.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final BeanModel groupModel =
                        grid.getSelectionModel().getSelectedItem();

                    new UpdateGroupPresenter(
                        new GroupViewImpl(GLOBALS),
                        groupModel.<Group>getBean());
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
        nameColumn.setId(Group.NAME);
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

        final DataProxy<PagingLoadResult<BeanModel>> proxy =
            new RpcProxy<PagingLoadResult<BeanModel>>() {

                @Override
                protected void load(final Object loadConfig,
                                    final AsyncCallback<
                                        PagingLoadResult<BeanModel>> callback) {

                    if (null==loadConfig
                        || !(loadConfig instanceof BasePagingLoadConfig)) {
                        final PagingLoadResult<BeanModel> plr =
                           new BasePagingLoadResult<BeanModel>(null);
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
                            order)
                        .execute(
                            new Callback<PagedCollection<Group>>() {

                                @Override
                                public void onSuccess(final PagedCollection<Group> groups) {
                                    final List<BeanModel> results =
                                        DataBinding.bindGroupSummary(
                                            groups.getElements());

                                    final PagingLoadResult<BeanModel> plr =
                                        new BasePagingLoadResult<BeanModel>(
                                           results,
                                           config.getOffset(),
                                           (int) groups.getTotalCount());
                                    callback.onSuccess(plr);
                                }

                                @Override
                                public void onFailure(final Throwable caught) {
                                    callback.onFailure(caught);
                                }
                            });
                    }
                }
            };

        updatePager(proxy);
    }


    private void updatePager(
                         final DataProxy<PagingLoadResult<BeanModel>> proxy){
        final PagingLoader<PagingLoadResult<BeanModel>> loader =
            new BasePagingLoader<PagingLoadResult<BeanModel>>(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case GROUP_UPDATE:
                updateGroup(event.<Group>getProperty("group"));
                break;

            default:
                break;
        }
    }


    private void updateGroup(final Group group) {
        final BeanModel gMD =
            _detailsStore.findModel(
                Group.ID, group.getId());
        if (null!=gMD) {
            final BeanModel bm = DataBinding.bindGroupSummary(group);
            gMD.setProperties(bm.getProperties());
            _detailsStore.update(gMD);
        }
    }
}
