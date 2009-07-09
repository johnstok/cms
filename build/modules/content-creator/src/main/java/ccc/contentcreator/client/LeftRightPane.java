/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
