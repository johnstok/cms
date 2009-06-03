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

import ccc.contentcreator.actions.CancelActionAction;
import ccc.contentcreator.api.UIConstants;


/**
 * A toolbar for manipulating scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();
    private final ActionTable _actionTable;

    /**
     * Constructor.
     *
     * @param actionTable The table to operate on.
     */
    public ActionToolBar(final ActionTable actionTable) {
        _actionTable = actionTable;

        addSeparator();
        addButton(
            "cancel-action",
            _constants.cancel(),
            new CancelActionAction(_actionTable));
        addSeparator();
    }

}
