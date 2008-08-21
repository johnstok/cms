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
package ccc.view.contentcreator.dialogs;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface GridControl extends Control {

    /**
     * TODO: Add a description of this method.
     *
     * @param row
     * @param i
     * @param widget
     */
    void setWidget(int row, int i, Control widget);

    /**
     * TODO: Add a description of this method.
     *
     * @param i
     * @param j
     * @param type
     */
    void setText(int i, int j, String type);

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     */
    void ensureDebugId(String string);

}
