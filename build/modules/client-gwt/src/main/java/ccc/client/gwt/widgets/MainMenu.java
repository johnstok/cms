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

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.types.Permission;
import ccc.api.types.ResourceName;
import ccc.api.types.SortOrder;
import ccc.client.actions.ChooseTemplateAction;
import ccc.client.actions.ListGroups;
import ccc.client.actions.LockAction;
import ccc.client.actions.LogoutAction;
import ccc.client.actions.OpenAboutAction;
import ccc.client.actions.OpenCreateUserAction;
import ccc.client.actions.OpenEditCacheAction;
import ccc.client.actions.OpenHelpAction;
import ccc.client.actions.OpenUpdateCurrentUserAction;
import ccc.client.actions.OpenUpdateFolderAction;
import ccc.client.actions.OpenUpdateMetadataAction;
import ccc.client.actions.OpenUpdateResourceAclAction;
import ccc.client.actions.PublishAction;
import ccc.client.actions.UnlockAction;
import ccc.client.actions.UnpublishAction;
import ccc.client.actions.ViewHistoryAction;
import ccc.client.core.Action;
import ccc.client.core.DefaultCallback;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.GroupViewImpl;
import ccc.client.i18n.UIConstants;
import ccc.client.presenters.CreateGroupPresenter;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;


/**
 * Design for the main menu toolbar.
 *
 * @author Civic Computing Ltd.
 */
