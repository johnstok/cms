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

import ccc.api.core.ACL;
import ccc.api.core.Group;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.client.core.I18n;
import ccc.client.core.Response;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.GetUserAction;
import ccc.client.gwt.remoting.UpdateResourceAclAction;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
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


/**
 * Dialog for updating the access control associated with a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclDialog
    extends
        AbstractEditDialog {

    private CheckBoxSelectionModel<BaseModelData> _groupSM;
    private final ListStore<BaseModelData> _groupStore =
        new ListStore<BaseModelData>();
    private Grid<BaseModelData> _groupGrid;
    private final ContentPanel _groupGridPanel = new ContentPanel();

    private CheckBoxSelectionModel<BeanModel> _userSM;
    private ListStore<BeanModel> _userStore =
        new ListStore<BeanModel>();
    private Grid<BeanModel> _userGrid;
    private final ContentPanel _userGridPanel = new ContentPanel();

    private final ResourceSummary _resource;

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
     * @param resource The resource whose ACL will be updated.
     * @param acl The access control list for the resource.
     * @param allGroups A list of all groups available in the system.
     */
    public UpdateResourceAclDialog(final ResourceSummary resource,
                                   final ACL acl,
                                   final Collection<Group> allGroups) {
        super(I18n.UI_CONSTANTS.updateRoles(),
              new GlobalsImpl());
        _resource = resource;
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

        final List<BeanModel> uData =
            new ArrayList<BeanModel>();
        for (final Entry e : _acl.getUsers()) {
            new GetUserAction(e.user()) { // FIXME: remove these calls.
                @Override
                protected void execute(final User user) {
                    final BeanModel d =
                        DataBinding.bindUserSummary(user);
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
        _userGrid = new Grid<BeanModel>(_userStore, ucm);
        _userGrid.setAutoExpandColumn(DataBinding.UserBeanModel.USERNAME);
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

        final List<BaseModelData> gData = new ArrayList<BaseModelData>();
        for (final Group g : allGroups) {
            final BaseModelData d = new BaseModelData();
            for (final Entry e : _acl.getGroups()){
                if (e.getPrincipal().equals(g.getId())) {
                    d.set(DataBinding.GroupBeanModel.NAME, g.getName());
                    d.set(DataBinding.GroupBeanModel.ID, g.getId());
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
        _groupGrid = new Grid<BaseModelData>(_groupStore, cm);
        _groupGrid.setAutoExpandColumn(DataBinding.GroupBeanModel.NAME);
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

        _groupSM = new CheckBoxSelectionModel<BaseModelData>();
        configs.add(_groupSM.getColumn());

        final ColumnConfig keyColumn =
            new ColumnConfig(
                DataBinding.GroupBeanModel.NAME,
                constants().name(),
                100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setReadOnly(true);
        configs.add(keyColumn);

        return new ColumnModel(configs);
    }

    private ColumnModel defineUserColumnModel() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _userSM = new CheckBoxSelectionModel<BeanModel>();
        configs.add(_userSM.getColumn());

        final ColumnConfig keyColumn =
            new ColumnConfig(
                DataBinding.UserBeanModel.USERNAME,
                constants().name(),
                100);
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
                for (final BaseModelData selected : _groupStore.getModels()) {
                    final Entry e = new Entry();
                    e.setReadable(true);
                    e.setWriteable(true);
                    e.setPrincipal(selected.<UUID>get("id"));
                    newGroups.add(e);
                }
                final List<Entry> newUsers = new ArrayList<Entry>();
                for (final BeanModel um : _userStore.getModels()) {
                    final Entry e = new Entry();
                    e.setReadable(true);
                    e.setWriteable(true);
                    e.setPrincipal(um.<User>getBean().getId());
                    newUsers.add(e);
                }

                final ACL acl =
                    new ACL()
                        .setGroups(newGroups)
                        .setUsers(newUsers);

                new UpdateResourceAclAction(_resource, acl) {
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
                for (final BaseModelData item
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
                for (final BeanModel item
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
