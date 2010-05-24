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

import java.util.Collection;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.types.Permission;
import ccc.client.gwt.actions.ChooseTemplateAction;
import ccc.client.gwt.actions.OpenAboutAction;
import ccc.client.gwt.actions.OpenCreateUserAction;
import ccc.client.gwt.actions.OpenHelpAction;
import ccc.client.gwt.actions.OpenUpdateCurrentUserAction;
import ccc.client.gwt.actions.OpenUpdateFolderAction;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.Action;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.presenters.CreateGroupPresenter;
import ccc.client.gwt.remoting.GetRootsAction;
import ccc.client.gwt.remoting.ListGroups;
import ccc.client.gwt.remoting.LockAction;
import ccc.client.gwt.remoting.LogoutAction;
import ccc.client.gwt.remoting.OpenEditCacheAction;
import ccc.client.gwt.remoting.OpenUpdateMetadataAction;
import ccc.client.gwt.remoting.OpenUpdateResourceAclAction;
import ccc.client.gwt.remoting.PublishAction;
import ccc.client.gwt.remoting.UnlockAction;
import ccc.client.gwt.remoting.UnpublishAction;
import ccc.client.gwt.remoting.ViewHistoryAction;
import ccc.client.gwt.views.gxt.GroupViewImpl;

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

    private final Globals _globals = new GlobalsImpl();
    private final UIConstants _constants = GlobalsImpl.uiConstants();
    private final User _user;

    /**
     * Constructor.
     *
     * @param user UserSummary of the currently logged in user.
     */
    public MainMenu(final User user) {
        _user = user;
        if (_user.hasPermission(Permission.USER_CREATE)) {
            addMenu(
                "users-menu",
                _constants.users(),
                createMenuItem(
                    "create-user-menu-item",
                    _constants.createUser(),
                    new OpenCreateUserAction()),
                createMenuItem(
                    "create-group-menu-item",
                    _constants.createGroup(),
                    new Action(){
                        @Override public void execute() {
                            new CreateGroupPresenter(
                                new GroupViewImpl(_globals));
                        }
                    }));
        }

//        if (_user.hasPermission(Globals.ADMINISTRATOR)
//                || _user.hasPermission(Globals.SITE_BUILDER)) {
            createContentRootMenu(CONTENT, _constants.contentRoot());
//        }

        addMenu(
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

        addMenu(
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
                    new GetRootsAction() {
                        // TODO: Do we really have to go to the server for this?
                        @Override protected void onSuccess(
                                      final Collection<ResourceSummary> roots) {
                            for (final ResourceSummary root : roots) {
                                if (rootName.equals(root.getName())) {
                                    addRootMenuItems(root, rootMenu);
                                }
                            }
                        }
                    }.execute();
                }
            }
        );

        rootItem.setMenu(rootMenu);
        add(rootItem);
    }


    private void addRootMenuItems(final ResourceSummary root,
                                  final Menu rootMenu) {
        final SingleSelectionModel ssm = createSsm(root);
        final String name = root.getName();

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
                    rootMenu.add(createMenuItem(
                        "publish-root-"+name,
                        _constants.publish(),
                        new PublishAction(ssm)));
                } else {
                    rootMenu.add(createMenuItem(
                        "unpublish-root-"+name,
                        _constants.unpublish(),
                        new UnpublishAction(ssm)));
                }
                if (CONTENT.equals(root.getName())) {
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
                    new ListGroups(1, Globals.MAX_FETCH, "name", "ASC") {
                        @Override
                        protected void execute(final PagedCollection<Group> groups) {
                            new OpenUpdateResourceAclAction(
                                ssm, groups.getElements())
                            .execute();
                        }}));
                rootMenu.add(createMenuItem(
                    "updateMetadata-root-"+name,
                    _constants.updateMetadata(),
                    new OpenUpdateMetadataAction(ssm)));
                rootMenu.add(createMenuItem(
                    "cacheDuration-root-"+name,
                    _constants.cacheDuration(),
                    new OpenEditCacheAction(ssm)));
            }
        }
    }

    private MenuItem rootDetailMenuItem(final ResourceSummary root,
                                        final String name) {

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
                    _globals.alert(sb.toString());
                }

            });
    }

    private SingleSelectionModel createSsm(final ResourceSummary root) {

        final SingleSelectionModel ssm =
            new SingleSelectionModel() {
                private final ResourceSummaryModelData _md =
                    new ResourceSummaryModelData(root);

                    public void create(final ResourceSummaryModelData model) {
                        /* No-op */
                    }
                    public void delete(final ResourceSummaryModelData model) {
                        /* No-op */
                    }
                    public void move(final ResourceSummaryModelData model,
                                     final ResourceSummaryModelData newParent,
                                     final ResourceSummaryModelData oldParent) {
                        /* No-op */
                    }
                    public ResourceSummaryModelData tableSelection() {
                        return _md;
                    }
                    public ResourceSummaryModelData treeSelection() {
                        throw new UnsupportedOperationException(
                            "Method not implemented.");
                    }
                    public void update(final ResourceSummaryModelData model) {
                        /* No-op */
                    }

                };
        return ssm;
    }
}
