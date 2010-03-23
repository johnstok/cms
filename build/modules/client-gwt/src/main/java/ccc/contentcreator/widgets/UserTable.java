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
package ccc.contentcreator.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.UserSummaryModelData;
import ccc.contentcreator.remoting.GetUserAction;
import ccc.contentcreator.remoting.ListGroups;
import ccc.contentcreator.remoting.ListUsersAction;
import ccc.contentcreator.remoting.OpenEditUserDialogAction;
import ccc.contentcreator.views.gxt.EditUserPwDialog;
import ccc.contentcreator.views.gxt.UserMetadataDialog;
import ccc.rest.dto.GroupDto;
import ccc.rest.dto.UserCriteria;
import ccc.rest.dto.UserDto;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
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

    private ListStore<UserSummaryModelData> _detailsStore =
        new ListStore<UserSummaryModelData>();

    private final RadioGroup _radioGroup = new RadioGroup("searchField");
    private final ToolBar _toolBar = new ToolBar();
    private final Button _searchButton;

    private final Radio _usernameRadio = new Radio();
    private final Radio _emailRadio = new Radio();

    private ModelData _lastSelected = null;

    private final TextField<String> _searchString;

    private final Grid<UserSummaryModelData> _grid;
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
        gp.setRenderer(new UserContextRenderer());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<UserSummaryModelData>(_detailsStore, cm);
        _grid.setId("UserGrid");

        contextMenu.add(createEditUserMenu(_grid));
        contextMenu.add(createEditPwMenu(_grid));
        contextMenu.add(createEditMetadataMenu(_grid));

        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }

    private MenuItem createEditUserMenu(final Grid<UserSummaryModelData> grid) {
        final MenuItem editUser = new MenuItem(UI_CONSTANTS.editUser());
        editUser.setId("editUserMenu");
        editUser.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final UserSummaryModelData userDTO =
                        grid.getSelectionModel().getSelectedItem();

                    new ListGroups() {
                        @Override
                        protected void execute(final Collection<GroupDto> g) {
                            new OpenEditUserDialogAction(
                                userDTO.getId(), UserTable.this, g)
                            .execute();
                        }}.execute();

                }
            }
        );
        return editUser;
    }

    private MenuItem createEditPwMenu(final Grid<UserSummaryModelData> grid) {
        final MenuItem editUserPw = new MenuItem(UI_CONSTANTS.editUserPw());
        editUserPw.setId("editUserPwMenu");
        editUserPw.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final UserSummaryModelData userDTO =
                    grid.getSelectionModel().getSelectedItem();
                new EditUserPwDialog(userDTO).show();
            }
        });
        return editUserPw;
    }

    private MenuItem createEditMetadataMenu(
                         final Grid<UserSummaryModelData> grid) {
        final MenuItem editUserMeta =
            new MenuItem(UI_CONSTANTS.editUserMetadata());
        editUserMeta.setId("editUserMetadataMenu");
        editUserMeta.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final UserSummaryModelData modeldata =
                    grid.getSelectionModel().getSelectedItem();
                new GetUserAction(modeldata.getId()) {
                    @Override
                    protected void execute(final UserDto user) {
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
        _toolBar.disable();
    }

    private List<ColumnConfig> createColumnConfigs(
        final ContextActionGridPlugin gp) {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(gp);

        final ColumnConfig usernameColumn = new ColumnConfig();
        usernameColumn.setId(UserSummaryModelData.Property.USERNAME.name());
        usernameColumn.setHeader(UI_CONSTANTS.username());
        usernameColumn.setWidth(COLUMN_WIDTH);
        configs.add(usernameColumn);

        final ColumnConfig emailColumn = new ColumnConfig();
        emailColumn.setId(UserSummaryModelData.Property.EMAIL.name());
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

        if (UserTree.SEARCH.equals(selectedItem.get("id"))) {
            _toolBar.enable();
        } else {
            _toolBar.disable();
        }

        if (UserTree.ALL.equals(selectedItem.get("id"))) {
            updatePager(null);
        } else if (UserTree.CONTENT_CREATOR.equals(selectedItem.get("id"))){
            final UserCriteria uc =
                new UserCriteria(null, null, "CONTENT_CREATOR");
            updatePager(uc);
        } else if (UserTree.SITE_BUILDER.equals(selectedItem.get("id"))) {
            final UserCriteria uc =
                new UserCriteria(null, null, "SITE_BUILDER");
            updatePager(uc);
        } else if(UserTree.ADMINISTRATOR.equals(selectedItem.get("id"))) {
            final UserCriteria uc =
                new UserCriteria(null, null, "ADMINISTRATOR");
            updatePager(uc);
        } else {
           //FIXME updatePager(new ArrayList<UserDto>());
        }

    }

    /**
     * Refresh user list unless the last list was created through a search.
     */
    public void refreshUsers() {
        if (_lastSelected != null
                && !UserTree.SEARCH.equals(_lastSelected.get("id"))) {
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
                final UserCriteria uc = new UserCriteria(
                    _searchString.getValue().replace('*', '%'), null, null);
                updatePager(uc);
            } else if (_radioGroup.getValue() == _emailRadio) {
                final UserCriteria uc = new UserCriteria(null,
                    _searchString.getValue().replace('*', '%'), null);
                updatePager(uc);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updatePager(final UserCriteria uc){

        final RpcProxy<PagingLoadResult<UserSummaryModelData>> proxy =
            new RpcProxy<PagingLoadResult<UserSummaryModelData>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <UserSummaryModelData>> callback) {
                if (null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<UserSummaryModelData> plr =
                       new BasePagingLoadResult<UserSummaryModelData>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/ config.getLimit()+1;
                    final String order = (
                        config.getSortDir() == Style.SortDir.ASC
                        ? "ASC" : "DESC");

                    new ListUsersAction(
                        uc,
                        page,
                        config.getLimit(),
                        config.getSortField(),
                        order) {

                            @Override
                            protected void execute(final Collection<UserDto> users,
                                                   final int totalCount) {
                                final List<UserSummaryModelData> results =
                                    DataBinding.bindUserSummary(users);

                                final PagingLoadResult<UserSummaryModelData> plr =
                                    new BasePagingLoadResult<UserSummaryModelData>(results, config.getOffset(), totalCount);
                                callback.onSuccess(plr);
                            }
                    }.execute();
                }
            }
        };


        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<UserSummaryModelData>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }
}
