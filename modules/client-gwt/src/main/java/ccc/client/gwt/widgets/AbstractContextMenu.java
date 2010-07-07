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

import ccc.client.core.Action;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;


/**
 * Abstract class for contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractContextMenu
    extends
        Menu {

    private final Globals _globals = new GlobalsImpl();
    private final UIConstants _constants = I18n.UI_CONSTANTS;


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public Globals getGlobals() {
        return _globals;
    }


    /**
     * Accessor.
     *
     * @return Returns the constants.
     */
    public UIConstants getConstants() {
        return _constants;
    }


    /**
     * Creates a new menu item.
     *
     * @param id The id of the menu item.
     * @param text The text of the menu item.
     * @param action The action  of the menu item.
     * @return  {@link MenuItem}.
     */
    protected MenuItem createMenuItem(final String id,
                                      final String text,
                                      final Action action) {
        final MenuItem item = new MenuItem();
        item.setId(id);
        item.setText(text);
        item.addSelectionListener(new MenuSelectionListenerAction(action));
        return item;
    }


    /**
     * Creates and adds a menu item to the context menu.
     *
     * @param permission The permission required to add the item.
     * @param id The id of the menu item.
     * @param text The text of the menu item.
     * @param action The action  of the menu item.
     */
    protected void addMenuItem(final String permission,
                               final String id,
                               final String text,
                               final Action action) {
        if (permission == null
                || _globals.currentUser().hasPermission(permission)) {
            final MenuItem menuItem = createMenuItem(id, text, action);
            add(menuItem);
        }
    }

}
