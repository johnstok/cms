/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;


/**
 * A GUI action.
 *
 * @author Civic Computing Ltd.
 */
public interface Action {
    IGlobals GLOBALS = new IGlobalsImpl();
    ActionNameConstants USER_ACTIONS = GLOBALS.userActions();
    UIConstants UI_CONSTANTS = GLOBALS.uiConstants();
    CommandServiceAsync _cs = GLOBALS.commandService();
    QueriesServiceAsync _qs = GLOBALS.queriesService();

    /**
     * Perform the action.
     */
    void execute();
}
