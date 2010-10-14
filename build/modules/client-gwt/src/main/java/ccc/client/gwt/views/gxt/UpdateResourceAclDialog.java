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
import ccc.client.actions.GetUserAction;
import ccc.client.actions.UpdateResourceAclAction;
import ccc.client.core.DefaultCallback;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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
    private EditorGrid<BaseModelData> _groupGrid;
    private final ContentPanel _groupGridPanel = new ContentPanel();

    private CheckBoxSelectionModel<BeanModel> _userSM;
    private final ListStore<BeanModel> _userStore =
        new ListStore<BeanModel>();
    private EditorGrid<BeanModel> _userGrid;
    private final ContentPanel _userGridPanel = new ContentPanel();

    private final ResourceSummary _resource;

    private final ACL _acl;
    private final Collection<Group> _allGroups;

    private static final int DIALOG_WIDTH = 440;
    private static final int GRID_WIDTH = 260;
    private static final int GRIDPANEL_WIDTH = 270;
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
            InternalServices.GLOBALS);
        _resource = resource;
        _acl = acl;
        _allGroups = allGroups;
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
            // FIXME: remove these calls.
            new GetUserAction(e.user())
            .execute(
                new DefaultCallback<User>(I18n.UI_CONSTANTS.updateRoles()) {
                    @Override public void onSuccess(final User user) {
                        final BeanModel d =
                            DataBinding.bindUserSummary(user);
                        uData.add(d);
                        final String rw = createRW(e);
                        d.set("rwid", rw);
                        _userStore.add(d);
                    }}
            );

        }
        _userGridPanel.setBodyBorder(false);
        _userGridPanel.setBorders(true);
        _userGridPanel.setHeading(getConstants().users());
        _userGridPanel.setHeaderVisible(true);
        _userGridPanel.setLayout(new FitLayout());
        _userGridPanel.setWidth(GRIDPANEL_WIDTH);

        final ColumnModel ucm = defineUserColumnModel();

        _userStore.add(uData);
        _userGrid = new EditorGrid<BeanModel>(_userStore, ucm);
        _userGrid.setAutoExpandColumn(User.Properties.USERNAME);
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
                    d.set(Group.Properties.NAME, g.getName());
                    d.set(Group.Properties.ID, g.getId());
                    final String rw = createRW(e);
                    d.set("rwid", rw);
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
        _groupGrid = new EditorGrid<BaseModelData>(_groupStore, cm);
        _groupGrid.setAutoExpandColumn(Group.Properties.NAME);
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

    private String createRW(final Entry e) {
        final StringBuilder sb = new StringBuilder();
        if (e.isReadable()) {
            sb.append("r");
        }
        if (e.isWriteable()) {
            if (sb.length()>0) {
                sb.append("+");
            }
            sb.append("w");
        }
        return sb.toString();
    }


    private ColumnModel defineGroupColumnModel() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _groupSM = new CheckBoxSelectionModel<BaseModelData>();
        configs.add(_groupSM.getColumn());

        final ColumnConfig rwColumn = createRWcombo();
        configs.add(rwColumn);

        final ColumnConfig keyColumn =
            new ColumnConfig(
                Group.Properties.NAME,
                constants().name(),
                100);
        configs.add(keyColumn);


        return new ColumnModel(configs);
    }

    private ColumnConfig createRWcombo() {

        final SimpleComboBox<String> combo = new SimpleComboBox<String>();
        combo.setForceSelection(true);
        combo.setTriggerAction(TriggerAction.ALL);
        combo.add("-");
        combo.add("r");
        combo.add("w");
        combo.add("r+w");

        final CellEditor editor = new CellEditor(combo) {
            @Override
            public Object preProcessValue(final Object value) {
              if (value == null) {
                return value;
              }
              return combo.findModel(value.toString());
            }

            @Override
            public Object postProcessValue(final Object value) {
              if (value == null) {
                return value;
              }
              return ((ModelData) value).get("value");
            }
          };

        final ColumnConfig rwColumn =
            new ColumnConfig(
                "rwid",
                I18n.UI_CONSTANTS.mode(),
                50);
        rwColumn.setEditor(editor);
        return rwColumn;
    }

    private ColumnModel defineUserColumnModel() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _userSM = new CheckBoxSelectionModel<BeanModel>();
        configs.add(_userSM.getColumn());

        final ColumnConfig rwColumn = createRWcombo();
        configs.add(rwColumn);

        final ColumnConfig keyColumn =
            new ColumnConfig(
                User.Properties.USERNAME,
                constants().name(),
                100);
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
                    assignRW(e, selected);
                    e.setPrincipal(selected.<UUID>get("id"));
                    newGroups.add(e);
                }
                final List<Entry> newUsers = new ArrayList<Entry>();
                for (final BeanModel um : _userStore.getModels()) {
                    final Entry e = new Entry();
                    assignRW(e, um);
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
                    protected void onSuccess(final Void response) {
                        hide();
                    }
                }.execute();
            }

            private void assignRW(final Entry e, final BaseModelData selected) {
                final String rw = selected.get("rwid");
                if (rw.indexOf('r')> -1) {
                    e.setReadable(true);
                }
                if (rw.indexOf('w')> -1) {
                    e.setWriteable(true);
                }
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
