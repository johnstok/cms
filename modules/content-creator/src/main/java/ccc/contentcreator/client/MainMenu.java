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

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.ResourceServiceAsync;
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

    private final Application _app;

    /**
     * Constructor.
     *
     * @param app
     */
    MainMenu(final Application app) {

        _app = app;

        final TextToolItem help = new TextToolItem(_app.constants().help());
        help.setId("help-menu");
        final Menu helpMenu = new Menu();
        help.setMenu(helpMenu);
        final MenuItem openManual = new MenuItem();
        openManual.setText(_app.constants().manual());
        openManual.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                Window.open("manual/ContentCreatorManual.html",
                  "_blank", "resizable=yes,scrollbars=yes,status=no");
            }
        });
        helpMenu.add(openManual);

        final TextToolItem assets = new TextToolItem(_app.constants().assets());
        assets.setId("assets-menu");
        final Menu assetsMenu = new Menu();
        assets.setMenu(assetsMenu);
        final MenuItem createTemplate = new MenuItem();
        createTemplate.setId("create-template-menu-item");
        createTemplate.setText(_app.constants().createDisplayTemplate());
        createTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                new CreateContentTemplateDialog(_app).center();
            }
        });
        assetsMenu.add(createTemplate);

        final TextToolItem tools = new TextToolItem(_app.constants().tools());
        tools.setId("tools-menu");
        final Menu toolsMenu = new Menu();
        tools.setMenu(toolsMenu);

        final MenuItem updateOptions = new MenuItem();
        updateOptions.setId("update-options-menu-item");
        updateOptions.setText(_app.constants().options());
        updateOptions.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                final ResourceServiceAsync resourceService =
                    _app.lookupService();

                resourceService.listOptions(
                 new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                     public void onFailure(final Throwable arg0) {
                         Window.alert(_app.constants().error());
                     }

                     public void onSuccess(
                                final List<OptionDTO<? extends DTO>> options) {
                         new UpdateOptionsDialog(_app, options).center();
                     }});
            }
        });
        toolsMenu.add(updateOptions);

        add(assets);
        add(tools);
        add(help);
    }
}
