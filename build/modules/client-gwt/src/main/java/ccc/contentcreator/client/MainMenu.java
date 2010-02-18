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
package ccc.contentcreator.client;

import java.util.Collection;

import ccc.contentcreator.actions.ChooseTemplateAction;
import ccc.contentcreator.actions.GetRootsAction;
import ccc.contentcreator.actions.LockAction;
import ccc.contentcreator.actions.LogoutAction;
import ccc.contentcreator.actions.OpenAboutAction;
import ccc.contentcreator.actions.OpenCreateUserAction;
import ccc.contentcreator.actions.OpenEditCacheAction;
import ccc.contentcreator.actions.OpenHelpAction;
import ccc.contentcreator.actions.OpenUpdateCurrentUserAction;
import ccc.contentcreator.actions.OpenUpdateFolderAction;
import ccc.contentcreator.actions.OpenUpdateMetadataAction;
import ccc.contentcreator.actions.OpenUpdateResourceRolesAction;
import ccc.contentcreator.actions.PublishAction;
import ccc.contentcreator.actions.UnlockAction;
import ccc.contentcreator.actions.UnpublishAction;
import ccc.contentcreator.actions.ViewHistoryAction;
import ccc.contentcreator.actions.remote.ListGroups;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.Action;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.i18n.UIConstants;
import ccc.contentcreator.presenters.CreateGroupPresenter;
import ccc.contentcreator.views.gxt.GroupViewImpl;
import ccc.rest.dto.GroupDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;

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

    private final IGlobals _globals = new IGlobalsImpl();
    private final UIConstants _constants = _globals.uiConstants();
    private final UserDto _user;

    /**
     * Constructor.
     *
     * @param user UserSummary of the currently logged in user.
     */
    public MainMenu(final UserDto user) {
        _user = user;
        if (_user.hasPermission(IGlobals.ADMINISTRATOR)) {
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

        if (_user.hasPermission(IGlobals.ADMINISTRATOR)
                || _user.hasPermission(IGlobals.SITE_BUILDER)) {
            createContentRootMenu(CONTENT, _constants.contentRoot());
        }

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
                    || _user.hasPermission(IGlobals.ADMINISTRATOR)) {

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
                    new ListGroups() {
                        @Override
                        protected void execute(final Collection<GroupDto> g) {
                            new OpenUpdateResourceRolesAction(ssm, g)
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
                    GLOBALS.alert(sb.toString());
                }

            });
    }

    private SingleSelectionModel createSsm(final ResourceSummary root) {

        final SingleSelectionModel ssm =
            new SingleSelectionModel() {
                private final ResourceSummaryModelData _md =
                    new ResourceSummaryModelData(root);

                    public void create(final ResourceSummaryModelData model,
                                       final ResourceSummaryModelData parent) {
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
