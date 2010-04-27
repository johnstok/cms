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
import java.util.UUID;

import ccc.api.core.Group;
import ccc.api.core.User;
import ccc.api.types.ACL;
import ccc.api.types.ACL.Entry;
import ccc.client.gwt.binding.UserSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.GetUserAction;
import ccc.client.gwt.remoting.UpdateResourceAclAction;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.http.client.Response;


/**
 * Dialog for updating the access control associated with a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclDialog
    extends
        AbstractEditDialog {

    private CheckBoxSelectionModel<ModelData> _groupSM;
    private final ListStore<ModelData> _groupStore = new ListStore<ModelData>();
    private Grid<ModelData> _groupGrid;
    private final ContentPanel _groupGridPanel = new ContentPanel();

    private CheckBoxSelectionModel<UserSummaryModelData> _userSM;
    private ListStore<UserSummaryModelData> _userStore =
        new ListStore<UserSummaryModelData>();
    private Grid<UserSummaryModelData> _userGrid;
    private final ContentPanel _userGridPanel = new ContentPanel();

    private final UUID _resourceId;

    private final ACL _acl;
    private Collection<Group> _allGroups;

    private static final int DIALOG_WIDTH = 440;
    private static final int GRID_WIDTH = 200;
    private static final int GRIDPANEL_WIDTH = 210;
    private static final int DIALOG_HEIGHT = 400;
    private static final int GROUPS_HEIGHT = 300;
    private static final int DEFAULT_MARGIN = 5;

    /**
     * Constructor.
     *
     * @param resourceId The resource whose ACL will be updated.
     * @param acl The access control list for the resource.
     * @param allGroups A list of all groups available in the system.
     */
    public UpdateResourceAclDialog(final UUID resourceId,
                                   final ACL acl,
                                   final Collection<Group> allGroups) {
        super(new GlobalsImpl().uiConstants().updateRoles(),
              new GlobalsImpl());
        _resourceId = resourceId;
        _acl = acl;
        _allGroups = allGroups;
        setLayout(new RowLayout(Orientation.HORIZONTAL));
        setLayout(new BorderLayout());

        setWidth(DIALOG_WIDTH);
        setHeight(DIALOG_HEIGHT);

        createGroupPanel(allGroups);
        createUsersPanel();
    }

    private void createUsersPanel() {

        final List<UserSummaryModelData> uData =
            new ArrayList<UserSummaryModelData>();
        for (final Entry e : _acl.getUsers()) {
            new GetUserAction(e._principal) { // FIXME: remove these calls.
                @Override
                protected void execute(final User user) {
                    final UserSummaryModelData d =
                        new UserSummaryModelData(user);
                    uData.add(d);
                    _userStore.add(d);
                }
            }.execute();

        }
        _userGridPanel.setBodyBorder(false);
        _userGridPanel.setBorders(true);
        _userGridPanel.setHeading(getConstants().users());
        _userGridPanel.setHeaderVisible(true);
        _userGridPanel.setLayout(new FitLayout());
        _userGridPanel.setWidth(GRIDPANEL_WIDTH);

        final ColumnModel ucm = defineUserColumnModel();

        _userStore.add(uData);
        _userGrid = new Grid<UserSummaryModelData>(_userStore, ucm);
        _userGrid.setAutoExpandColumn("USERNAME");
        _userGrid.setSelectionModel(_userSM);
        _userGrid.addPlugin(_userSM);
        _userGrid.setBorders(false);
        _userGrid.setHeight(GROUPS_HEIGHT);
        _userGrid.setWidth(GRID_WIDTH);
        _userGrid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

        _userGridPanel.add(_userGrid);

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(DEFAULT_MARGIN));

        addUserToolbar();
        add(_userGridPanel, centerData);
    }

    private void createGroupPanel(final Collection<Group> allGroups) {

        final List<ModelData> gData = new ArrayList<ModelData>();
        for (final Group g : allGroups) {
            final BaseModelData d = new BaseModelData();
            for (final Entry e : _acl.getGroups()){
                if (e._principal.equals(g.getId())) {
                    d.set("name", g.getName());
                    d.set("id", g.getId());
                    gData.add(d);
                }
            }
        }

        _groupGridPanel.setBodyBorder(false);
        _groupGridPanel.setBorders(true);
        _groupGridPanel.setHeading(getConstants().groups());
        _groupGridPanel.setHeaderVisible(true);
        _groupGridPanel.setLayout(new FitLayout());
        _groupGridPanel.setWidth(GRIDPANEL_WIDTH);

        final ColumnModel cm = defineGroupColumnModel();

        _groupStore.add(gData);
        _groupGrid = new Grid<ModelData>(_groupStore, cm);
        _groupGrid.setAutoExpandColumn("name");
        _groupGrid.setSelectionModel(_groupSM);
        _groupGrid.addPlugin(_groupSM);
        _groupGrid.setBorders(false);
        _groupGrid.setHeight(GROUPS_HEIGHT);
        _groupGrid.setWidth(GRID_WIDTH);
        _groupGrid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

        _groupGridPanel.add(_groupGrid);

        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST);
        westData.setMargins(new Margins(DEFAULT_MARGIN));
        addGroupToolbar();
        add(_groupGridPanel, westData);
    }


    private ColumnModel defineGroupColumnModel() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _groupSM = new CheckBoxSelectionModel<ModelData>();
        configs.add(_groupSM.getColumn());

        final ColumnConfig keyColumn =
            new ColumnConfig("name", constants().name(), 100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setReadOnly(true);
        configs.add(keyColumn);

        return new ColumnModel(configs);
    }

    private ColumnModel defineUserColumnModel() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _userSM = new CheckBoxSelectionModel<UserSummaryModelData>();
        configs.add(_userSM.getColumn());

        final ColumnConfig keyColumn =
            new ColumnConfig("USERNAME", constants().name(), 100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setReadOnly(true);
        configs.add(keyColumn);

        return new ColumnModel(configs);
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {

                final List<Entry> newGroups = new ArrayList<Entry>();
                for (final ModelData selected : _groupStore.getModels()) {
                    final Entry e = new Entry();
                    e._canRead = true;
                    e._canWrite = true;
                    e._principal = selected.<UUID>get("id");
                    newGroups.add(e);
                }
                final List<Entry> newUsers = new ArrayList<Entry>();
                for (final UserSummaryModelData um : _userStore.getModels()) {
                    final Entry e = new Entry();
                    e._canRead = true;
                    e._canWrite = true;
                    e._principal = um.getId();
                    newUsers.add(e);
                }

                final ACL acl =
                    new ACL()
                        .setGroups(newGroups)
                        .setUsers(newUsers);

                new UpdateResourceAclAction(_resourceId, acl) {
                    /** {@inheritDoc} */
                    @Override
                    protected void onNoContent(final Response response) {
                        hide();
                    }
                }.execute();
            }
        };
    }

    private void addGroupToolbar() {

        final ToolBar toolBar = new ToolBar();
        toolBar.add(new SeparatorToolItem());

        final Button add = new Button(constants().add());
        add.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                new GroupACLSelector(
                    _groupStore,
                    _allGroups,
                    getConstants()).show();
            }
        });
        toolBar.add(add);
        toolBar.add(new SeparatorToolItem());

        final Button remove = new Button(constants().remove());
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                for (final ModelData item
                        : _groupGrid.getSelectionModel().getSelectedItems()) {
                    _groupStore.remove(item);
                }
            }
        });
        toolBar.add(remove);

        toolBar.add(new SeparatorToolItem());
        _groupGridPanel.setBottomComponent(toolBar);
    }

    private void addUserToolbar() {

        final ToolBar toolBar = new ToolBar();
        toolBar.add(new SeparatorToolItem());

        final Button add = new Button(constants().add());
        add.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                new UserACLSelector(_userStore, getConstants()).show();
            }
        });
        toolBar.add(add);
        toolBar.add(new SeparatorToolItem());

        final Button remove = new Button(constants().remove());
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                for (final UserSummaryModelData item
                    : _userGrid.getSelectionModel().getSelectedItems()) {
                    _userStore.remove(item);
                }
            }
        });
        toolBar.add(remove);

        toolBar.add(new SeparatorToolItem());
        _userGridPanel.setBottomComponent(toolBar);
    }
}
