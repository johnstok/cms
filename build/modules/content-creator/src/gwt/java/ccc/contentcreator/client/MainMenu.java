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

import ccc.contentcreator.actions.CreateUserAction;
import ccc.contentcreator.actions.LogoutAction;
import ccc.contentcreator.actions.OpenHelpAction;
import ccc.contentcreator.api.UIConstants;


/**
 * Design for the main menu toolbar.
 *
 * @author Civic Computing Ltd.
 */
public class MainMenu
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     * @param app
     */
    MainMenu() {

        addMenu(
            "help-menu",
            _constants.help(),
            createMenuItem(
                "open-manual",
                _constants.manual(),
                new OpenHelpAction())
        );

        addMenu(
            "users-menu",
            _constants.users(),
            createMenuItem(
                "create-user-menu-item",
                _constants.createUser(),
                new CreateUserAction())
        );

        addMenu(
            "tools-menu",
            _constants.tools(),
            createMenuItem(
                "logout-menu-item",
                _constants.logout(),
                new LogoutAction())
        );
    }
}
