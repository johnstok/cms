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

import ccc.contentcreator.core.Action;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * Adapter class - wires an action to a GWT selection listener.
 *
 * @author Civic Computing Ltd.
 */
public final class ButtonSelectionListenerAction
    extends
        SelectionListener<ButtonEvent> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The Action.
     */
    public ButtonSelectionListenerAction(final Action action) {
        _action = action;
    }

    /** {@inheritDoc} */
    @Override public void componentSelected(final ButtonEvent ce) {
        _action.execute();
    }
}
