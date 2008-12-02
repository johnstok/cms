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

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.EditUserDialog;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
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


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserTable extends TablePanel {

    private final ListStore<ModelData> _detailsStore =
        new ListStore<ModelData>();

    private final RadioGroup _radioGroup = new RadioGroup("searchField");
    private final ToolBar _toolBar = new ToolBar();

    private final Radio _usernameRadio = new Radio();
    private final Radio _emailRadio = new Radio();

    private TreeItem _lastSelected = null;

    /**
     * Constructor.
     */
    UserTable() {
        setId("UserDetails");
        setHeading("User Details");
        setLayout(new FitLayout());

        final TextField<String> searchString = new TextField<String>();
        searchString.setToolTip("Use * for wild card searches, for example "
        + "Joh* finds John");
        searchString.setId("searchString");
        final AdapterToolItem ti = new AdapterToolItem(searchString);

        final TextToolItem searchButton = new TextToolItem("Search");
        searchButton.setId("searchButton");

        searchButton.addListener(Events.Select, new Listener<ComponentEvent>(){
            public void handleEvent(final ComponentEvent be) {
                _detailsStore.removeAll();

                if (_radioGroup.getValue() == _usernameRadio) {
                    qs.listUsersWithUsername(
                        searchString.getValue().replace('*', '%'),
                        new ErrorReportingCallback<Collection<UserSummary>>() {
                            public void onSuccess(final Collection<UserSummary> result) {
                                _detailsStore.add(DataBinding.bindUserSummary(result));
                            }
                        });
                } else if (_radioGroup.getValue() == _emailRadio) {
                    qs.listUsersWithEmail(
                        searchString.getValue().replace('*', '%'),
                        new ErrorReportingCallback<Collection<UserSummary>>() {
                            public void onSuccess(final Collection<UserSummary> result) {
                                _detailsStore.add(DataBinding.bindUserSummary(result));
                            }
                        });
                }
            }
        }
        );

        _usernameRadio.setName("Username");
        _usernameRadio.setBoxLabel("Username");
        _usernameRadio.setValue(true);
        _usernameRadio.setId("usernameRadio");

        _emailRadio.setName("Email");
        _emailRadio.setBoxLabel("Email");
        _emailRadio.setId("emailRadio");

        _radioGroup.setFieldLabel("Search Field");
        _radioGroup.add(_usernameRadio);
        _radioGroup.add(_emailRadio);
        _toolBar.add(new AdapterToolItem(_radioGroup));
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(ti);
        _toolBar.add(new SeparatorToolItem());
        _toolBar.add(searchButton);
        _toolBar.setId("toolbar");

        setTopComponent(_toolBar);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig usernameColumn = new ColumnConfig();
        usernameColumn.setId("username");
        usernameColumn.setHeader("Username");
        usernameColumn.setWidth(400);
        configs.add(usernameColumn);

        final ColumnConfig emailColumn = new ColumnConfig();
        emailColumn.setId("email");
        emailColumn.setHeader("e-mail");
        emailColumn.setWidth(400);
        configs.add(emailColumn);

        final ColumnModel cm = new ColumnModel(configs);

        final Grid<ModelData> grid = new Grid<ModelData>(_detailsStore, cm);
        grid.setLoadMask(true);
        grid.setId("UserGrid");

        final Menu contextMenu = new Menu();
        contextMenu.setId("userContextMenu");
        final MenuItem editUser = new MenuItem("Edit user");
        editUser.setId("editUserMenu");
        editUser.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                final ModelData userDTO =
                    grid.getSelectionModel().getSelectedItem();
                new EditUserDialog(null, UserTable.this).show(); // FIXME: look up delta
            }

        });
        contextMenu.add(editUser);

        grid.setContextMenu(contextMenu);
        add(grid);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param selectedItem The selected TreeItem.
     */
    public void displayUsersFor(final TreeItem selectedItem) {
        _lastSelected = selectedItem;
        _detailsStore.removeAll();


        if ("Search".equals(selectedItem.getText())) {
            _toolBar.show();
        } else {
            _toolBar.hide();
        }

        if ("All".equals(selectedItem.getText())) {
            qs.listUsers(
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if ("Content creator".equals(selectedItem.getText())){
            qs.listUsersWithRole(
                "CONTENT_CREATOR",
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if ("Site Builder".equals(selectedItem.getText())) {
            qs.listUsersWithRole(
                "SITE_BUILDER",
                new ErrorReportingCallback<Collection<UserSummary>>() {
                    public void onSuccess(final Collection<UserSummary> result) {
                        _detailsStore.add(DataBinding.bindUserSummary(result));
                    }
                });
        } else if("Administrator".equals(selectedItem.getText())) {
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
     *
     */
    public void refreshUsers() {
        if (_lastSelected != null
                && !"Search".equals(_lastSelected.getText())) {
            displayUsersFor(_lastSelected);
        }
    }
}
