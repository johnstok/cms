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
import ccc.contentcreator.actions.CreateUserAction;
import ccc.contentcreator.actions.EditCacheAction;
import ccc.contentcreator.actions.LockAction;
import ccc.contentcreator.actions.LogoutAction;
import ccc.contentcreator.actions.OpenHelpAction;
import ccc.contentcreator.actions.PublishAction;
import ccc.contentcreator.actions.UnlockAction;
import ccc.contentcreator.actions.UnpublishAction;
import ccc.contentcreator.actions.UpdateMetadataAction;
import ccc.contentcreator.actions.UpdateResourceRolesAction;
import ccc.contentcreator.actions.UpdateSortOrderAction;
import ccc.contentcreator.actions.UpdateTagsAction;
import ccc.contentcreator.actions.ViewHistoryAction;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.google.gwt.core.client.GWT;


/**
 * Design for the main menu toolbar.
 *
 * @author Civic Computing Ltd.
 */
public class MainMenu
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    private final Menu _itemMenu = new Menu();
    private final UserSummary _user;
    /**
     * Constructor.
     *
     * @param user UserSummary of the currently logged in user.
     */
    MainMenu(final UserSummary user) {
        _user = user;
        addMenu(
            "help-menu",
            _constants.help(),
            createMenuItem(
                "open-manual",
                _constants.manual(),
                new OpenHelpAction())
        );

        if (_user.getRoles().contains(IGlobals.ADMINISTRATOR)) {
            addMenu(
                "users-menu",
                _constants.users(),
                createMenuItem(
                    "create-user-menu-item",
                    _constants.createUser(),
                    new CreateUserAction())
            );
        }

        if (_user.getRoles().contains(IGlobals.ADMINISTRATOR)
                || _user.getRoles().contains(IGlobals.SITE_BUILDER)) {
            createContentRootMenu();
        }

        addMenu(
            "tools-menu",
            _constants.tools(),
            createMenuItem(
                "logout-menu-item",
                _constants.logout(),
                new LogoutAction())
        );
    }

    private void createContentRootMenu() {

        final TextToolItem item =
            new TextToolItem(_constants.contentRoot());
        item.setId("contentRoot-menu");

        _itemMenu.addListener(Events.BeforeShow, new Listener<MenuEvent>() {
            public void handleEvent(final MenuEvent be) {
                _itemMenu.removeAll();
                final QueriesServiceAsync qs = Globals.queriesService();
                qs.roots(new ErrorReportingCallback<Collection<ResourceSummary>>(USER_ACTIONS.internalAction()){
                    public void onSuccess(final Collection<ResourceSummary> roots) {
                        for (final ResourceSummary root : roots) {
                            if ("content".equals(root.getName())) {
                                addRootMenuItems(root);
                            }
                        }
                    }
                });
            }
        });

        item.setMenu(_itemMenu);
        add(item);
    }

    private void addRootMenuItems(final ResourceSummary root) {
        final SingleSelectionModel ssm = createSsm(root);

        _itemMenu.add(createMenuItem(
            "details-root",
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
                    Globals.alert(sb.toString());
                }

            }));

        _itemMenu.add(createMenuItem(
            "viewHistory-root",
            _constants.viewHistory(),
            new ViewHistoryAction(ssm)));

        if (root.getLockedBy() == null
            || root.getLockedBy().toString().equals("")) {
            _itemMenu.add(createMenuItem(
                "lock-root",
                _constants.lock(),
                new LockAction(ssm)));
        } else {
            if (root.getLockedBy().equals(_user.getUsername())
                    || _user.getRoles().contains(IGlobals.ADMINISTRATOR)) {

                _itemMenu.add(createMenuItem(
                    "unlock-root",
                    _constants.unlock(),
                    new UnlockAction(ssm)));
            }
            if (root.getLockedBy().equals(_user.getUsername())) {
                if (root.getPublishedBy() == null
                        || root.getPublishedBy().toString().equals("")) {
                    _itemMenu.add(createMenuItem(
                        "publish-root",
                        _constants.publish(),
                        new PublishAction(ssm)));
                } else {
                    _itemMenu.add(createMenuItem(
                        "unpublish-root",
                        _constants.unpublish(),
                        new UnpublishAction(ssm)));
                }
                _itemMenu.add(createMenuItem(
                    "chooseTemplate-root",
                    _constants.chooseTemplate(),
                    new ChooseTemplateAction(ssm)));
                _itemMenu.add(createMenuItem(
                    "changeSortOrder-root",
                    _constants.changeSortOrder(),
                    new UpdateSortOrderAction(ssm)));
                _itemMenu.add(createMenuItem(
                    "updateRoles-root",
                    _constants.updateRoles(),
                    new UpdateResourceRolesAction(ssm)));
                _itemMenu.add(createMenuItem(
                    "updateTags-root",
                    _constants.updateTags(),
                    new UpdateTagsAction(ssm)));
                _itemMenu.add(createMenuItem(
                    "updateMetadata-root",
                    _constants.updateMetadata(),
                    new UpdateMetadataAction(ssm)));
                _itemMenu.add(createMenuItem(
                    "cacheDuration-root",
                    _constants.cacheDuration(),
                    new EditCacheAction(ssm)));

            }
        }

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
