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
package ccc.client.gwt.widgets;

import ccc.api.types.Permission;
import ccc.client.actions.CancelActionAction;
import ccc.client.core.I18n;
import ccc.client.i18n.UIConstants;


/**
 * A toolbar for manipulating scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = I18n.uiConstants;

    /**
     * Constructor.
     *
     * @param actionTable The table to operate on.
     */
    public ActionToolBar(final ActionTable actionTable) {

        addSeparator(null);
        addButton(Permission.ACTION_CANCEL,
            "cancel-action",
            _constants.cancel(),
            new CancelActionAction(actionTable));
        addSeparator(Permission.ACTION_CANCEL);
    }

}
