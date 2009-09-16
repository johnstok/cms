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
import ccc.contentcreator.api.UIConstants;


/**
 * A GUI action.
 *
 * @author Civic Computing Ltd.
 */
public interface Action {

    /** GLOBALS : IGlobals. */
    IGlobals GLOBALS = new IGlobalsImpl();

    /** USER_ACTIONS : ActionNameConstants. */
    ActionNameConstants USER_ACTIONS = GLOBALS.userActions();

    /** UI_CONSTANTS : UIConstants. */
    UIConstants UI_CONSTANTS = GLOBALS.uiConstants();

    /**
     * Perform the action.
     */
    void execute();
}
