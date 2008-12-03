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

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MainMenu
    extends
        ToolBar {

    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     * @param app
     */
    MainMenu() {

        final TextToolItem help = new TextToolItem(_constants.help());
        help.setId("help-menu");
        final Menu helpMenu = new Menu();
        help.setMenu(helpMenu);
        final MenuItem openManual = new MenuItem();
        openManual.setText(_constants.manual());
        openManual.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                Window.open("manual/ContentCreatorManual.html",
                  "_blank", "resizable=yes,scrollbars=yes,status=no");
            }
        });
        helpMenu.add(openManual);

        final TextToolItem users = new TextToolItem(_constants.users());
        users.setId("users-menu");
        final Menu usersMenu = new Menu();
        users.setMenu(usersMenu);
        final MenuItem createUser = new MenuItem();
        createUser.setId("create-user-menu-item");
        createUser.setText(_constants.createUser());
        createUser.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                Globals.queriesService().loggedInUser(
                    new ErrorReportingCallback<UserSummary>() {
                    public void onSuccess(final UserSummary user) { // FIXME: Broken.
//                        if (user._roles.contains("ADMINISTRATOR")) {
//                            new CreateUserDialog().show();
//                        } else {
                            Globals.alert(_constants.notAllowedAction());
//                        }
                    }
                });

            }
        });
        usersMenu.add(createUser);

        final TextToolItem tools = new TextToolItem(_constants.tools());
        tools.setId("tools-menu");
        final Menu toolsMenu = new Menu();
        tools.setMenu(toolsMenu);

//        createUpdateOptionsItem(toolsMenu);
        createLogoutItem(toolsMenu);

        add(users);
        add(tools);
        add(help);
    }

//    private void createUpdateOptionsItem(final Menu toolsMenu) {
//
//        final MenuItem updateOptions = new MenuItem();
//        updateOptions.setId("update-options-menu-item");
//        updateOptions.setText(_constants.options());
//        updateOptions.addSelectionListener(new SelectionListener<MenuEvent>() {
//            @Override
//            public void componentSelected(final MenuEvent ce) {
//                final ResourceServiceAsync resourceService =
//                    Globals.resourceService();
//
//                Globals.queriesService().listOptions(
//                 new AsyncCallback<List<OptionDTO<? extends DTO>>>(){
//
//                     public void onFailure(final Throwable arg0) {
//                         Window.alert(_constants.error());
//                     }
//
//                     public void onSuccess(
//                                final List<OptionDTO<? extends DTO>> options) {
//                         new UpdateOptionsDialog(options).show();
//                     }});
//            }
//        });
//        toolsMenu.add(updateOptions);
//    }

    private void createLogoutItem(final Menu menu) {

        final MenuItem item = new MenuItem();
        item.setId("logout-menu-item");
        item.setText(_constants.logout());
        item.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                Globals.commandService().logout(
                    new AsyncCallback<Void>(){

                        public void onFailure(final Throwable arg0) {
                            // TODO: Anything we can do here?!
                            Globals.alert("Error logging out: "+arg0);
                        }

                        public void onSuccess(final Void result) {
                            Globals.disableExitConfirmation();
                            Globals.redirectTo(Globals.APP_URL);
                        }});
            }
        });
        menu.add(item);
    }
}
