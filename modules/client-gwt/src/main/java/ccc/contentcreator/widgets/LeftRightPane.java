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
package ccc.contentcreator.widgets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;


/**
 * Layout for dividing the UI screen to a tree and a table.
 *
 * @author Civic Computing Ltd.
 */
public class LeftRightPane extends LayoutContainer {

    private final BorderLayoutData _rightData =
        new BorderLayoutData(LayoutRegion.CENTER);
    private final BorderLayoutData _leftData =
        new BorderLayoutData(LayoutRegion.WEST, 400);

    private LayoutContainer _right;

    /**
     * Constructor.
     */
    public LeftRightPane() {

        setLayout(new BorderLayout());

        _leftData.setSplit(true);
        _leftData.setCollapsible(true);
        _leftData.setMargins(new Margins(5, 0, 5, 5));

        _rightData.setMargins(new Margins(5));
    }

    /**
     * Sets specified component to the right hand pane and refreshes the layout.
     *
     * @param pane The component to be set for the right hand pane.
     */
    public void setRightHandPane(final LayoutContainer pane) {
        if (null!=_right) {
            remove(_right);
        }
        _right = pane;
        add(pane, _rightData);
        layout();
    }

    /**
     * Sets specified component to the left hand pane.
     *
     * @param pane The component to be set for the left hand pane.
     */
    public void setLeftHandPane(final LayoutContainer pane) {
        add(pane, _leftData);
    }

}
