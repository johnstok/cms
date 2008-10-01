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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LeftRightPane extends LayoutContainer {

    private final BorderLayoutData _rightData =
        new BorderLayoutData(LayoutRegion.CENTER);
    private final BorderLayoutData _leftData =
        new BorderLayoutData(LayoutRegion.WEST, 400);

    LayoutContainer _left;
    LayoutContainer _right;

    /**
     * Constructor.
     */
    LeftRightPane() {

        setLayout(new BorderLayout());

        _leftData.setSplit(true);
        _leftData.setCollapsible(true);
        _leftData.setMargins(new Margins(5, 0, 5, 5));

        _rightData.setMargins(new Margins(5));
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param pane
     */
    public void setRightHandPane(final LayoutContainer pane) {
        if (null!=_right) { remove(_right);};
        _right = pane;
        add(pane, _rightData);
        this.layout();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param pane
     */
    public void setLeftHandPane(final LayoutContainer pane) {
        add(pane, _leftData);
    }

}
