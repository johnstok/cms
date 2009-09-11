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

import ccc.contentcreator.api.UIConstants;

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

    private final IGlobals _globals = new IGlobalsImpl();
    private final UIConstants _constants = _globals.uiConstants();


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public IGlobals getGlobals() {
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
     * @param id The id of the menu item.
     * @param text The text of the menu item.
     * @param action The action  of the menu item.
     */
    protected void addMenuItem(final String id,
                               final String text,
                               final Action action) {
        final MenuItem menuItem = createMenuItem(id, text, action);
        add(menuItem);
    }
}
