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

import ccc.client.gwt.core.Action;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * Abstract class providing helper methods for creating a toolbar.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractToolBar
    extends
        ToolBar {

    /**
     * Adds a new {@link LabelToolItem}.
     *
     * @param text The text for label.
     */
    protected void addLabel(final String text) {
        add(new LabelToolItem(text));
    }

    /**
     * Adds a new {@link SeparatorToolItem}.
     *
     */
    protected void addSeparator() {
        add(new SeparatorToolItem());
    }

    /**
     * Adds a new button {@link TextToolItem} to the toolbar.
     *
     * @param id The id of the button.
     * @param text The text of the button.
     * @param action The action of the button.
     */
    protected void addButton(final String id,
                             final String text,
                             final Action action) {
        final Button toolItem = new Button(text);
        toolItem.setId(id);
        toolItem.addListener(Events.Select, new ListenerAction(action));
        add(toolItem);
    }

    /**
     * Adds a new menu {@link TextToolItem} to the toolbar.
     *
     * @param id The id of the menu.
     * @param text The text of the menu.
     * @param children Children of the menu.
     */
    protected void addMenu(final String id,
                           final String text,
                           final MenuItem... children) {
        final Button item = new Button(text);
        item.setId(id);
        final Menu itemMenu = new Menu();
        for (final MenuItem child : children) {
            itemMenu.add(child);
        }
        item.setMenu(itemMenu);
        add(item);
    }

    /**
     * Creates a new {@link MenuItem}.
     *
     * @param id The id of the menu item.
     * @param text The text of the menu item.
     * @param action The action of the item.
     * @return The created MenuItem
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
}
