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

import java.util.List;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.dialogs.CreatePageDialog;
import ccc.contentcreator.client.dialogs.CreateUserDialog;
import ccc.contentcreator.client.dialogs.EditContentTemplateDialog;
import ccc.contentcreator.client.dialogs.UpdateOptionsDialog;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;

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

        final TextToolItem assets = new TextToolItem(_constants.assets());
        assets.setId("assets-menu");
        final Menu assetsMenu = new Menu();
        assets.setMenu(assetsMenu);
        final MenuItem createTemplate = new MenuItem();
        createTemplate.setId("create-template-menu-item");
        createTemplate.setText(_constants.createDisplayTemplate());
        createTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                new EditContentTemplateDialog().show();
            }
        });
        assetsMenu.add(createTemplate);
        final MenuItem createPage = new MenuItem();
        createPage.setId("create-page-menu-item");
        createPage.setText(_constants.createPage());
        createPage.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                new CreatePageDialog().show();
            }
        });
        assetsMenu.add(createPage);

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
                new CreateUserDialog().show();
            }
        });
        usersMenu.add(createUser);

        final TextToolItem tools = new TextToolItem(_constants.tools());
        tools.setId("tools-menu");
        final Menu toolsMenu = new Menu();
        tools.setMenu(toolsMenu);

        final MenuItem updateOptions = new MenuItem();
        updateOptions.setId("update-options-menu-item");
        updateOptions.setText(_constants.options());
        updateOptions.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                final ResourceServiceAsync resourceService =
                    Globals.resourceService();

                resourceService.listOptions(
                 new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                     public void onFailure(final Throwable arg0) {
                         Window.alert(_constants.error());
                     }

                     public void onSuccess(
                                final List<OptionDTO<? extends DTO>> options) {
                         new UpdateOptionsDialog( options).show();
                     }});
            }
        });
        toolsMenu.add(updateOptions);

        add(assets);
        add(users);
        add(tools);
        add(help);
    }
}
