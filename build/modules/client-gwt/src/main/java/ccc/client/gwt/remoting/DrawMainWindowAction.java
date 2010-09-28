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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.remoting;

import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.client.core.InternalServices;
import ccc.client.gwt.widgets.LeftRightPane;
import ccc.client.gwt.widgets.MainMenu;
import ccc.client.gwt.widgets.ResourceNavigator;
import ccc.client.remoting.GetRootsAction;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Draws the GXT client's main window.
 *
 * @author Civic Computing Ltd.
 */
public final class DrawMainWindowAction
    extends
        GetRootsAction {

    /** _user : UserSummary. */
    private final User _user;

    /**
     * Constructor.
     *
     * @param user The currently logged in user.
     */
    public DrawMainWindowAction(final User user) {
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final PagedCollection<ResourceSummary> roots) {
        InternalServices.ROOTS = roots;
        final LeftRightPane contentPane = new LeftRightPane();
        contentPane.setRightHandPane(new ContentPanel());
        ResourceSummary root = null;
        for (final ResourceSummary rr : InternalServices.ROOTS.getElements()) {
            if (rr.getName().toString().equals("content")) {
                root = rr;
            }
        }
        contentPane.setLeftHandPane(
            new ResourceNavigator(contentPane,
                root,
                _user));

        final Viewport vp =
            layoutMainWindow(new MainMenu(_user), contentPane);

        RootPanel.get().add(vp);
    }

    /**
     * Lay out the GUI components of the main window.
     *
     * @param menu The tool-bar for the main menu.
     * @param content The layout container for the content panel.
     * @return The layout as a GXT view-port for rendering by the browser.
     */
    private Viewport layoutMainWindow(final ToolBar menu,
                                      final LayoutContainer content) {

        final LayoutContainer vp = new LayoutContainer();
         vp.setLayout(new RowLayout());
         vp.add(menu, new RowData(1, -1));
         vp.add(content, new RowData(1, 1));

         final Viewport v = new Viewport();
         v.setLayout(new FitLayout());
         v.add(vp);

        return v;
    }
}
