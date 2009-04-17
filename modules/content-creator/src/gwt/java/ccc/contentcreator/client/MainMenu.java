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
import java.util.Set;

import ccc.contentcreator.actions.ChooseRootTemplateAction;
import ccc.contentcreator.actions.CreateUserAction;
import ccc.contentcreator.actions.LockRootAction;
import ccc.contentcreator.actions.LogoutAction;
import ccc.contentcreator.actions.OpenHelpAction;
import ccc.contentcreator.actions.PublishRootAction;
import ccc.contentcreator.actions.UnlockRootAction;
import ccc.contentcreator.actions.UnpublishRootAction;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;


/**
 * Design for the main menu toolbar.
 *
 * @author Civic Computing Ltd.
 */
public class MainMenu
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();
    private final Menu _itemMenu = new Menu();

    /**
     * Constructor.
     *
     * @param roles Set of roles.
     */
    MainMenu(final Set<String> roles) {

        addMenu(
            "help-menu",
            _constants.help(),
            createMenuItem(
                "open-manual",
                _constants.manual(),
                new OpenHelpAction())
        );

        if (roles.contains("ADMINISTRATOR")) {
            addMenu(
                "users-menu",
                _constants.users(),
                createMenuItem(
                    "create-user-menu-item",
                    _constants.createUser(),
                    new CreateUserAction())
            );
        }

        if (roles.contains("ADMINISTRATOR") || roles.contains("SITE_BUILDER")) {
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
                qs.roots(new ErrorReportingCallback<Collection<ResourceSummary>>(){
                    public void onSuccess(final Collection<ResourceSummary> roots) {
                        for (final ResourceSummary root : roots) {
                            if ("content".equals(root._name)) {
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

        if (root._lockedBy == null || root._lockedBy.equals("")) {
            _itemMenu.add(createMenuItem(
                "lock-root",
                _constants.lock(),
                new LockRootAction(root._id)));
        } else {
            _itemMenu.add(createMenuItem(
                "unlock-root",
                _constants.unlock(),
                new UnlockRootAction(root._id)));
        }
        if (root._publishedBy == null || root._publishedBy.equals("")) {
            _itemMenu.add(createMenuItem(
                "publish-root",
                _constants.publish(),
                new PublishRootAction(root._id)));
        } else {
            _itemMenu.add(createMenuItem(
                "unpublish-root",
                _constants.unpublish(),
                new UnpublishRootAction(root._id)));
        }

        _itemMenu.add(createMenuItem(
            "chooseTemplate-root",
            _constants.chooseTemplate(),
            new ChooseRootTemplateAction(root._id)));

    }
}
