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
package ccc.view.contentcreator.controls;


import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface PanelControl extends Control {

    /**
     * TODO: Add a description of this method.
     *
     * @param alignBottom
     */
    void setVerticalAlignment(VerticalAlignmentConstant alignBottom);

    /**
     * TODO: Add a description of this method.
     *
     * @param alignRight
     */
    void setHorizontalAlignment(HorizontalAlignmentConstant alignRight);

    /**
     * TODO: Add a description of this method.
     *
     * @param frame
     */
    void add(Control frame);

    /**
     * TODO: Add a description of this method.
     *
     * @param i
     * @return
     */
    Control child(int i);

    /**
     * TODO: Add a description of this method.
     *
     */
    void clear();

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     */
    void setStyleName(String string);

    /**
     * TODO: Add a description of this method.
     *
     */
    void submit();

}
