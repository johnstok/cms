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
package ccc.contentcreator.controls;


import com.google.gwt.user.client.ui.ChangeListener;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface ListControl extends Control {

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param string2
     */
    void addItem(String string, String string2);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    int getItemCount();

    /**
     * TODO: Add a description of this method.
     *
     * @param i
     * @return
     */
    Object getValue(int i);

    /**
     * TODO: Add a description of this method.
     *
     * @param i
     */
    void setSelectedIndex(int i);

    /**
     * TODO: Add a description of this method.
     *
     * @param changeListener
     */
    void addChangeListener(ChangeListener changeListener);

}
