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

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.widget.ContentPanel;


/**
 * Abstract base class for table panels.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TablePanel extends ContentPanel {

    private static final IGlobals GLOBALS = new IGlobalsImpl();

    /** USER_ACTIONS : ActionNameConstants. */
    protected final ActionNameConstants USER_ACTIONS = GLOBALS.userActions();
    /** UI_CONSTANTS : UIConstants. */
    protected static final UIConstants UI_CONSTANTS = GLOBALS.uiConstants();
    /** PAGING_ROW_COUNT : int. */
    protected static final int PAGING_ROW_COUNT = 20;
}
