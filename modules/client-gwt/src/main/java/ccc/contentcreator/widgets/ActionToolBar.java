/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.widgets;

import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.i18n.UIConstants;
import ccc.contentcreator.remoting.CancelActionAction;


/**
 * A toolbar for manipulating scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = new GlobalsImpl().uiConstants();
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
