/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.i18n.ActionNameConstants;
import ccc.client.gwt.i18n.UIConstants;

import com.extjs.gxt.ui.client.widget.ContentPanel;


/**
 * Abstract base class for table panels.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TablePanel extends ContentPanel {

    /** GLOBALS : IGlobals. */
    protected static final Globals GLOBALS = new GlobalsImpl();

    /** USER_ACTIONS : ActionNameConstants. */
    protected static final ActionNameConstants USER_ACTIONS =
        GlobalsImpl.userActions();

    /** UI_CONSTANTS : UIConstants. */
    protected static final UIConstants UI_CONSTANTS = GlobalsImpl.uiConstants();

    /** PAGING_ROW_COUNT : int. */
    protected static final int PAGING_ROW_COUNT = 20;
}
