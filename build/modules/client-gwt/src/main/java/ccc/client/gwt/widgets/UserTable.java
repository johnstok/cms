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
import ccc.api.core.User;
import ccc.api.core.UserCriteria;
import ccc.api.types.Permission;
import ccc.api.types.SortOrder;
import ccc.client.core.Globals;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.GetUserAction;
import ccc.client.gwt.remoting.ListGroups;
import ccc.client.gwt.remoting.ListUsersAction;
import ccc.client.gwt.remoting.OpenEditUserDialogAction;
import ccc.client.gwt.views.gxt.EditUserPwDialog;
import ccc.client.gwt.views.gxt.UserMetadataDialog;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays users using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class UserTable extends TablePanel {

    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

    private final RadioGroup _radioGroup = new RadioGroup("searchField");
    private final ToolBar _toolBar = new ToolBar();
    private final Button _searchButton;

    private final Radio _usernameRadio = new Radio();
    private final Radio _emailRadio = new Radio();

    private ModelData _lastSelected = null;

    private final TextField<String> _searchString;

    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;

    private static final int COLUMN_WIDTH = 400;

    /**
     * Constructor.
     */
    UserTable() {

        setId("UserDetails");
        setHeading(UI_CONSTANTS.userDetails());
        setLayout(new FitLayout());

        _searchString = new TextField<String>();
        _searchString.setToolTip(UI_CONSTANTS.searchToolTip());
        _searchString.setId("searchString");

        _searchButton = new Button(UI_CONSTANTS.search());
        _searchButton.setId("searchButton");

        _searchButton.addListener(Events.Select, new SearchListener());
        createToolBar();
        setTopComponent(_toolBar);

        final Menu contextMenu = new Menu();
        contextMenu.setId("userContextMenu");
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new ContextMenuRenderer());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<BeanModel>(_detailsStore, cm);
        _grid.setId("UserGrid");

        if (GLOBALS.currentUser().hasPermission(Permission.USER_UPDATE)) {
            contextMenu.add(createEditUserMenu(_grid));
            contextMenu.add(createEditPwMenu(_grid));
            contextMenu.add(createEditMetadataMenu(_grid));
        }

        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }

    private MenuItem createEditUserMenu(final Grid<BeanModel> grid) {
        final MenuItem editUser = new MenuItem(UI_CONSTANTS.editUser());
        editUser.setId("editUserMenu");
        editUser.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final BeanModel userDTO =
                        grid.getSelectionModel().getSelectedItem();

                    new ListGroups(1,
                                   Globals.MAX_FETCH,
                                   "name",
                                   SortOrder.ASC) {
                        @Override
                        protected void execute(
                                       final PagedCollection<Group> groups) {
                            new OpenEditUserDialogAction(
                                userDTO.<User>getBean(),
                                UserTable.this,
                                groups.getElements())
                            .execute();
                        }}.execute();

                }
            }
        );
        return editUser;
    }

    private MenuItem createEditPwMenu(final Grid<BeanModel> grid) {
        final MenuItem editUserPw = new MenuItem(UI_CONSTANTS.editUserPw());
        editUserPw.setId("editUserPwMenu");
        editUserPw.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final BeanModel userDTO =
                    grid.getSelectionModel().getSelectedItem();
                new EditUserPwDialog(userDTO.<User>getBean()).show();
            }
        });
        return editUserPw;
    }

    private MenuItem createEditMetadataMenu(
                         final Grid<BeanModel> grid) {
        final MenuItem editUserMeta =
            new MenuItem(UI_CONSTANTS.editUserMetadata());
        editUserMeta.setId("editUserMetadataMenu");
        editUserMeta.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final BeanModel modeldata =
                    grid.getSelectionModel().getSelectedItem();
                new GetUserAction(modeldata.<User>getBean().self()) {
                    @Override
                    protected void execute(final User user) {
                        new UserMetadataDialog(user).show();
                    }
                }.execute();
            }
        });
        return editUserMeta;
    }

    private void createToolBar() {
        _usernameRadio.setName("Username");
        _usernameRadio.setBoxLabel(UI_CONSTANTS.username());
        _usernameRadio.setValue(Boolean.TRUE);
        _usernameRadio.setId("usernameRadio");

        _emailRadio.setName("Email");
        _emailRadio.setBoxLabel(UI_CONSTANTS.email());
        _emailRadio.setId("emailRadio");

        _radioGroup.setFieldLabel(UI_CONSTANTS.searchField());
        _radioGroup.add(_usernameRadio);
        _radioGroup.add(_emailRadio);
        _toolBar.add(_radioGroup);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_searchString);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_searchButton);
        _toolBar.setId("toolbar");
    }

    private List<ColumnConfig> createColumnConfigs(
        final ContextActionGridPlugin gp) {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(gp);

        final ColumnConfig usernameColumn = new ColumnConfig();
        usernameColumn.setId(DataBinding.UserBeanModel.USERNAME);
        usernameColumn.setHeader(UI_CONSTANTS.username());
        usernameColumn.setWidth(COLUMN_WIDTH);
        configs.add(usernameColumn);

        final ColumnConfig emailColumn = new ColumnConfig();
        emailColumn.setId(DataBinding.UserBeanModel.EMAIL);
        emailColumn.setHeader(UI_CONSTANTS.email());
        emailColumn.setWidth(COLUMN_WIDTH);
        configs.add(emailColumn);

        return configs;
    }

    /**
     *  Displays selection of users based on selected item.
     *
     * @param selectedItem The selected TreeItem.
     */
    public void displayUsersFor(final ModelData selectedItem) {
        _lastSelected = selectedItem;
        _detailsStore.removeAll();
        _searchString.setValue(null);
        if (UserTree.USERS.equals(selectedItem.get("id"))) {
            updatePager(null);
        } else {
            final UserCriteria uc = new UserCriteria();
            uc.setGroups((String) selectedItem.get("id"));
            updatePager(uc);
        }
    }

    /**
     * Refresh user list unless the last list was created through a search.
     */
    public void refreshUsers() {
        if (_lastSelected != null){
            displayUsersFor(_lastSelected);
        }
    }

    /**
     * Listener for user search.
     *
     * @author Civic Computing Ltd.
     */
    private final class SearchListener implements Listener<ComponentEvent> {

        public void handleEvent(final ComponentEvent be) {
            if (_searchString.getValue() == null) {
                return;
            }
            _detailsStore.removeAll();
            if (_radioGroup.getValue() == _usernameRadio) {
                final UserCriteria uc = new UserCriteria();
                if (!UserTree.USERS.equals(_lastSelected.get("id"))) {
                    uc.setGroups((String) _lastSelected.get("id"));
                }
                uc.setUsername(_searchString.getValue().replace('*', '%'));
                updatePager(uc);
            } else if (_radioGroup.getValue() == _emailRadio) {
                final UserCriteria uc = new UserCriteria();
                uc.setEmail(_searchString.getValue().replace('*', '%'));
                updatePager(uc);
            }
        }
    }

    private void updatePager(final UserCriteria uc){

        final RpcProxy<PagingLoadResult<BeanModel>> proxy =
            new RpcProxy<PagingLoadResult<BeanModel>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <BeanModel>> callback) {
                if (null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<BeanModel> plr =
                       new BasePagingLoadResult<BeanModel>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/config.getLimit()+1;
                    final SortOrder order = (
                        config.getSortDir() == Style.SortDir.ASC
                        ? SortOrder.ASC : SortOrder.DESC);

                    new ListUsersAction(
                        uc,
                        page,
                        config.getLimit(),
                        config.getSortField(),
                        order) {

                            @Override
                            protected void execute(
                                           final PagedCollection<User> users) {
                                final List<BeanModel> results =
                                    DataBinding
                                        .bindUserSummary(users.getElements());

                            final PagingLoadResult<BeanModel> plr =
                                new BasePagingLoadResult<BeanModel>(
                                    results, config.getOffset(),
                                    (int) users.getTotalCount());
                                callback.onSuccess(plr);
                            }
                    }.execute();
                }
            }
        };


        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }
}
