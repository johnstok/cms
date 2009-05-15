/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.UserSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.EditUserDialog;
import ccc.contentcreator.dialogs.EditUserPwDialog;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserTable extends TablePanel {
    /** _constants : UIConstants. */
    private final UIConstants _constants = Globals.uiConstants();

    private final ListStore<UserSummaryModelData> _detailsStore =
        new ListStore<UserSummaryModelData>();

    private final RadioGroup _radioGroup = new RadioGroup("searchField");
    private final ToolBar _toolBar = new ToolBar();
    private final TextToolItem _searchButton;
    private final AdapterToolItem _ti;

    private final Radio _usernameRadio = new Radio();
    private final Radio _emailRadio = new Radio();

    private TreeItem _lastSelected = null;

    /**
     * Constructor.
     */
    UserTable() {

        setId("UserDetails");
        setHeading(_constants.userDetails());
        setLayout(new FitLayout());

        final TextField<String> searchString = new TextField<String>();
        searchString.setToolTip(_constants.searchToolTip());
        searchString.setId("searchString");
        _ti = new AdapterToolItem(searchString);

        _searchButton = new TextToolItem(_constants.search());
        _searchButton.setId("searchButton");

        _searchButton.addListener(Events.Select, new Listener<ComponentEvent>(){
            public void handleEvent(final ComponentEvent be) {
                if (searchString.getValue() == null) {
                    return;
                }
                _detailsStore.removeAll();
                if (_radioGroup.getValue() == _usernameRadio) {
                    qs.listUsersWithUsername(
                        searchString.getValue().replace('*', '%'),
                        new ErrorReportingCallback<Collection<UserSummary>>() {
                            public void onSuccess(final Collection<UserSummary> result) {
                                if (result != null && result.size() > 0) {
                                    _detailsStore.add(DataBinding.bindUserSummary(result));
                                }
                            }
                        });
                } else if (_radioGroup.getValue() == _emailRadio) {
                    qs.listUsersWithEmail(
                        searchString.getValue().replace('*', '%'),
                        new ErrorReportingCallback<Collection<UserSummary>>() {
                            public void onSuccess(final Collection<UserSummary> result) {
                                if (result != null && result.size() > 0) {
                                    _detailsStore.add(DataBinding.bindUserSummary(result));
                                }
                            }
                        });
                }
            }
        }
        );
        createToolBar();
        setTopComponent(_toolBar);

        final Menu contextMenu = new Menu();
        contextMenu.setId("userContextMenu");
        final ContextActionGridPlugin gp = new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new UserContextRenderer());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        final Grid<UserSummaryModelData> grid =
            new Grid<UserSummaryModelData>(_detailsStore, cm);
        grid.setLoadMask(true);
        grid.setId("UserGrid");

        contextMenu.add(createEditUserMenu(grid));
        contextMenu.add(createEditPwMenu(grid));

        grid.setContextMenu(contextMenu);
        grid.addPlugin(gp);
        add(grid);
    }

    private MenuItem createEditUserMenu(final Grid<UserSummaryModelData> grid) {
        final MenuItem editUser = new MenuItem(_constants.editUser());
        editUser.setId("editUserMenu");
        editUser.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final UserSummaryModelData userDTO =
                        grid.getSelectionModel().getSelectedItem();
                    qs.userDelta(
                        userDTO.getId(),
                        new AsyncCallback<UserDelta>(){
                            public void onFailure(final Throwable arg0) {
                                Globals.unexpectedError(arg0);
                            }
                            public void onSuccess(final UserDelta delta) {
                                new EditUserDialog(
                                    userDTO.getId(),delta, UserTable.this)
                                .show();
                            }
                        }
                    );
                }
            }
        );
        return editUser;
    }

    private MenuItem createEditPwMenu(final Grid<UserSummaryModelData> grid) {
        final MenuItem editUserPw = new MenuItem(_constants.editUserPw());
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

    private void createToolBar() {
        _usernameRadio.setName("Username");
        _usernameRadio.setBoxLabel(_constants.username());
        _usernameRadio.setValue(Boolean.TRUE);
        _usernameRadio.setId("usernameRadio");

        _emailRadio.setName("Email");
        _emailRadio.setBoxLabel(_constants.email());
        _emailRadio.setId("emailRadio");

        _radioGroup.setFieldLabel(_constants.searchField());
        _radioGroup.add(_usernameRadio);
        _radioGroup.add(_emailRadio);
        _toolBar.add(new AdapterToolItem(_radioGroup));
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_ti);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(_searchButton);
        _toolBar.setId("toolbar");
    }

    private List<ColumnConfig> createColumnConfigs(final ContextActionGridPlugin gp) {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(gp);

        final ColumnConfig usernameColumn = new ColumnConfig();
        usernameColumn.setId(UserSummaryModelData.Property.USERNAME.name());
        usernameColumn.setHeader(_constants.username());
        usernameColumn.setWidth(400);
        configs.add(usernameColumn);

        final ColumnConfig emailColumn = new ColumnConfig();
        emailColumn.setId(UserSummaryModelData.Property.EMAIL.name());
        emailColumn.setHeader(_constants.email());
        emailColumn.setWidth(400);
        configs.add(emailColumn);

        return configs;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param selectedItem The selected TreeItem.
     */
    public void displayUsersFor(final TreeItem selectedItem) {
        _lastSelected = selectedItem;
        _detailsStore.removeAll();


        if ("Search".equals(selectedItem.getText())) { // FIXME: I18n.
            _toolBar.show();
        } else {
            _toolBar.hide();
        }

        if ("All".equals(selectedItem.getText())) { // FIXME: I18n.
            qs.listUsers(
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if ("Content creator".equals(selectedItem.getText())){ // FIXME: I18n.
            qs.listUsersWithRole(
                "CONTENT_CREATOR",
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if ("Site Builder".equals(selectedItem.getText())) { // FIXME: I18n.
            qs.listUsersWithRole(
                "SITE_BUILDER",
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if("Administrator".equals(selectedItem.getText())) { // FIXME: I18n.
            qs.listUsersWithRole(
                "ADMINISTRATOR",
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        }
    }

    /**
     * Refresh user list unless the last list was created through a search.
     */
    public void refreshUsers() {
        if (_lastSelected != null
                && !"Search".equals(_lastSelected.getText())) {
            displayUsersFor(_lastSelected);
        }
    }
}
