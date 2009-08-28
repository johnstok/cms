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

import java.util.Collection;

import ccc.api.ResourceSummary;
import ccc.api.UserSummary;
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
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;

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
    private static final String ASSETS = "assets";

    private final IGlobals _globals = new IGlobalsImpl();
    private final UIConstants _constants = _globals.uiConstants();
    private final UserSummary _user;

    /**
     * Constructor.
     *
     * @param user UserSummary of the currently logged in user.
     */
    public MainMenu(final UserSummary user) {
        _user = user;
        if (_user.getRoles().contains(IGlobals.ADMINISTRATOR)) {
            addMenu(
                "users-menu",
                _constants.users(),
                createMenuItem(
                    "create-user-menu-item",
                    _constants.createUser(),
                    new OpenCreateUserAction())
            );
        }

        if (_user.getRoles().contains(IGlobals.ADMINISTRATOR)
                || _user.getRoles().contains(IGlobals.SITE_BUILDER)) {
            createContentRootMenu(CONTENT, _constants.contentRoot());
            createContentRootMenu(ASSETS, _constants.assetsRoot());
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
                    new GetRootsAction() { // TODO: Do we really have to go to the server for this?!
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
                    || _user.getRoles().contains(IGlobals.ADMINISTRATOR)) {

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
                    new OpenUpdateResourceRolesAction(ssm)));
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