public class MainMenu
    extends
        AbstractToolBar {

    private static final String CONTENT = "content";

    private final Globals _globals = InternalServices.globals;
    private final UIConstants _constants = I18n.uiConstants;
    private final User _user;

    /**
     * Constructor.
     *
     * @param user UserSummary of the currently logged in user.
     */
    public MainMenu(final User user) {
        _user = user;

        createUserMenu();
        createContentRootMenu(CONTENT, _constants.contentRoot());

        addMenu(null,
            "tools-menu",
            _constants.tools(),
            createMenuItem(
                "update-account-details",
                _constants.yourDetails(),
                new OpenUpdateCurrentUserAction()),
            createMenuItem(
                "logout-menu-item",
                _constants.logout(),
                new LogoutAction())
        );

        addMenu(null,
            "help-menu",
            _constants.help(),
            createMenuItem(
                "about-dialog",
                _constants.about(),
                new OpenAboutAction()),
            createMenuItem(
                "open-manual",
                _constants.manual(),
                new OpenHelpAction())
        );
    }

    private void createUserMenu() {

        final Menu itemMenu = new Menu();
        if (_user.hasPermission(Permission.USER_CREATE)
            ||_user.hasPermission(Permission.GROUP_CREATE)) {
            final Button item = new Button(_constants.users());
            item.setId("users-menu");
            item.setMenu(itemMenu);
            add(item);
        }
        if (_user.hasPermission(Permission.USER_CREATE)) {
            itemMenu.add(createMenuItem(
                    "create-user-menu-item",
                    _constants.createUser(),
                    new OpenCreateUserAction()));
        }
        if (_user.hasPermission(Permission.GROUP_CREATE)) {
            itemMenu.add(createMenuItem(
                    "create-group-menu-item",
                    _constants.createGroup(),
                    new Action(){
                        @Override public void execute() {
                            new CreateGroupPresenter(
                                new GroupViewImpl(_globals));
                        }
                    }));
        }
    }

    private void createContentRootMenu(final String rootName,
                                       final String label) {

        final Button rootItem = new Button(label);
        rootItem.setId(rootName+"Root-menu");

        final Menu rootMenu = new Menu();
        rootMenu.setEnableScrolling(false);
        rootMenu.setAutoWidth(true);
        rootMenu.addListener(
            Events.BeforeShow,
            new Listener<MenuEvent>() {
                public void handleEvent(final MenuEvent be) {
                    rootMenu.removeAll();

                    ResourceSummary root = null;
                    for (final ResourceSummary rr : InternalServices.roots.getElements()) {
                        if (rr.getName().toString().equals("content")) {
                            root = rr;
                        }
                    }
                    if (rootName.equals(root.getName().toString())) {
                        addRootMenuItems(root, rootMenu);
                    }
                }
            }
        );

        rootItem.setMenu(rootMenu);
        add(rootItem);
    }


    private void addRootMenuItems(final ResourceSummary root,
                                  final Menu rootMenu) {
        final SingleSelectionModel ssm = createSsm(root);
        final ResourceName name = root.getName();

        rootMenu.add(rootDetailMenuItem(root, name));

        rootMenu.add(createMenuItem(
            "viewHistory-root-"+name,
            _constants.viewHistory(),
            new ViewHistoryAction(ssm)));

        if (root.getLockedBy() == null
            || root.getLockedBy().toString().equals("")) {
            rootMenu.add(createMenuItem(
                "lock-root-"+name,
                _constants.lock(),
                new LockAction(ssm)));
        } else {
            if (root.getLockedBy().equals(_user.getUsername())
                    || _user.hasPermission(Permission.RESOURCE_UNLOCK)) {
                rootMenu.add(createMenuItem(
                    "unlock-root-"+name,
                    _constants.unlock(),
                    new UnlockAction(ssm)));
            }
            if (root.getLockedBy().equals(_user.getUsername())) {
                if (root.getPublishedBy() == null
                        || root.getPublishedBy().toString().equals("")) {
                    if (_user.hasPermission(Permission.RESOURCE_PUBLISH)) {
                        rootMenu.add(createMenuItem(
                            "publish-root-"+name,
                            _constants.publish(),
                            new PublishAction(ssm)));
                    }
                } else {
                    if (_user.hasPermission(Permission.RESOURCE_UNPUBLISH)) {
                        rootMenu.add(createMenuItem(
                            "unpublish-root-"+name,
                            _constants.unpublish(),
                            new UnpublishAction(ssm)));
                    }
                }
                if (CONTENT.equals(root.getName().toString())
                     && _user.hasPermission(Permission.RESOURCE_UPDATE)) {
                    rootMenu.add(createMenuItem(
                        "chooseTemplate-root-"+name,
                        _constants.chooseTemplate(),
                        new ChooseTemplateAction(ssm)));
                    rootMenu.add(createMenuItem(
                        "editFolder-root-"+name,
                        _constants.edit(),
                        new OpenUpdateFolderAction(ssm)));
                }
                rootMenu.add(createMenuItem(
                    "updateRoles-root-"+name,
                    _constants.updateRoles(),
                    new ListGroups(1,
                        Globals.MAX_FETCH,
                        "name",
                        SortOrder.ASC) {

                        @Override
                        public void execute() {
                            execute(new DefaultCallback<PagedCollection<Group>>(_constants.updateRoles()) {

                                @Override
                                public void onSuccess(final PagedCollection<Group> groups) {
                                    new OpenUpdateResourceAclAction(
                                        ssm, groups.getElements())
                                    .execute();
                                }});
                        }}));
                if (_user.hasPermission(Permission.RESOURCE_UPDATE))  {
                    rootMenu.add(createMenuItem(
                        "updateMetadata-root-"+name,
                        _constants.updateMetadata(),
                        new OpenUpdateMetadataAction(ssm)));
                }
                if (_user.hasPermission(Permission.RESOURCE_CACHE_UPDATE))  {
                    rootMenu.add(createMenuItem(
                        "cacheDuration-root-"+name,
                        _constants.cacheDuration(),
                        new OpenEditCacheAction(ssm)));
                }
            }
        }
    }

    private MenuItem rootDetailMenuItem(final ResourceSummary root,
                                        final ResourceName name) {

        return createMenuItem(
            "details-root-"+name,
            _constants.details(),
            new Action(){

                public void execute() {
                    final StringBuilder sb = new StringBuilder();
                    if (root.getLockedBy() != null) {
                     sb.append(_constants.lockedBy()
                         +" "+root.getLockedBy()+"\n");
                    }
                    if (root.getPublishedBy() != null) {
                        sb.append(_constants.publishedBy()
                            +" "+root.getPublishedBy()+"\n");
                    }
                    InternalServices.window.alert(sb.toString());
                }

            });
    }

    private SingleSelectionModel createSsm(final ResourceSummary root) {

        final SingleSelectionModel ssm =
            new SingleSelectionModel() {
                private final ResourceSummary _md = root;

                    public void create(final ResourceSummary model) {
                        /* No-op */
                    }
                    public void move(final ResourceSummary model,
                                     final ResourceSummary newParent,
                                     final ResourceSummary oldParent) {
                        /* No-op */
                    }
                    public ResourceSummary tableSelection() {
                        return _md;
                    }
                    public ResourceSummary treeSelection() {
                        throw new UnsupportedOperationException(
                            "Method not implemented.");
                    }
                    public void update(final ResourceSummary model) {
                        /* No-op */
                    }
                    public void create(final Resource model) {
                        /* No-op */
                    }
                    public ResourceSummary root() {
                        return null;
                    }

                };
        return ssm;
    }
}
